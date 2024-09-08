package br.com.emerson.app.dto.request;

import br.com.emerson.core.enums.TipoLancamentoEnum;
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
public class ComprasRequest{
    private UUID idCartao;
    private String descricao;
    private TipoLancamentoEnum tipoLancamento;
    private Integer qtdParcelas;
    private BigDecimal valorCompra;
    private LocalDate dataCompra;
}
