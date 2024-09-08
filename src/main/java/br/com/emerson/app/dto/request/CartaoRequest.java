package br.com.emerson.app.dto.request;

import br.com.emerson.core.enums.TipoCartaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoRequest{

    private Long idCarteira;
    private UUID cartaoReferencia;
    private String apelido;
    private String digitosFinais;
    private Integer diaFechamento;
    private Integer diaVencimento;
    private TipoCartaoEnum tipoCartao;
    private BigDecimal vlLimiteTotal;
    private BigDecimal vlLimiteUtilizado;
}
