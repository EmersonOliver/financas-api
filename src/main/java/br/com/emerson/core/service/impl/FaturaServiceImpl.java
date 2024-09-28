package br.com.emerson.core.service.impl;

import br.com.emerson.core.dataprovider.FaturaRepository;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.service.FaturaService;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class FaturaServiceImpl implements FaturaService {

    @Inject
    FaturaRepository faturaRepository;

    @Override
    @Transactional
    public void salvarFatura(FaturaEntity fatura) {
        try {
            if (fatura.getIdFatura() == null)
                faturaRepository.persistAndFlush(fatura);
            else
                this.faturaRepository
                        .update("update FaturaEntity " +
                                        "set vlFatura=?1, " +
                                        "idCartao=?2," +
                                        "situacaoFatura=?3," +
                                        "dataFaturaGerada=?4," +
                                        "dataFaturaVencimento=?5" +
                                        "where idFatura =?6",
                                fatura.getVlFatura(),
                                fatura.getIdCartao(),
                                fatura.getSituacaoFatura(),
                                fatura.getDataFaturaGerada(),
                                fatura.getDataFaturaVencimento(),
                                fatura.getIdFatura());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void atualizarValorFatura(FaturaEntity fatura) {
        try {
            this.faturaRepository
                    .update("update FaturaEntity set vlFatura=?1 where idFatura =?2", fatura.getVlFatura(),
                            fatura.getIdFatura());

        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    @Override
    @Transactional
    public FaturaEntity findFaturaGeradaByData(LocalDate dataFechamento) {
        return faturaRepository.find("dataFaturaGerada between ?1 and ?2", dataFechamento.minusMonths(1), dataFechamento)
                .firstResultOptional().orElse(null);
    }

    @Override
    @Transactional
    public List<FaturaEntity> findFaturaGeradaByData(List<LocalDate> range) {
        return faturaRepository.find("dataFaturaGerada between ?1 and ?2 ", range.get(0).atTime(LocalTime.now()), range.get(1).atTime(LocalTime.now()))
                .list();
    }

    @Override
    @Transactional
    public List<FaturaEntity> findFaturaGerada() {
        return faturaRepository.findAll().list();
    }

    @Override
    @Transactional
    public List<FaturaEntity> findFaturaIdCartaoAndData(UUID idCartao, String dataInicio, String dataFim) {
        Date inicio = null;
        Date fim = null;
        try {
            inicio = new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio);
            fim = new SimpleDateFormat("yyyy-MM-dd").parse(dataFim);
            ZoneId zoneId = ZoneId.systemDefault();
            List<LocalDateTime> localDateTimes = List.of(
                    LocalDateTime.ofInstant(inicio.toInstant(), zoneId).withHour(0).withMinute(0).withSecond(0).withNano(0),
                    LocalDateTime.ofInstant(fim.toInstant(), zoneId).withHour(23).withMinute(59).withSecond(59).withNano(99));

            return faturaRepository.find("where idCartao=:idCartao and " +
                                    "dataFaturaGerada between :dataInicio and :dataFim order by dataFaturaGerada DESC",
                            Parameters.with("idCartao", idCartao).and("dataInicio", localDateTimes.get(0)).and("dataFim", localDateTimes.get(1)))
                    .list();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List<FaturaEntity> findFaturaByIdCartao(UUID idCartao) {
        return faturaRepository.find("where idCartao=:idCartao order by dataFaturaGerada DESC",
                        Parameters.with("idCartao", idCartao))
                .list();
    }

    @Override
    @Transactional
    public FaturaEntity findFaturaGeradaByDataAndCartao(UUID idCartao, LocalDate dataFechamento) {
        return faturaRepository.find("idCartao =?1 and dataFaturaGerada between ?2 and ?3",
                        idCartao,
                        dataFechamento.minusMonths(1)
                                .atTime(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                                        .toLocalTime()), dataFechamento.atTime(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
                                .toLocalTime()))
                .firstResultOptional().orElse(null);
    }
}
