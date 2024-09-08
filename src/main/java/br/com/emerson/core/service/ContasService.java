package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.ContaRequest;
import br.com.emerson.core.entity.ContasEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ContasService {

    void cadastrarConta(ContaRequest conta);
    List<ContasEntity> listarContas();
    List<ContasEntity> listarContasAPagar(String date);
    BigDecimal totalContasApagar(String date);
}
