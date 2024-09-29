package br.com.emerson.core.enums;

import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.entity.ParcelaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public enum SituacaoFaturaEnum {

    PAGA {
        @Override
        public void build(List<ParcelaEntity> parcelas, FaturaEntity entity) {
            entity.setSituacaoFatura(PAGA);
            entity.setDataPagamento(LocalDate.now());
        }
    },

    VENCIDA {
        @Override
        public void build(List<ParcelaEntity> parcelas, FaturaEntity entity) {
            entity.setSituacaoFatura(VENCIDA);
        }
    },
    PENDENTE {
        @Override
        public void build(List<ParcelaEntity> parcelas, FaturaEntity entity) {
            entity.setSituacaoFatura(PENDENTE);
        }
    },
    ABERTA {
        @Override
        public void build(List<ParcelaEntity> parcelas, FaturaEntity entity) {
            BigDecimal valor = parcelas.stream()
                    .filter(parcela ->
                            parcela.getDataParcela().getMonth() == entity.getDataFaturaGerada().getMonth()
                                    && parcela.getDataParcela().getYear() == entity.getDataFaturaGerada().getYear()
                                    && !parcela.getSituacao().equals(SituacaoParcelaEnum.PAGA))
                    .map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setVlFatura(valor);
        }
    },
    FECHADA {
        @Override
        public void build(List<ParcelaEntity> parcelas, FaturaEntity entity) {
            BigDecimal valor = parcelas.stream().map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setSituacaoFatura(FECHADA);
            entity.setVlFatura(valor);
        }
    };


    public abstract void build(List<ParcelaEntity> parcelas, FaturaEntity entity);
}
