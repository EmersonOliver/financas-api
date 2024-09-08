package br.com.emerson.app.dto.request;

import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturaRequest {
    private UUID idCartao;
    private BigDecimal vlFatura;
    private SituacaoFaturaEnum situacaoFatura;
    private Date dataFaturaGerada;
    private LocalDate dataFaturaVencimento;
}
