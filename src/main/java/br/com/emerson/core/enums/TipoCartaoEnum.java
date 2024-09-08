package br.com.emerson.core.enums;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.ComprasEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public enum TipoCartaoEnum {

    DEBITO {
        @Override
        public CartaoEntity build(CartaoRequest cartao) {
            return CartaoEntity.builder()
                    .tipoCartao(cartao.getTipoCartao())
                    .apelido(cartao.getApelido())
                    .icAtivo(Boolean.TRUE)
                    .digitosFinais(cartao.getDigitosFinais())
                    .build();
        }
    },
    CREDITO {
        @Override
        public CartaoEntity build(CartaoRequest cartao) {
            return CartaoEntity.builder()
                    .tipoCartao(cartao.getTipoCartao())
                    .idCartaoReferencia(cartao.getTipoCartao().equals(TipoCartaoEnum.CREDITO_VIRTUAL)
                            ? cartao.getCartaoReferencia() : null)
                    .apelido(cartao.getApelido())
                    .icAtivo(Boolean.TRUE)
                    .vlLimiteTotal(cartao.getVlLimiteTotal())
                    .vlLimiteUtilizado(cartao.getVlLimiteUtilizado())
                    .diaFechamento(cartao.getDiaFechamento())
                    .digitosFinais(cartao.getDigitosFinais())
                    .diaVencimento(cartao.getDiaVencimento())
                    .build();
        }
    },
    CREDITO_VIRTUAL {
        @Override
        public CartaoEntity build(CartaoRequest cartao) {
            return CartaoEntity.builder()
                    .tipoCartao(cartao.getTipoCartao())
                    .idCartaoReferencia(cartao.getCartaoReferencia())
                    .apelido(cartao.getApelido())
                    .icAtivo(Boolean.TRUE)
                    .vlLimiteTotal(cartao.getVlLimiteTotal())
                    .vlLimiteUtilizado(cartao.getVlLimiteUtilizado())
                    .diaFechamento(cartao.getDiaFechamento())
                    .digitosFinais(cartao.getDigitosFinais())
                    .diaVencimento(cartao.getDiaVencimento())
                    .build();
        }
    };

    public abstract CartaoEntity build(CartaoRequest cartao);

    public static BigDecimal valorComprasMesAtual(CartaoEntity cartao) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1); // Primeiro dia do mês atual
        LocalDate fimMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // Último dia do mês atual
        return cartao.getCompras().stream().filter(compra -> {
                    LocalDate dataCompra = compra.getDataCompra();
                    return (dataCompra.isEqual(inicioMes) || dataCompra.isAfter(inicioMes)) &&
                            (dataCompra.isEqual(fimMes) || dataCompra.isBefore(fimMes));
                })
                .map(ComprasEntity::getValorCompra).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
