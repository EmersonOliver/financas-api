package br.com.emerson.app.dto.response;

import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarteiraResponse {
    private Long idCarteira;
    private String agencia;
    private String conta;
    private String dv;
    private UUID idCartao;
    private String nome;
    private BigDecimal valor;
    private List<MovimentacaoCarteiraEntity> movimentacoes;

}
