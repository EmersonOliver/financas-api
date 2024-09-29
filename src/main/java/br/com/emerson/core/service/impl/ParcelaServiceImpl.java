package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.ParcelaRequest;
import br.com.emerson.core.dataprovider.ParcelaRepository;
import br.com.emerson.core.entity.ParcelaEntity;
import br.com.emerson.core.enums.SituacaoParcelaEnum;
import br.com.emerson.core.service.ParcelaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ParcelaServiceImpl implements ParcelaService {

    @Inject
    ParcelaRepository parcelaRepository;

    @Override
    @Transactional
    public void salvar(ParcelaEntity parcela) {
        log.info("Persistindo parcela....");
        parcelaRepository.persistAndFlush(parcela);
    }

    @Override
    @Transactional
    public void salvar(ParcelaRequest parcela) {
        var entity = ParcelaEntity.builder()
                .idCompra(parcela.getIdCompra())
                .situacao(parcela.getSituacao())
                .valorParcela(parcela.getValorParcela())
                .parcela(parcela.getParcela())
                .build();
        parcelaRepository.persistAndFlush(entity);
    }

    @Override
    @Transactional
    public void atualizarSituacaoParcela(Long idParcela, SituacaoParcelaEnum situacao) {
        var entity = this.parcelaRepository.findById(idParcela);
        entity.setSituacao(situacao);
        this.parcelaRepository.persistAndFlush(entity);
    }

    @Override
    @Transactional
    public void atualizar(Long id, ParcelaRequest request) {

    }

    @Override
    @Transactional
    public void atualizar(Long id, ParcelaEntity parcela) {
        try {
            this.parcelaRepository.update("UPDATE ParcelaEntity set idFatura=?1 where idParcela =?2",
                    parcela.getIdFatura(), parcela.getIdParcela());
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<ParcelaEntity> listarParcelasByDataCompraAndSituacao(SituacaoParcelaEnum situacao, LocalDate dataCompra) {
        return List.of();
    }

    @Override
    public List<ParcelaEntity> listarParcelasByIdCompra(Long idCompra) {
        return parcelaRepository.find("where idCompra =?1", idCompra).list();
    }
}
