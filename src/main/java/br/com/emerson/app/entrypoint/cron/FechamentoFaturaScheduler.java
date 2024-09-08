package br.com.emerson.app.entrypoint.cron;

import br.com.emerson.app.entrypoint.cron.commons.JobCommons;
import br.com.emerson.core.entity.ComprasEntity;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.entity.ParcelaEntity;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.enums.SituacaoParcelaEnum;
import br.com.emerson.core.service.ComprasService;
import br.com.emerson.core.service.FaturaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class FechamentoFaturaScheduler extends JobCommons implements Runnable {


    @Inject
    FaturaService faturaService;

    @Inject
    ComprasService comprasService;

    @Override
    public void run() {
        FaturaEntity fatura = faturaService.findFaturaGeradaByDataAndCartao(getIdCartao(), getDataFechamento());
        if (fatura == null) {
            fatura = new FaturaEntity();
            List<ComprasEntity> compras =
                    comprasService.listarComprasByDataAndCartao(getIdCartao(),
                            getDataFechamento().minusMonths(1),
                            getDataFechamento());
            if (compras.isEmpty()) {
                fatura.setVlFatura(BigDecimal.ZERO);
                fatura.setSituacaoFatura(SituacaoFaturaEnum.PAGA);
                fatura.setDataFaturaGerada(getDataFechamento().atTime(LocalTime.now()));
                fatura.setDataFaturaVencimento(LocalDate.of(getDataFechamento().getYear(), getDataFechamento().getMonth(), getDiaVencimento()));
                fatura.setIdCartao(getIdCartao());
                faturaService.salvarFatura(fatura);
            } else {
                BigDecimal valorFatura = BigDecimal.ZERO;
                for (ComprasEntity compra : compras) {
                    for (ParcelaEntity parcela : compra.getParcelas()) {
                        if (!parcela.getSituacao().equals(SituacaoParcelaEnum.PAGA)) {
                            valorFatura = valorFatura.add(parcela.getValorParcela());
                        }
                    }
                }
                fatura.setVlFatura(valorFatura);
                fatura.setSituacaoFatura(SituacaoFaturaEnum.PENDENTE);

            }
        } else {
            log.info("Ja contem faturas neste periodo para o cartao -->" + fatura.getCartao().getApelido());
        }

    }
}
