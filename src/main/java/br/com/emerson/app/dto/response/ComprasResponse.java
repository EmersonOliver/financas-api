package br.com.emerson.app.dto.response;

import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.ParcelaEntity;
import br.com.emerson.core.enums.TipoLancamentoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComprasResponse {

    private Long idCompra;
    private UUID idCartao;
    private String descricao;
    private TipoLancamentoEnum tipoLancamento;
    private Integer qtdParcelas;
    private BigDecimal valorCompra;
    private LocalDate dataCompra;
    private CartaoEntity cartao;
    private List<ParcelaEntity> parcelas;
}
