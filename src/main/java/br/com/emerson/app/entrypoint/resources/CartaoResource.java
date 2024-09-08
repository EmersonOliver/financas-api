package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Path("cartao")
public class CartaoResource {

    @Inject
    CartaoService cartaoService;

    @POST
    @Path("cadastrar")
    public Response cadastrarCartao(CartaoRequest request) {
        cartaoService.salvar(request);
        return Response.ok().build();
    }

    @PUT
    @Path("atualizar/{id}")
    public Response atualizarCartao(@PathParam("id") UUID uuid, CartaoRequest request) {
        cartaoService.atualizar(uuid, request);
        return Response.ok().build();
    }

    @GET
    @Path("detalhar/{id}")
    public Response detalharCartao(@PathParam("id") UUID uuid) {
        var cartao = cartaoService.findCartaoById(uuid);
        return Response.ok(CartaoResponse.builder()
                        .idCartao(cartao.getIdCartao())
                        .apelido(cartao.getApelido())
                        .digitosFinais(cartao.getDigitosFinais())
                        .diaFechamento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaFechamento() : null)
                        .diaVencimento(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getDiaVencimento() : null)
                        .tipoCartao(cartao.getTipoCartao())
                        .vlLimiteTotal(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal() : null)
                        .vlLimiteUtilizado(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteUtilizado() : null)
                        .vlLimiteRestante(!cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getVlLimiteTotal().subtract(cartao.getVlLimiteUtilizado()) : null)
                        .vlSaldo(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor() : null)
                        .vlSaldoUtilizado(TipoCartaoEnum.valorComprasMesAtual(cartao))
                        .vlSaldoRestante(cartao.getTipoCartao().equals(TipoCartaoEnum.DEBITO) ? cartao.getCarteira().getValor().subtract(TipoCartaoEnum.valorComprasMesAtual(cartao)) : null)
                        .cartaoReferencia(cartao.getIdCartaoReferencia())
                        .icAtivo(cartao.getIcAtivo())
                        .compras(cartao.getCompras())
                        .faturas(cartao.getFaturas())
                        .build())
                .build();
    }

    @GET
    @Path("listAll")
    public Response listarTodos() {
        return Response.ok(cartaoService.listarTodos()).build();
    }

    @GET
    @Path("buscarByNome")
    public Response findByName(@QueryParam("nome") String nome) {
        return Response.ok(cartaoService.buscarByNomeCartao(nome)).build();
    }

    @GET
    @Path("buscarByTipoCartao")
    public Response findByTipoCartao(@QueryParam("tipo") TipoCartaoEnum tipoCartao) {
        return Response.ok(cartaoService.buscarByTipoCartao(tipoCartao)).build();
    }

    @GET
    @Path("buscarByValor")
    public Response findByValor(@QueryParam("valor") List<BigDecimal> range) {
        return Response.ok(cartaoService.buscarByValor(range)).build();
    }

    @GET
    @Path("inativar/{id}")
    public Response inativarCartao(@PathParam("id") UUID idCartao) {
        cartaoService.inativarCartao(idCartao);
        return Response.ok().build();
    }

}
