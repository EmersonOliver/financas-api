package br.com.emerson.core.service;

import br.com.emerson.core.entity.FaturaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FaturaService {

    void salvarFatura(FaturaEntity fatura);
    FaturaEntity findFaturaGeradaByData(LocalDate dataFechamento);
    FaturaEntity findFaturaGeradaByDataAndCartao(UUID idCartao, LocalDate dataFechamento);
    List<FaturaEntity> findFaturaGeradaByData(List<LocalDate> range);
    List<FaturaEntity> findFaturaGerada();
    List<FaturaEntity> findFaturaIdCartaoAndData(UUID idCartao, String dataInicio, String dataFim);
    List<FaturaEntity> findFaturaByIdCartao(UUID idCartao);
}
