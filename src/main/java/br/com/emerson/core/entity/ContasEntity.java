package br.com.emerson.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_contas")
@SequenceGenerator(name = "sq_id_conta", sequenceName = "seq_id_conta", allocationSize = 1)
public class ContasEntity {

    @Id
    @Column(name = "id_conta")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_id_conta")
    private Long idConta;

    @Column(name = "origem_conta")
    private String origemConta;

    @Column(name = "tp_conta")
    private String tipoConta;

    @Column(name = "vl_conta")
    private BigDecimal valorConta;

    @Column(name = "dt_conta")
    private LocalDate dtConta;

    @Column(name = "dt_vencimento_conta")
    private LocalDate dtVencimentoConta;



}
