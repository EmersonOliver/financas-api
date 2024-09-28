package br.com.emerson.app.entrypoint.cron.service;

import br.com.emerson.app.entrypoint.cron.FechamentoFaturaScheduler;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Singleton
@Startup
@Slf4j
public class TemporizadorService {

    private Map<String, ScheduledFuture<?>[]> mapJobs;
    private ScheduledFuture<?>[] arrayJobs;

    @Inject
    CartaoService cartaoService;

    @Inject
    Instance<FechamentoFaturaScheduler> fechamentoFaturaScheduler;

    public static final int JOB_FECHAMENTO_FATURA = 0;

    ScheduledExecutorService executorService;


    @PostConstruct
    public void init() {
        executorService = Executors.newScheduledThreadPool(5);
        mapJobs = new HashMap<>();
        cartaoService.listarCartoes().stream().filter(card -> !card.getTipoCartao().equals(TipoCartaoEnum.DEBITO))
                .forEach(cartao -> {
                    arrayJobs = new ScheduledFuture<?>[1];
                    arrayJobs[JOB_FECHAMENTO_FATURA] = fechamentoFaturaJob(executorService, cartao);
                    mapJobs.put("FECHAMENTO", arrayJobs);
                });
    }

    @PreDestroy
    public void stop() {
        executorService.shutdown();
    }

    private ScheduledFuture<?> fechamentoFaturaJob(ScheduledExecutorService executor, CartaoEntity cartao) {

        ScheduledFuture<?> result;
        FechamentoFaturaScheduler jFechamentoFatura = fechamentoFaturaScheduler.get();

        LocalDate hoje = LocalDate.now();
        LocalDate dataFechamento = LocalDate.of(hoje.getYear(), hoje.getMonth(), cartao.getDiaFechamento());

        jFechamentoFatura.setDataFechamento(dataFechamento);
        jFechamentoFatura.setCartao(cartao.getApelido());
        jFechamentoFatura.setDia(cartao.getDiaFechamento());
        jFechamentoFatura.setDigitosFinais(cartao.getDigitosFinais());
        jFechamentoFatura.setIdCartao(cartao.getIdCartao());
        jFechamentoFatura.setDiaVencimento(cartao.getDiaVencimento());
        jFechamentoFatura.imprimeLog();

        result = executor.scheduleAtFixedRate(jFechamentoFatura, 0, 1, TimeUnit.MINUTES);
        return result;
    }

    public void reagendar() {
        log.info("Reagendando tarefas...");
        try {
            ScheduledFuture<?>[] jobs = mapJobs.get("FECHAMENTO");

            if (jobs != null && jobs[JOB_FECHAMENTO_FATURA] != null) {
                jobs[JOB_FECHAMENTO_FATURA].cancel(false);
                mapJobs = new HashMap<>();
                cartaoService.listarCartoes().stream().filter(card -> !card.getTipoCartao().equals(TipoCartaoEnum.DEBITO))
                        .forEach(cartao -> {
                            arrayJobs = new ScheduledFuture<?>[1];
                            arrayJobs[JOB_FECHAMENTO_FATURA] = fechamentoFaturaJob(executorService, cartao);
                            mapJobs.put("FECHAMENTO", arrayJobs);
                        });
            }else{
                this.init();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }

    }


}
