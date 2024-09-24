package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.ComprasRequest;
import br.com.emerson.core.dataprovider.ComprasRepository;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.ComprasEntity;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;
import br.com.emerson.core.entity.ParcelaEntity;
import br.com.emerson.core.enums.MovimentacaoEnum;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.*;
import br.com.emerson.domain.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class ComprasServiceImpl implements ComprasService {

    @Inject
    ComprasRepository comprasRepository;

    @Inject
    ParcelaService parcelaService;

    @Inject
    CartaoService cartaoService;

    @Inject
    CarteiraService carteiraService;

    @Inject
    MovimentacaoService movimentacaoService;

    @Override
    @Transactional
    public void salvar(ComprasRequest request) {
        CartaoEntity cartao = cartaoService.findCartaoById(request.getIdCartao());
        BigDecimal valorSaldoRestante = !cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ?
                cartao.getVlLimiteTotal().subtract(cartao.getVlLimiteUtilizado()) :
                cartao.getCarteira().getValor().subtract(request.getValorCompra());

        if (valorSaldoRestante.floatValue() < request.getValorCompra().floatValue()) {
            throw new BusinessException("Saldo indisponivel para compra!");
        }

        var entity = ComprasEntity.builder()
                .qtdParcelas(request.getQtdParcelas())
                .dataCompra(request.getDataCompra())
                .tipoLancamento(request.getTipoLancamento())
                .valorCompra(request.getValorCompra())
                .descricao(request.getDescricao())
                .idCartao(cartao.getIdCartao())
                .cartao(cartao)
                .build();
        comprasRepository.persistAndFlush(entity);

        List<ParcelaEntity> parcelas = request.getTipoLancamento().build(entity);
        parcelas.forEach(p -> {
            if (cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO)) {
                var carteira = this.carteiraService.findCarteiraByIdCartao(p.getCompra().getIdCartao());
                carteira.setValor(valorSaldoRestante);
                this.carteiraService.salvar(carteira);
                var movimentacao = MovimentacaoCarteiraEntity.builder()
                        .dtMovimentacao(LocalDate.now())
                        .valor(request.getValorCompra())
                        .tipoMovimentacao(MovimentacaoEnum.SAIDA)
                        .descricaoMovimento(request.getDescricao())
                        .idCarteira(carteira.getIdCarteira())
                        .build();
                this.movimentacaoService.salvarMovimentacao(movimentacao);
            }
            parcelaService.salvar(p);
        });
        if (!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO)) {
            BigDecimal result = valorSaldoRestante.subtract(request.getValorCompra());
            System.out.println(result);
            cartao.setVlLimiteUtilizado(cartao.getVlLimiteTotal().subtract(result));
        }
    }

    @Override
    @Transactional
    public List<ComprasEntity> listarComprasByDataAndCartao(UUID idCartao, LocalDate dataAbertura, LocalDate dataFechamento) {
        try {

            return comprasRepository.find("idCartao = ?1  ", idCartao).list();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
