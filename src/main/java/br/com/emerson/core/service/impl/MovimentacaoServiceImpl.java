package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.MovimentacaoRequest;
import br.com.emerson.core.dataprovider.MovimentacaoRepository;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;
import br.com.emerson.core.service.CarteiraService;
import br.com.emerson.core.service.MovimentacaoService;
import br.com.emerson.domain.exception.BusinessException;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class MovimentacaoServiceImpl implements MovimentacaoService {

    @Inject
    MovimentacaoRepository movimentacaoRepository;

    @Inject
    CarteiraService carteiraService;

    @Override
    @Transactional
    public void salvarMovimentacao(MovimentacaoCarteiraEntity entity) {
        this.movimentacaoRepository.persistAndFlush(entity);
    }

    @Override
    @Transactional
    public void gerarMovimentacaoCarteira(Long id, MovimentacaoRequest request) {
        var carteira = this.carteiraService.findCarteiraById(id);
        carteira.ifPresentOrElse(result -> {
            var mov = MovimentacaoCarteiraEntity.builder()
                    .idCarteira(result.getIdCarteira())
                    .dtMovimentacao(request.getDtMovimentacao())
                    .tipoMovimentacao(request.getTipoMovimentacao())
                    .valor(request.getValor())
                    .descricaoMovimento(request.getDescricaoMovimento())
                    .carteira(result)
                    .build();
            mov.getTipoMovimentacao().build(mov, result);
            this.movimentacaoRepository.persistAndFlush(mov);
            this.carteiraService.salvar(result);
        }, () -> {
            throw new BusinessException("Carteira Invalida para atualizacao de movimento");
        });

    }

    @Override
    public List<MovimentacaoCarteiraEntity> listarMovimentacaoAll() {
        return movimentacaoRepository.listAll();
    }

    @Override
    public List<MovimentacaoCarteiraEntity> listarMovimentacaoByIdCarteira(Long id) {
        return movimentacaoRepository.find("where idCarteira=:idCarteira", Parameters.with("idCarteira", id)).list();
    }

    @Override
    public List<MovimentacaoCarteiraEntity> listarMovimentacaoByIdCarteiraAndData(Long idCarteira, List<LocalDate> range) {
        LocalDate inicio = range.get(0);//.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fim = range.get(1);//.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return movimentacaoRepository.find("idCarteira =:idCarteira and dtMovimentacao between :inicio and :fim",
                Parameters.with("inicio", inicio)
                        .and("fim", fim)
                        .and("idCarteira", idCarteira))
                .list();
    }
}
