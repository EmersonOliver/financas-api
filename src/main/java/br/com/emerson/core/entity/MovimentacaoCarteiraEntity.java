package br.com.emerson.core.entity;

import br.com.emerson.core.enums.MovimentacaoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_movimentacao_carteira")
public class MovimentacaoCarteiraEntity {

    @Id
    @Column(name = "movement_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idMovimentacao;

    @Column(name = "id_carteira")
    private Long idCarteira;

    @Column(name = "descricao_movimento")
    private String descricaoMovimento;

    @Column(name = "tipo_movimentacao")
    private MovimentacaoEnum tipoMovimentacao;

    @Column(name = "vl_movimentacao")
    private BigDecimal valor;

    @Column(name = "dt_movimentacao")
    private LocalDate dtMovimentacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_carteira", referencedColumnName = "id_carteira", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_carteira_movimentacao"))
    private CarteiraEntity carteira;

}
