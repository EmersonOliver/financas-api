package br.com.emerson.app.dto.response;

import br.com.emerson.core.entity.CartaoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContasFaturasPagarResponse {

    private BigDecimal totalContas;
    private BigDecimal totalFaturas;
    private BigDecimal valorTotal;
    private List<CartaoEntity> faturaCartoes;
}
