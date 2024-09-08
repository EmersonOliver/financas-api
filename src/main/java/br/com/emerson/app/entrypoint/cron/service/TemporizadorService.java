package br.com.emerson.app.entrypoint.cron.service;

import br.com.emerson.app.entrypoint.cron.FechamentoFaturaScheduler;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    public static final int JOB_VALIDACAO_FATURA = 1;

    ScheduledExecutorService executorService;


    @PostConstruct
    public void init() {
        executorService = new ScheduledThreadPoolExecutor(5);
        mapJobs = new HashMap<>();
        cartaoService.listarCartoes().stream().filter(card-> !card.getTipoCartao().equals(TipoCartaoEnum.DEBITO)).forEach(cartao -> {
            arrayJobs =  new ScheduledFuture<?>[1];
            arrayJobs[JOB_FECHAMENTO_FATURA] = fechamentoFaturaJob(executorService, cartao);
        });
    }

    private ScheduledFuture<?> fechamentoFaturaJob(ScheduledExecutorService executor, CartaoEntity cartao) {

        ScheduledFuture<?> result;
        FechamentoFaturaScheduler jFechamentoFatura = fechamentoFaturaScheduler.get();
        LocalDate hoje = LocalDate.now();
        LocalDate dataFechamento = LocalDate.of(hoje.getYear(), hoje.getMonth(), cartao.getDiaFechamento());
        if (dataFechamento.isBefore(hoje)) {
            dataFechamento = dataFechamento.plusMonths(1);
        }
        int targetHour = 8;
        int targetMinute = 0;
        jFechamentoFatura.setDataFechamento(dataFechamento);
        jFechamentoFatura.setCartao(cartao.getApelido());
        jFechamentoFatura.setDia(cartao.getDiaFechamento());
        jFechamentoFatura.setDigitosFinais(cartao.getDigitosFinais());
        jFechamentoFatura.setIdCartao(cartao.getIdCartao());
        jFechamentoFatura.setDiaVencimento(cartao.getDiaVencimento());

        long initialDelay = calcularDelay(targetHour, targetMinute);
        result = executor.scheduleAtFixedRate(jFechamentoFatura, initialDelay, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
        jFechamentoFatura.imprimeLog();
        return result;
    }

    private long calcularDelay(int targetHour, int targetMinute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(targetHour).withMinute(targetMinute).withSecond(0).withNano(0);
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }
        return ChronoUnit.MINUTES.between(now, nextRun);
    }


}
