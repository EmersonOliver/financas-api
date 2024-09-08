package br.com.emerson.core.entity;

import br.com.emerson.core.enums.TipoLancamentoEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Responsavel por cadastrar as compras
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_compra")
@SequenceGenerator(name = "sq_id_compra", sequenceName = "seq_id_compra", allocationSize = 1)
public class ComprasEntity {

    @Id
    @Column(name = "id_compra")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_id_compra")
    private Long idCompra;

    @Column(name = "id_cartao")
    private UUID idCartao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tipo_lancamento")
    private TipoLancamentoEnum tipoLancamento;

    @Column(name = "qtd_parcelas")
    private Integer qtdParcelas;

    @Column(name = "vl_compra")
    private BigDecimal valorCompra;

    @Column(name = "dt_compra")
    private LocalDate dataCompra;

    @JsonIgnoreProperties("compras")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cartao", referencedColumnName = "id_cartao", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_id_compra_cartao"))
    private CartaoEntity cartao;

    @JsonIgnoreProperties("compra")
    @OneToMany(mappedBy = "compra", fetch = FetchType.EAGER)
    private List<ParcelaEntity> parcelas;

}
