package br.com.emerson.app.dto.request;

import br.com.emerson.core.enums.SituacaoParcelaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaRequest{
    private Long idCompra;
    private BigDecimal valorParcela;
    private Integer parcela;
    private SituacaoParcelaEnum situacao;
}
