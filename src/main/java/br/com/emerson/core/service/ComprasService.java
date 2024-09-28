package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.ComprasRequest;
import br.com.emerson.core.entity.ComprasEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ComprasService {

    void salvar(ComprasRequest request);
    List<ComprasEntity> listarComprasByCartao(UUID idCartao);


}
