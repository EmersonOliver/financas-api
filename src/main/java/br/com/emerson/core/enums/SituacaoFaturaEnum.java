package br.com.emerson.core.enums;

import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.entity.ParcelaEntity;

import java.math.BigDecimal;
import java.util.List;

public enum SituacaoFaturaEnum {

    PAGA {
        @Override
        public void build(List<ParcelaEntity> compras, FaturaEntity entity) {
            BigDecimal valorFatura = compras.stream()
                    .map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setVlFatura(valorFatura);
        }
    },

    VENCIDA {
        @Override
        public void build(List<ParcelaEntity> compras, FaturaEntity entity) {
            BigDecimal valorFatura = compras.stream()
                    .map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setVlFatura(valorFatura);
        }
    },
    PENDENTE {
        @Override
        public void build(List<ParcelaEntity> compras, FaturaEntity entity) {
            BigDecimal valorFatura = compras.stream()
                    .map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setVlFatura(valorFatura);
        }
    },
    ABERTA {
        @Override
        public void build(List<ParcelaEntity> compras, FaturaEntity entity) {
            BigDecimal valorFatura = compras.stream()
                    .map(ParcelaEntity::getValorParcela)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            entity.setVlFatura(valorFatura);
        }
    },
    FECHADA {
        @Override
        public void build(List<ParcelaEntity> compras, FaturaEntity entity) {

        }
    };


    public abstract void build(List<ParcelaEntity> compras, FaturaEntity entity);
}
