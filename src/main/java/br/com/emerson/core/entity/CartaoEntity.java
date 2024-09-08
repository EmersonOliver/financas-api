package br.com.emerson.core.entity;

import br.com.emerson.core.enums.TipoCartaoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Responsavel por cadastrar os cartoes tipo de credito ou debito
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cartoes")
public class CartaoEntity {

    @Id
    @Column(name = "id_cartao")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCartao;

    @Column(name = "id_cartao_referencia")
    private UUID idCartaoReferencia;

    @Column(name = "id_carteira")
    private Long idCarteira;

    @Column(name = "no_apelido")
    private String apelido;

    @Column(name = "four_last_digits")
    private String digitosFinais;

    @Column( name = "dia_fechamento")
    private Integer diaFechamento;

    @Column(name = "dia_vencimento")
    private Integer diaVencimento;

    @Column(name = "tipo_cartao")
    private TipoCartaoEnum tipoCartao;

    @Column(name = "vl_limite_total")
    private BigDecimal vlLimiteTotal;

    @Column(name = "vl_limite_utilizado")
    private BigDecimal vlLimiteUtilizado;

    @Column(name = "ic_ativo")
    private Boolean icAtivo;

    @JsonIgnoreProperties("cartao")
    @OneToMany(mappedBy = "cartao", fetch = FetchType.EAGER)
    private List<ComprasEntity> compras;

    @OneToMany(mappedBy = "cartao", fetch = FetchType.EAGER)
    private List<FaturaEntity> faturas;

    @JsonIgnoreProperties("cartao")
    @JoinColumn(name = "id_carteira", referencedColumnName = "id_carteira", insertable = false, updatable = false)
    @OneToOne(mappedBy = "cartao")
    private CarteiraEntity carteira;

}
