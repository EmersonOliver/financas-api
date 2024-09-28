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

        this.comprasService.listarComprasByCartao(getIdCartao()).stream()
                .filter(c-> c.getValorCompra().floatValue() > 0 && c.getQtdParcelas() >= 1)
                .forEach(compras-> {
                    LocalDate currentDate = LocalDate.now();
                    if(currentDate.isAfter(getDataFechamento())){
                        setDataFechamento(getDataFechamento().plusMonths(1));
                    }
                    FaturaEntity fatura = abrirFatura(getDataFechamento());
                    log.info("Fatura carregada Situacao Fatura--> {}, Data do Fechamento--> {}", fatura.getSituacaoFatura(), getDataFechamento());
                    BigDecimal valorFatura = BigDecimal.ZERO;
                    List<ParcelaEntity> parcelas = compras.getParcelas().stream().filter(p-> !p.getSituacao().equals(SituacaoParcelaEnum.PAGA))
                            .toList();
                    for(ParcelaEntity p : parcelas) {
                        if(p.getDataParcela().isBefore(getDataFechamento()) || p.getDataParcela().isEqual(getDataFechamento())) {
                            valorFatura = valorFatura.add(p.getValorParcela());
                            continue;
                        }
                        break;
                    }
                    if(getDataFechamento().isBefore(currentDate) || getDataFechamento().isEqual(currentDate)) {
                        fatura.setSituacaoFatura(SituacaoFaturaEnum.FECHADA);
                    }
                    fatura.setVlFatura(valorFatura);
                    this.faturaService.salvarFatura(fatura);
                });


    }

    private FaturaEntity abrirFatura(LocalDate dataFechamento) {
        FaturaEntity fatura = faturaService.findFaturaGeradaByDataAndCartao(getIdCartao(), dataFechamento);
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
