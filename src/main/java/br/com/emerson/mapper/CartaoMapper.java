package br.com.emerson.mapper;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;

public class CartaoMapper {

    public static CartaoResponse toCartaoResponse(CartaoEntity cartao) {
        return CartaoResponse.builder()
                .idCartao(cartao.getIdCartao())
                .apelido(cartao.getApelido())
                .digitosFinais(cartao.getDigitosFinais())
                .diaFechamento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaFechamento() : null)
                .diaVencimento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaVencimento() : null)
                .tipoCartao(cartao.getTipoCartao())
                .vlLimiteTotal(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal() : null)
                .vlLimiteUtilizado(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteUtilizado() : null)
                .vlLimiteRestante(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal().subtract(cartao.getVlLimiteUtilizado()) : null)
                .vlSaldo(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor() : null)
                .vlSaldoUtilizado(TipoCartaoEnum.valorComprasMesAtual(cartao))
                .vlSaldoRestante(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor().subtract(TipoCartaoEnum.valorComprasMesAtual(cartao)) : null)
                .cartaoReferencia(cartao.getIdCartaoReferencia())
                .icAtivo(cartao.getIcAtivo())
                .compras(cartao.getCompras())
                .faturas(cartao.getFaturas())
                .build();
    }

    public static CartaoEntity toCartaoEntityByRequest(CartaoRequest request) {
        return request.getTipoCartao().build(request);
    }
}
