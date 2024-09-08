package br.com.emerson.core.service;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.core.entity.CartaoEntity;
import br.com.emerson.core.enums.TipoCartaoEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CartaoService {

    void salvar(CartaoRequest request);

    void atualizar(UUID uuid, CartaoRequest request);

    CartaoEntity atualizarLimite(UUID uuid, BigDecimal valorUtilizado);

    List<CartaoEntity> listerCartaoDebito();

    List<CartaoEntity> listarCartaoCredito();

    List<CartaoEntity> listarTodos();

    List<CartaoResponse> buscarByNomeCartao(String nome);

    List<CartaoResponse> buscarByTipoCartao(TipoCartaoEnum tipoCartaoEnum);

    List<CartaoResponse> buscarByValor(List<BigDecimal> range);

    List<CartaoEntity> listarCartoes();

    CartaoEntity findCartaoById(UUID idCartao);

    void inativarCartao(UUID idCartao);
}
