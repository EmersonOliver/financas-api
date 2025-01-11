package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.CartaoRequest;
import br.com.emerson.app.dto.response.CartaoResponse;
import br.com.emerson.app.entrypoint.cron.service.TemporizadorService;
import br.com.emerson.core.enums.TipoCartaoEnum;
import br.com.emerson.core.service.CartaoService;
import br.com.emerson.mapper.CartaoMapper;
import jakarta.inject.Inject;
import jakarta.persistence.Temporal;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Path("cartao")
public class CartaoResource {

    @Inject
    CartaoService cartaoService;

    @Inject
    TemporizadorService temporizadorService;

    @POST
    @Path("cadastrar")
    public Response cadastrarCartao(CartaoRequest request) {
       var cartao = cartaoService.salvar(request);
        this.temporizadorService.cadastrarNovoJobCartao(cartao);
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
        return Response.ok(CartaoMapper.toCartaoResponse(cartao))
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
