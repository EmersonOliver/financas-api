package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.CarteiraRequest;
import br.com.emerson.app.dto.response.CarteiraResponse;
import br.com.emerson.app.dto.response.SaldoCarteiraResponse;
import br.com.emerson.app.dto.response.SimulacaoCarteiraResponse;
import br.com.emerson.core.entity.CarteiraEntity;
import br.com.emerson.core.service.CarteiraService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.function.BiFunction;

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
        var carteira = carteiraService.listarTodos();
        BigDecimal saldo = carteira.stream()
                .map(CarteiraResponse::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saldoCarteira = carteira.stream()
                .map(s-> carteiraService.saldoValor(s.getIdCarteira()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Response.ok(SaldoCarteiraResponse.builder().saldo(saldoCarteira).build()).build();
    }

    @GET
    @Path("listAll")
    public Response listarTodos() {
        return Response.ok(carteiraService.listarTodos())
                .build();
    }

    @GET
    @Path("simulacao/{id}")
    public Response getSimulacao(@PathParam("id") Long id) {
        BigDecimal saldo = BigDecimal.ZERO;
        SimulacaoCarteiraResponse simulacao = this.carteiraService.simulacaoCarteiraResponse(id).stream().reduce(SimulacaoCarteiraResponse.builder().build(), (acc, sim) -> {
            acc.setSaldoFuturo(saldo.add(sim.getSaldoFuturo()));
            acc.setData(sim.getData());
            return acc;
        });
        return Response.ok(simulacao).build();
    }

}
