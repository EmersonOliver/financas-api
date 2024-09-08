package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.MovimentacaoRequest;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface MovimentacaoService {

    void salvarMovimentacao(MovimentacaoCarteiraEntity entity);
    void gerarMovimentacaoCarteira(Long id, MovimentacaoRequest request);
    List<MovimentacaoCarteiraEntity> listarMovimentacaoAll();
    List<MovimentacaoCarteiraEntity> listarMovimentacaoByIdCarteira(Long id);

    List<MovimentacaoCarteiraEntity> listarMovimentacaoByIdCarteiraAndData(Long idCarteira, List<LocalDate> range);
}
