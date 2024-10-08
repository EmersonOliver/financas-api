package br.com.emerson.core.service.impl;

import br.com.emerson.app.dto.request.CarteiraRequest;
import br.com.emerson.app.dto.response.CarteiraResponse;
import br.com.emerson.app.dto.response.SimulacaoCarteiraResponse;
import br.com.emerson.core.dataprovider.CarteiraRepository;
import br.com.emerson.core.entity.CarteiraEntity;
import br.com.emerson.core.entity.FaturaEntity;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;
import br.com.emerson.core.enums.MovimentacaoEnum;
import br.com.emerson.core.enums.SituacaoFaturaEnum;
import br.com.emerson.core.service.CarteiraService;
import br.com.emerson.core.service.FaturaService;
import br.com.emerson.core.service.MovimentacaoService;
import br.com.emerson.domain.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class CarteiraServiceImpl implements CarteiraService {

    @Inject
    CarteiraRepository carteiraRepository;

    @Inject
    MovimentacaoService movimentacaoService;

    @Inject
    FaturaService faturaService;

    @Override
    @Transactional
    public void salvar(CarteiraRequest request) {
        log.info("Persistindo carteira....");
        var entity = CarteiraEntity.builder()
                .nome(request.getNome())
                .agencia(request.getAgencia())
                .conta(request.getConta())
                .dv(request.getDv())
                .idCartao(request.getIdCartao())
                .valor(request.getValor())
                .build();
        carteiraRepository.persistAndFlush(entity);

        var movimentacao = MovimentacaoCarteiraEntity.builder()
                .dtMovimentacao(LocalDate.now())
                .carteira(entity)
                .idCarteira(entity.getIdCarteira())
                .descricaoMovimento("Nova Carteira Adicionada")
                .valor(request.getValor())
                .tipoMovimentacao(MovimentacaoEnum.ENTRADA)
                .build();
        movimentacaoService.salvarMovimentacao(movimentacao);

    }

    @Override
    @Transactional
    public void salvar(CarteiraEntity entity) {
        this.carteiraRepository.persistAndFlush(entity);
    }

    @Override
    @Transactional
    public void atualizar(Long id, CarteiraRequest request) {
        log.info("Atualizando carteira....");
        carteiraRepository.findByIdOptional(id).ifPresentOrElse(value -> {
                    value.setIdCartao(request.getIdCartao());
                    value.setDv(request.getDv());
                    value.setAgencia(request.getAgencia());
                    value.setConta(request.getConta());
                    value.setNome(request.getNome());
                    value.setValor(request.getValor());
                    carteiraRepository.persistAndFlush(value);
                }, () -> {
                    throw new BusinessException("Ocorreu uma falha na atualizacao");
                }
        );
    }

    @Override
    public List<CarteiraResponse> listarTodos() {
        log.info("Listando todas as carteiras...");
        return carteiraRepository.findAll().list().stream().map(c ->
                        CarteiraResponse.builder().idCarteira(c.getIdCarteira())
                                .idCartao(c.getIdCartao())
                                .agencia(c.getAgencia())
                                .conta(c.getConta())
                                .dv(c.getDv())
                                .valor(c.getValor())
                                .nome(c.getNome()).build())
                .toList();
    }

    @Override
    public Optional<CarteiraEntity> findCarteiraById(Long idCarteira) {
        return carteiraRepository.findByIdOptional(idCarteira);
    }

    @Override
    public CarteiraEntity findCarteiraByIdCartao(UUID idCartao) {
        return carteiraRepository.find("where idCartao = ?1", idCartao).singleResult();
    }

    @Override
    @Transactional
    public List<SimulacaoCarteiraResponse> simulacaoCarteiraResponse(Long id) {
        CarteiraEntity carteira = this.carteiraRepository.findById(id);
        BigDecimal saldoCarteira = carteira.getValor();
        List<FaturaEntity> listFaturas = faturaService.findFaturaGerada();
        List<SimulacaoCarteiraResponse> result = new ArrayList<>();
        for (FaturaEntity fatura : listFaturas) {
            saldoCarteira =  saldoCarteira.subtract(fatura.getVlFatura());
            SimulacaoCarteiraResponse sim = SimulacaoCarteiraResponse.builder()
                    .saldoFuturo(saldoCarteira)
                    .data(fatura.getDataFaturaGerada().toLocalDate())
                    .build();
            result.add(sim);
        }
        return result;
    }

    @Override
    public BigDecimal saldoValor(Long idCarteira) {
        var movimentacao =  movimentacaoService.listarMovimentacaoByIdCarteira(idCarteira);
        BigDecimal valorEntrada =
                movimentacao.stream().filter(entrada-> !entrada.getTipoMovimentacao().equals(MovimentacaoEnum.SAIDA))
                        .map(MovimentacaoCarteiraEntity::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorSaida =  movimentacao.stream().filter(entrada-> entrada.getTipoMovimentacao().equals(MovimentacaoEnum.SAIDA))
                .map(MovimentacaoCarteiraEntity::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        return valorEntrada.subtract(valorSaida);
    }
}
