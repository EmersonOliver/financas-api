package br.com.emerson.core.entity;


import br.com.emerson.core.enums.SituacaoFaturaEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Responsavel por registrar a fatura no fechamento
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_fatura")
@SequenceGenerator(name = "sq_id_fatura", sequenceName = "seq_id_fatura", allocationSize = 1)
public class FaturaEntity {

    @Id
    @Column(name = "id_fatura", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_id_fatura")
    private Long idFatura;

    @Column(name = "id_cartao", nullable = false)
    private UUID idCartao;

    @Column(name = "vl_fatura")
    private BigDecimal vlFatura;

    @Column(name = "situacao_fatura")
    private SituacaoFaturaEnum situacaoFatura;

    @Column(name = "data_fatura_gerada")
    private LocalDateTime dataFaturaGerada;

    @Column(name = "data_fatura_vencimento")
    private LocalDate dataFaturaVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("faturas")
    @JoinColumn(name = "id_cartao", referencedColumnName = "id_cartao", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "fk_id_cartao_fatura"))
    private CartaoEntity cartao;

}
