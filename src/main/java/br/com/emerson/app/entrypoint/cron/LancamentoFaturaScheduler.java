package br.com.emerson.app.entrypoint.cron;

import br.com.emerson.app.entrypoint.cron.commons.JobCommons;
import br.com.emerson.core.service.ComprasService;
import br.com.emerson.core.service.FaturaService;
import br.com.emerson.core.service.ParcelaService;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Dependent
public class LancamentoFaturaScheduler extends JobCommons implements Runnable {

    @Inject
    FaturaService faturaService;

    @Inject
    ComprasService comprasService;

    @Inject
    ParcelaService parcelaService;


    @Override
    public void run() {
        var faturaCarregada = this.faturaService.findFaturaGeradaByDataAndCartao(getIdCartao(), getDataFechamento());
        log.info("Fatura carregada ---> {}, Data Fechamento=> {}, Cartao=> {}",
                faturaCarregada.getSituacaoFatura().name(),
                faturaCarregada.getDataFaturaGerada().toLocalDate(), faturaCarregada.getCartao().getApelido());
        comprasService.listarComprasByCartao(getIdCartao()).stream()
                .filter(compra -> compra.getQtdParcelas() >= 1 && compra.getValorCompra().floatValue() > 0)
                .forEach(compra -> {
                   faturaCarregada.getSituacaoFatura().build(compra.getParcelas(), faturaCarregada);
                    faturaService.atualizarValorFatura(faturaCarregada);
                });

    }
}
