package br.com.emerson.core.entity;


import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.enums.SituacaoParcelaEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Responsavel por cadastras as parcelas quando uma compra for parcelada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_parcelas")
@SequenceGenerator(name = "sq_id_parcela", sequenceName = "seq_id_parcela", allocationSize = 1)
public class ParcelaEntity {

    @Id
    @GeneratedValue(generator = "sq_id_parcela", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_parcela")
    private Long idParcela;

    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "id_fatura")
    private Long idFatura;

    @Column(name = "vl_parcela")
    private BigDecimal valorParcela;

    @Column(name = "numero_parcela")
    private Integer parcela;

    @Column(name = "dt_parcela")
    private LocalDate dataParcela;

    @Column(name = "situacao_parcela")
    private SituacaoParcelaEnum situacao;

    @JsonIgnoreProperties("parcelas")
    @ManyToOne
    @JoinColumn(name = "id_compra", referencedColumnName = "id_compra", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_id_parcela_compra"))
    private ComprasEntity compra;

    @JsonIgnoreProperties("cartao")
    @ManyToOne
    @JoinColumn(name = "id_fatura", referencedColumnName = "id_fatura", insertable = false, updatable = false)
    private FaturaEntity fatura;
}
