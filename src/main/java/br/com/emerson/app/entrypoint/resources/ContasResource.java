package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.CalcularCustoFixoRequest;
import br.com.emerson.app.dto.request.ContaRequest;
import br.com.emerson.core.service.ContasService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("contas")
public class ContasResource {

    @Inject
    ContasService contasService;

    @POST
    @Path("cadastrar")
    public Response persistirConta(ContaRequest request) {
        return null;
    }

    @GET
    @Path("all")
    public Response getAllContas() {
        return Response.ok(contasService.listarContas()).build();
    }

    @GET
    @Path("custo/fixo")
    public Response getAllCustoFixo() {
        return Response.ok(contasService.listarCustoFixo()).build();
    }

    @POST
    @Path("calcular/custo/fixo")
    public Response simularComprasCustoByCustoFixo(CalcularCustoFixoRequest request) {
        return null;
    }

}
