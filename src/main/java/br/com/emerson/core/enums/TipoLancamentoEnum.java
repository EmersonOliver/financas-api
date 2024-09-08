package br.com.emerson.core.enums;

import br.com.emerson.core.entity.ComprasEntity;
import br.com.emerson.core.entity.ParcelaEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public enum TipoLancamentoEnum {
    PARCELADO {
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            BigDecimal valorTotal = comprasEntity.getValorCompra();
            int qtdParcelas = comprasEntity.getQtdParcelas();
            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP);

            return IntStream.rangeClosed(1, comprasEntity.getQtdParcelas())
                    .mapToObj(numParcela -> {
                        LocalDate dataCompra = comprasEntity.getDataCompra();
                        LocalDate dataFechamento = LocalDate.of(dataCompra.getYear(), dataCompra.getMonth(),comprasEntity.getCartao().getDiaFechamento());
                        LocalDate dataParcela;
                      if(dataCompra.isBefore(dataFechamento)) {
                          dataParcela = dataCompra.plusMonths(numParcela-1);;
                      } else {
                          dataParcela = dataCompra.plusMonths(numParcela);
                      }
                        return ParcelaEntity.builder()
                                .parcela(numParcela)
                                .dataParcela(dataParcela)
                                .idCompra(comprasEntity.getIdCompra())
                                .valorParcela(valorParcela)
                                .situacao(SituacaoParcelaEnum.PENDENTE)
                                .compra(comprasEntity)
                                .build();
                    })
                    .toList();
        }
    },
    AVISTA {
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            var situacaoParcela = comprasEntity.getCartao().getTipoCartao().equals(TipoCartaoEnum.DEBITO) ?
                    SituacaoParcelaEnum.PAGA : SituacaoParcelaEnum.PENDENTE;
            return List.of(ParcelaEntity.builder()
                    .parcela(comprasEntity.getQtdParcelas())
                    .compra(comprasEntity)
                    .valorParcela(comprasEntity.getValorCompra())
                    .idCompra(comprasEntity.getIdCompra())
                            .situacao(situacaoParcela)
                            .dataParcela(comprasEntity.getDataCompra())
                    .build());
        }
    },
    FINANCIAMENTO {
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            BigDecimal valorTotal = comprasEntity.getValorCompra();
            int qtdParcelas = comprasEntity.getQtdParcelas();
            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP);
            return IntStream.rangeClosed(1, comprasEntity.getQtdParcelas())
                    .mapToObj(numParcela -> ParcelaEntity.builder()
                            .parcela(numParcela)
                            .idCompra(comprasEntity.getIdCompra())
                            .valorParcela(valorParcela)
                            .compra(comprasEntity)
                            .build())
                    .toList();
        }
    },
    EMPRESTIMO_CONSIGNADO {
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            BigDecimal valorTotal = comprasEntity.getValorCompra();
            int qtdParcelas = comprasEntity.getQtdParcelas();
            BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP);
            return IntStream.rangeClosed(1, comprasEntity.getQtdParcelas())
                    .mapToObj(numParcela -> ParcelaEntity.builder()
                            .parcela(numParcela)
                            .idCompra(comprasEntity.getIdCompra())
                            .valorParcela(valorParcela)
                            .compra(comprasEntity)
                            .build())
                    .toList();
        }
    },DESCONTO {
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            return List.of(ParcelaEntity.builder()
                    .parcela(comprasEntity.getQtdParcelas())
                    .compra(comprasEntity)
                    .valorParcela(comprasEntity.getValorCompra())
                    .idCompra(comprasEntity.getIdCompra())
                    .situacao(SituacaoParcelaEnum.PAGA)
                    .build());
        }
    },PAGAMENTO{
        @Override
        public List<ParcelaEntity> build(ComprasEntity comprasEntity) {
            return List.of();
        }
    };

    public abstract List<ParcelaEntity> build(ComprasEntity comprasEntity);


}
