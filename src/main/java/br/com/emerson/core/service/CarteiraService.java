package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.CarteiraRequest;
import br.com.emerson.app.dto.response.CarteiraResponse;
import br.com.emerson.core.entity.CarteiraEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarteiraService {
    void salvar(CarteiraRequest request);

    void salvar(CarteiraEntity entity);

    void atualizar(Long id, CarteiraRequest request);

    List<CarteiraResponse> listarTodos();

    Optional<CarteiraEntity> findCarteiraById(Long idCarteira);

    CarteiraEntity findCarteiraByIdCartao(UUID idCartao);
}
