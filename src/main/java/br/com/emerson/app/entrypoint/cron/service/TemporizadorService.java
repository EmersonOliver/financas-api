package br.com.emerson.app.entrypoint.cron.service;

import br.com.emerson.app.entrypoint.cron.FechamentoFaturaScheduler;
import br.com.emerson.app.entrypoint.cron.LancamentoFaturaScheduler;
import br.com.emerson.app.entrypoint.cron.commons.JobCommons;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
@Slf4j
public class TemporizadorService {

    private final Map<String, ScheduledFuture<?>> jobMap = new HashMap<>();

    @Inject
    CartaoService cartaoService;

    @Inject
    Instance<FechamentoFaturaScheduler> fechamentoFaturaSchedulers;

    @Inject
    Instance<LancamentoFaturaScheduler> lancamentoFaturaSchedulers;

    ScheduledExecutorService executorService;

    @PostConstruct
    public void init() {
        executorService = Executors.newScheduledThreadPool(5);
        try {
            log.info("Agendamento de job cartao de credito");
            cartaoService.listarCartoes().stream()
                    .filter(cartao -> !cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO))
                    .forEach(cartao->{
                       LocalDate fechamento = jobFechamentoFaturaJob(cartao);
                        jobLancamentoFaturaJob(cartao, fechamento);
                    });
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @PreDestroy
    public void stop() {
        executorService.shutdown();
        jobMap.values().forEach(future -> future.cancel(true));
    }

    private void jobLancamentoFaturaJob(CartaoEntity cartao, LocalDate fechamento) {
        LancamentoFaturaScheduler jLancamento = lancamentoFaturaSchedulers.get();
        buildJobCommons(jLancamento, cartao, fechamento);
        ScheduledFuture<?> jobLancamento = executorService.scheduleAtFixedRate(jLancamento, 0, 2, TimeUnit.SECONDS);
        jobMap.put(cartao.getApelido()+"_LANCAMENTO", jobLancamento);
    }

    private LocalDate jobFechamentoFaturaJob(CartaoEntity cartao) {
        FechamentoFaturaScheduler jFechamentoFatura = fechamentoFaturaSchedulers.get();
        LocalDate hoje = LocalDate.now();
        LocalDate dataFechamento = cartao.getFaturas().isEmpty() ? LocalDate.of(hoje.getYear(), hoje.getMonth(), cartao.getDiaFechamento())
                : cartao.getFaturas().stream()
                .map(FaturaEntity::getDataFaturaGerada)
                .map(LocalDateTime::toLocalDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.of(hoje.getYear(),
                        hoje.getMonth(), cartao.getDiaFechamento())); // valor padrão
        buildJobCommons(jFechamentoFatura, cartao, dataFechamento);
        ScheduledFuture<?> jobFechamento = executorService.scheduleAtFixedRate(jFechamentoFatura, 0, 10, TimeUnit.SECONDS);
        jobMap.put(cartao.getApelido()+"_FECHAMENTO", jobFechamento);
        return dataFechamento;
    }

    public void cadastrarNovoJobCartao(CartaoEntity cartao) {
        if (!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO)) {
            jobFechamentoFaturaJob(cartao);
        }
    }

    private void buildJobCommons(JobCommons jobCommons, CartaoEntity cartao, LocalDate dataFechamento) {
        jobCommons.setDataFechamento(dataFechamento);
        jobCommons.setCartao(cartao.getApelido());
        jobCommons.setDia(cartao.getDiaFechamento());
        jobCommons.setDigitosFinais(cartao.getDigitosFinais());
        jobCommons.setIdCartao(cartao.getIdCartao());
        jobCommons.setDiaVencimento(cartao.getDiaVencimento());
        jobCommons.imprimeLog();
    }
}
