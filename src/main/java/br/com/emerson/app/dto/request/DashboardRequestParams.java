package br.com.emerson.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequestParams {

    private List<BigDecimal> valorLimites;
    private List<BigDecimal> valorUtilizados;
    private List<LocalDate> dataFechamentoFatura;
    private List<LocalDate> dataLancamentoCompras;
}
