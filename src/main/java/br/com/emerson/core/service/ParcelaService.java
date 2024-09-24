package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.ParcelaRequest;
import br.com.emerson.core.entity.ParcelaEntity;
import br.com.emerson.core.enums.SituacaoParcelaEnum;

import java.time.LocalDate;
import java.util.List;

public interface ParcelaService {

    void salvar(ParcelaEntity parcela);

    void salvar(ParcelaRequest parcela);

    void atualizarSituacaoParcela(Long idParcela, SituacaoParcelaEnum situacao);

    void atualizar(Long id, ParcelaRequest request);

    void atualizar(Long id, ParcelaEntity parcela);

    List<ParcelaEntity> listarParcelasByDataCompraAndSituacao(SituacaoParcelaEnum situacao, LocalDate dataCompra);
    List<ParcelaEntity> listarParcelasByIdCompra(Long idCompra);
}
