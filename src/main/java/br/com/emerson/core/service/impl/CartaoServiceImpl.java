package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.app.entrypoint.cron.service.TemporizadorService;
import br.com.emerson.core.dataprovider.CartaoRepository;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import br.com.emerson.core.service.CarteiraService;
import br.com.emerson.core.service.MovimentacaoService;
import br.com.emerson.domain.exception.BusinessException;
import br.com.emerson.mapper.CartaoMapper;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CartaoServiceImpl implements CartaoService {

    @Inject
    CartaoRepository cartaoRepository;

    @Inject
    CarteiraService carteiraService;

    @Inject
    MovimentacaoService movimentacaoService;

//    @Inject
//    TemporizadorService temporizadorService;

    @Override
    @Transactional
    public CartaoEntity salvar(CartaoRequest request) {
        log.info("Persistindo cartao...." + request.getDigitosFinais());
        cartaoRepository.find("where UPPER(apelido) =:apelido",
                        Parameters.with("apelido", request.getApelido().toUpperCase()))
                .stream().findFirst().ifPresent(found -> {
                    if (found.getDigitosFinais().equalsIgnoreCase(request.getDigitosFinais())) {
                        throw new BusinessException("Cartao ja existe");
                    }
                });

        var cartaoEntity = CartaoMapper.toCartaoEntityByRequest(request);
        cartaoRepository.persistAndFlush(cartaoEntity);

        if (cartaoEntity.getTipoCartao().equals(TipoCartaoEnum.DEBITO)) {
            log.info("Criando carteira de DEBITO para o cartao vinculado ---> " + cartaoEntity.getApelido());
            carteiraService.findCarteiraById(request.getIdCarteira()).ifPresentOrElse(carteira -> {
                log.info("Gerando movimentacao---> " + LocalDateTime.now());
                carteira.setIdCartao(cartaoEntity.getIdCartao());
                this.carteiraService.salvar(carteira);
            }, () -> {
                throw new BusinessException("Necessario vincular o cartao há uma carteira valida!");
            });
        }
        return cartaoEntity;
    }

    @Override
    @Transactional
    public void atualizar(UUID uuid, CartaoRequest request) {
        var entity = cartaoRepository.find("where UPPER(apelido) =:apelido",
                        Parameters.with("apelido", request.getApelido().toUpperCase()))
                .stream().findAny().filter(found -> found.getIdCartao().equals(uuid))
                .orElseThrow(BusinessException::repeatData);
        if(!entity.getTipoCartao().equals(TipoCartaoEnum.DEBITO)){
            entity.setApelido(request.getApelido());
            entity.setTipoCartao(request.getTipoCartao());
            entity.setDigitosFinais(request.getDigitosFinais());
            entity.setDiaVencimento(request.getDiaVencimento());
            entity.setDiaFechamento(request.getDiaFechamento());
            entity.setVlLimiteTotal(request.getVlLimiteTotal());
            entity.setVlLimiteUtilizado(request.getVlLimiteUtilizado());
        }else{
            entity.setApelido(request.getApelido());
            entity.setTipoCartao(request.getTipoCartao());
            entity.setDigitosFinais(request.getDigitosFinais());
        }
        this.cartaoRepository.persistAndFlush(entity);
    }

    @Override
    @Transactional
    public CartaoEntity atualizarLimite(UUID uuid, BigDecimal valorUtilizado) {
        var entity = cartaoRepository.findById(uuid);
        entity.setVlLimiteUtilizado(valorUtilizado);
        cartaoRepository.persistAndFlush(entity);
        return entity;
    }

    @Override
    public List<CartaoEntity> listerCartaoDebito() {
        return this.cartaoRepository.find("tipoCartao =?1", TipoCartaoEnum.DEBITO).list();
    }

    @Override
    public List<CartaoEntity> listarCartaoCredito() {
        return this.cartaoRepository.find("tipoCartao <> ?1", TipoCartaoEnum.DEBITO).list();
    }

    @Override
    public List<CartaoEntity> listarTodos() {
        return cartaoRepository.findAll().list();
    }

    @Override
    public List<CartaoResponse> buscarByNomeCartao(String nome) {
        return cartaoRepository.findAll().list().stream().filter(filter -> filter
                .getApelido().equalsIgnoreCase(nome)).map(found ->
                CartaoResponse.builder().idCartao(found.getIdCartao())
                        .apelido(found.getApelido())
                        .tipoCartao(found.getTipoCartao())
                        .digitosFinais(found.getDigitosFinais())
                        .diaFechamento(found.getDiaFechamento())
                        .diaVencimento(found.getDiaVencimento())
                        .vlLimiteTotal(found.getVlLimiteTotal())
                        .vlLimiteUtilizado(found.getVlLimiteUtilizado())
                        .vlSaldo(found.getCarteira().getValor())
                        .cartaoReferencia(found.getIdCartaoReferencia())
                        .compras(found.getCompras())
                        .build())
                .toList();
    }

    @Override
    public List<CartaoResponse> buscarByTipoCartao(TipoCartaoEnum tipoCartaoEnum) {
        return cartaoRepository.findAll().list().stream().filter(filter -> filter
                .getTipoCartao().equals(tipoCartaoEnum)).map(found ->
                CartaoResponse.builder().idCartao(found.getIdCartao())
                        .apelido(found.getApelido())
                        .tipoCartao(found.getTipoCartao())
                        .digitosFinais(found.getDigitosFinais())
                        .diaFechamento(found.getDiaFechamento())
                        .diaVencimento(found.getDiaVencimento())
                        .vlLimiteTotal(found.getVlLimiteTotal())
                        .vlLimiteUtilizado(found.getVlLimiteUtilizado())
                        .vlSaldo(found.getCarteira().getValor())
                        .cartaoReferencia(found.getIdCartaoReferencia())
                        .compras(found.getCompras())
                        .build()).toList();
    }

    @Override
    public List<CartaoResponse> buscarByValor(List<BigDecimal> range) {
        return cartaoRepository.findAll().list().stream().filter(filter -> filter
                .getVlLimiteUtilizado().compareTo(range.get(0)) > 1 && filter
                .getVlLimiteUtilizado().compareTo(range.get(1)) > 0).map(found ->
                CartaoResponse.builder().idCartao(found.getIdCartao())
                        .apelido(found.getApelido())
                        .tipoCartao(found.getTipoCartao())
                        .digitosFinais(found.getDigitosFinais())
                        .diaFechamento(found.getDiaFechamento())
                        .diaVencimento(found.getDiaVencimento())
                        .vlLimiteTotal(found.getVlLimiteTotal())
                        .vlLimiteUtilizado(found.getVlLimiteUtilizado())
                        .vlSaldo(found.getCarteira().getValor())
                        .cartaoReferencia(found.getIdCartaoReferencia())
                        .compras(found.getCompras())
                        .build()).toList();
    }

    @Override
    public List<CartaoEntity> listarCartoes() {
        return cartaoRepository.findAll().list();
    }

    @Override
    public CartaoEntity findCartaoById(UUID idCartao) {
        return cartaoRepository.findById(idCartao);
    }

    @Override
    @Transactional
    public void inativarCartao(UUID idCartao) {
        CartaoEntity cartao = cartaoRepository.findById(idCartao);
        cartao.setIcAtivo(Boolean.FALSE);
        cartaoRepository.persistAndFlush(cartao);
    }
}
