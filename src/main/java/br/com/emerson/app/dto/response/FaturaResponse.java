package br.com.emerson.app.dto.response;

import br.com.emerson.core.enums.SituacaoFaturaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturaResponse {

    private Long idFatura;
    private UUID idCartao;
    private BigDecimal vlFatura;
    private SituacaoFaturaEnum situacaoFatura;
    private LocalDateTime dataFaturaGerada;
    private LocalDate dataFaturaVencimento;
    private CartaoResponse cartao;
}
