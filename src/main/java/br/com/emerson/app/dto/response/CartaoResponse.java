package br.com.emerson.app.dto.response;

import br.com.emerson.core.entity.ComprasEntity;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoResponse {
    private UUID idCartao;
    private Long idCarteira;
    private String apelido;
    private String digitosFinais;
    private Integer diaFechamento;
    private Integer diaVencimento;
    private TipoCartaoEnum tipoCartao;
    private BigDecimal vlLimiteTotal;
    private BigDecimal vlLimiteUtilizado;
    private BigDecimal vlLimiteRestante;
    private BigDecimal vlSaldo;
    private BigDecimal vlSaldoRestante;
    private BigDecimal vlSaldoUtilizado;
    private UUID cartaoReferencia;
    private Boolean icAtivo;
    private List<ComprasEntity> compras;
    private List<FaturaEntity> faturas;
}
