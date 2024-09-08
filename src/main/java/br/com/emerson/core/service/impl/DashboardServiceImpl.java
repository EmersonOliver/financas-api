package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.app.dto.response.ComprasResponse;
import br.com.emerson.app.dto.response.FaturaResponse;
import br.com.emerson.app.dto.response.FaturaResponseModel;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import br.com.emerson.core.service.DashboardService;
import br.com.emerson.core.service.FaturaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@ApplicationScoped
public class DashboardServiceImpl implements DashboardService {

    @Inject
    CartaoService cartaoService;

    @Inject
    FaturaService faturaService;

    @Override
    @Transactional
    public List<CartaoResponse> cartoesAll() {
        List<CartaoEntity> result = this.cartaoService.listarTodos();
      return result.stream().map(cartao->
        CartaoResponse.builder()
                .idCartao(cartao.getIdCartao())
                .apelido(cartao.getApelido())
                .digitosFinais(cartao.getDigitosFinais())
                .diaFechamento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaFechamento() : null)
                .diaVencimento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaVencimento() : null)
                .tipoCartao(cartao.getTipoCartao())
                .vlLimiteTotal(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal() : null)
                .vlLimiteUtilizado(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteUtilizado() : null)
                .vlLimiteRestante(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal().subtract(cartao.getVlLimiteUtilizado()): null)
                .vlSaldo(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor() : null)
                .vlSaldoUtilizado(TipoCartaoEnum.valorComprasMesAtual(cartao))
                .vlSaldoRestante(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor(): null)
                .cartaoReferencia(cartao.getIdCartaoReferencia())
                .icAtivo(cartao.getIcAtivo())
                .compras(cartao.getCompras())
                .faturas(cartao.getFaturas())
                .build()
      ).toList().stream().sorted(Comparator.comparing(CartaoResponse::getTipoCartao).reversed()).toList();
    }

    @Override
    @Transactional
    public List<FaturaEntity> findFaturaByData(List<Date> range) {
        List<LocalDate> localDates = range.stream().map(value ->
                value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).toList();
        return this.faturaService.findFaturaGeradaByData(localDates);
    }

    @Override
    public FaturaResponseModel loadFatura() {
        var fatura = this.faturaService.findFaturaGerada().stream().filter(f-> !f.getSituacaoFatura().equals(SituacaoFaturaEnum.PAGA))
                .map(f-> FaturaResponseModel.builder()
                        .mesFatura(LocalDate.now().toString())
                        .faturas(List.of(FaturaResponse
                                .builder()
                                        .vlFatura(f.getVlFatura())
                                        .idFatura(f.getIdFatura())
                                        .situacaoFatura(f.getSituacaoFatura())
                                        .dataFaturaGerada(f.getDataFaturaGerada())
                                        .dataFaturaVencimento(f.getDataFaturaVencimento())
                                        .idCartao(f.getIdCartao())
                                .build()))
                        .build()).toList();
        return fatura.get(0);
    }


}
