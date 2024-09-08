package br.com.emerson.app.dto.response;

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
public class ParcelaResponse {
    private Long idParcela;
    private Long idCompra;
    private BigDecimal valorParcela;
    private Integer parcela;
    private SituacaoParcelaEnum situacao;
    private ComprasResponse compra;
}
