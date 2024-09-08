package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.CarteiraRequest;
import br.com.emerson.app.dto.response.CarteiraResponse;
import br.com.emerson.app.dto.response.SaldoCarteiraResponse;
import br.com.emerson.core.service.CarteiraService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

@Path("carteira")
public class CarteiraResource {

    @Inject
    CarteiraService carteiraService;

    @POST
    @Path("cadastrar")
    public Response criarCarteiras(CarteiraRequest request) {
        carteiraService.salvar(request);
        return Response.ok().build();
    }

    @PUT
    @Path("atualizar/{id}")
    public Response atualizarCarteira(@PathParam("id") Long id, CarteiraRequest request) {
        carteiraService.atualizar(id, request);
        return Response.ok().build();
    }

    @GET
    @Path("saldo")
    public Response saldoCarteira() {
        BigDecimal saldo = carteiraService.listarTodos().stream()
                .map(CarteiraResponse::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        return Response.ok(SaldoCarteiraResponse.builder().saldo(saldo).build()).build();
    }

    @GET
    @Path("listAll")
    public Response listarTodos() {
        return Response.ok(carteiraService.listarTodos())
                .build();
    }

}
