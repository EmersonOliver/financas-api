package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.ContaRequest;
import br.com.emerson.core.dataprovider.ContasRepository;
import br.com.emerson.core.entity.ContasEntity;
import br.com.emerson.core.service.ContasService;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ContasServiceImpl implements ContasService {

    @Inject
    ContasRepository contasRepository;

    @Override
    public void cadastrarConta(ContaRequest conta) {
        var entity = ContasEntity.builder()
                .dtConta(conta.getDtConta())
                .dtVencimentoConta(conta.getDtVencimentoConta())
                .origemConta(conta.getOrigemConta())
                .valorConta(conta.getValorConta())
                .tipoConta(conta.getTipoConta())
                .custoFixo(conta.getCustoFixo())
                .build();
        this.contasRepository.persistAndFlush(entity);
    }

    @Override
    public List<ContasEntity> listarContas() {
        return contasRepository.listAll();
    }

    @Override
    public List<ContasEntity> listarContasAPagar(String date) {
        LocalDate inicio = LocalDate.parse(date).withDayOfMonth(1);
        LocalDate fimMes = LocalDate.parse(date).withDayOfMonth(LocalDate.now().lengthOfMonth());
        return this.contasRepository.find("where dtVencimentoConta between :dtInicio and :dtFim ",
                Parameters.with("dtInicio", inicio).and("dtFim",fimMes)).list();
    }

    @Override
    public BigDecimal totalContasApagar(String date) {
        LocalDate inicio = LocalDate.parse(date).withDayOfMonth(1);
        LocalDate fimMes = LocalDate.parse(date).withDayOfMonth(LocalDate.now().lengthOfMonth());
        return this.contasRepository.find("where dtVencimentoConta between :dtInicio and :dtFim ",
                Parameters.with("dtInicio", inicio).and("dtFim",fimMes)).list().stream().map(ContasEntity::getValorConta)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public List<ContasEntity> listarCustoFixo(){
        return this.contasRepository.listAll().stream().filter(ContasEntity::getCustoFixo).toList();
    }

}
