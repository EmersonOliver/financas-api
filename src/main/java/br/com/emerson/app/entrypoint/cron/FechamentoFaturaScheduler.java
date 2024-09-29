package br.com.emerson.app.entrypoint.cron;

import br.com.emerson.app.entrypoint.cron.commons.JobCommons;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.service.FaturaService;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Dependent
public class FechamentoFaturaScheduler extends JobCommons implements Runnable {

    @Inject
    FaturaService faturaService;

    @Override
    public void run() {
        log.info("Executando JOB Fatura-->" + getIdCartao());
        LocalDate currentDate = LocalDate.now();
        //Data de fechamento inicia com o mês atual da inicializacao da aplicacao
        if (currentDate.isAfter(getDataFechamento())) {
            //Gerando uma fatura em caso se ja fechou
            var novaFatura = new FaturaEntity();
            novaFatura.setVlFatura(BigDecimal.ZERO);
            novaFatura.setSituacaoFatura(SituacaoFaturaEnum.FECHADA);
            novaFatura.setDataFaturaGerada(getDataFechamento().atTime(LocalTime.now()));
            novaFatura.setDataFaturaVencimento(LocalDate.of(getDataFechamento().getYear(),
                    getDataFechamento().getMonth(), getDiaVencimento()));
            novaFatura.setIdCartao(getIdCartao());
            this.faturaService.salvarFatura(novaFatura);

            //Avanca para a proxima data de fechamento
            setDataFechamento(getDataFechamento().plusMonths(1));
        }
        //Abrindo uma nova fatura caso nao houver uma fatura mais recente
        FaturaEntity faturaAberta = abrirFatura(getDataFechamento());
        log.info("Fatura carregada Situacao Fatura--> {}, Data do Fechamento--> {}", faturaAberta.getSituacaoFatura(), getDataFechamento());
        if (faturaAberta.getIdFatura() == null) {
            this.faturaService.salvarFatura(faturaAberta);
        }
    }

    private FaturaEntity abrirFatura(LocalDate dataFechamento) {
        var fatura = faturaService.findFaturaGeradaByDataAndCartao(getIdCartao(), dataFechamento);
        return fatura == null ? FaturaEntity.builder()
                .vlFatura(BigDecimal.ZERO)
                .situacaoFatura(SituacaoFaturaEnum.ABERTA)
                .dataFaturaGerada(dataFechamento.atTime(LocalTime.now()))
                .dataFaturaVencimento(
                        LocalDate.of(dataFechamento.getYear(), dataFechamento.getMonth(), getDiaVencimento()))
                .idCartao(getIdCartao())
                .build() : fatura;
    }

}
