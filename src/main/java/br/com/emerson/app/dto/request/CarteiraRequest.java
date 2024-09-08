package br.com.emerson.app.dto.request;

import br.com.emerson.core.enums.MovimentacaoEnum;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarteiraRequest {
    private Long idCarteira;
    private String agencia;
    private String conta;
    private String dv;
    private UUID idCartao;
    private String nome;
    private BigDecimal valor;
}
