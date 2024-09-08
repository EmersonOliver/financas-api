package br.com.emerson.core.enums;

import br.com.emerson.core.entity.CarteiraEntity;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public enum MovimentacaoEnum {

    ENTRADA {
        @Override
        public void build(MovimentacaoCarteiraEntity movimentacao, CarteiraEntity carteira) {
            BigDecimal valorMovimentacao = movimentacao.getValor();
            BigDecimal saldoCarteira = carteira.getValor();
            BigDecimal valor = saldoCarteira.add(valorMovimentacao);
            carteira.setValor(valor);
        }
    },
    INVESTIMENTO {
        @Override
        public void build(MovimentacaoCarteiraEntity movimentacao, CarteiraEntity carteira) {
            BigDecimal valorMovimentacao = movimentacao.getValor();
            BigDecimal saldoCarteira = carteira.getValor();
            BigDecimal valor = saldoCarteira.add(valorMovimentacao);
            carteira.setValor(valor);
        }
    },
    SAIDA {
        @Override
        public void build(MovimentacaoCarteiraEntity movimentacao, CarteiraEntity carteira) {
            BigDecimal valorMovimentacao = movimentacao.getValor();
            BigDecimal saldoCarteira = carteira.getValor();
            BigDecimal valor = saldoCarteira.subtract(valorMovimentacao);
            carteira.setValor(valor);
        }
    },
    RESGATE {
        @Override
        public void build(MovimentacaoCarteiraEntity movimentacao, CarteiraEntity carteira) {
            BigDecimal valorMovimentacao = movimentacao.getValor();
            BigDecimal saldoCarteira = carteira.getValor();
            BigDecimal valor = saldoCarteira.add(valorMovimentacao);
            carteira.setValor(valor);
            carteira.getMovimentacoes().add(MovimentacaoCarteiraEntity.builder()
                            .dtMovimentacao(LocalDate.now())
                            .tipoMovimentacao(MovimentacaoEnum.ENTRADA)
                            .descricaoMovimento("Resgate de aplicação ->" + carteira.getNome())
                            .valor(valorMovimentacao)
                    .build());
        }
    };

    public abstract void build(MovimentacaoCarteiraEntity movimentacao, CarteiraEntity carteira);
}
