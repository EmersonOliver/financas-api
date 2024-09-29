package br.com.emerson.app.entrypoint.cron.commons;

import br.com.emerson.core.entity.FaturaEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Slf4j
public class JobCommons {

    private UUID idCartao;
    private int dia;
    private int diaVencimento;
    private LocalDate dataFechamento;
    private String cartao;
    private String digitosFinais;

    public void imprimeLog() {
        log.info("Criando job =[" + cartao + "]");
        log.info("Data Fechamento =[" + dataFechamento.toString() + "]");
    }

}
