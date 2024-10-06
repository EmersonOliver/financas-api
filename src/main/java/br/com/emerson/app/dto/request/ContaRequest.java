package br.com.emerson.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaRequest {

    private Long idConta;
    private String origemConta;
    private String tipoConta;
    private BigDecimal valorConta;
    private LocalDate dtConta;
    private LocalDate dtVencimentoConta;
    private Boolean custoFixo;
}
