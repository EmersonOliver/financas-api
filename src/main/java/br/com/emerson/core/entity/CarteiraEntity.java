package br.com.emerson.core.entity;

import br.com.emerson.core.enums.MovimentacaoEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * Responsavel por entrada e saida de dinheiro, por exemplo salario, entrada de renda variavel e diversas como poupanca, acoes e etc..
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_carteira")
@SequenceGenerator(name = "sq_id_carteira", sequenceName = "seq_id_carteira", allocationSize = 1)
public class CarteiraEntity {

    @Id
    @GeneratedValue(generator = "sq_id_carteira", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_carteira")
    private Long idCarteira;

    @Column(name = "agencia")
    private String agencia;

    @Column(name = "conta")
    private String conta;

    @Column(name = "dv")
    private String dv;

    @Column(name = "id_cartao")
    private UUID idCartao;

    @Column(name = "nome")
    private String nome;

    @Column(name = "valor")
    private BigDecimal valor;

    @OneToOne
    @JoinColumn(name = "id_cartao", referencedColumnName = "id_cartao", updatable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_carteira_cartao"))
    private CartaoEntity cartao;

    @JsonIgnoreProperties("carteira")
    @OneToMany(mappedBy = "carteira", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<MovimentacaoCarteiraEntity> movimentacoes;

}
