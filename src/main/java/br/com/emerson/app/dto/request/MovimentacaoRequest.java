package br.com.emerson.app.dto.request;

import br.com.emerson.core.entity.CarteiraEntity;
import br.com.emerson.core.enums.MovimentacaoEnum;
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
public class MovimentacaoRequest {

    private UUID idMovimentacao;
    private Long idCarteira;
    private String descricaoMovimento;
    private MovimentacaoEnum tipoMovimentacao;
    private BigDecimal valor;
    private LocalDate dtMovimentacao;
    private CarteiraEntity carteira;
}
