package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.core.service.FaturaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.UUID;

@Path("fatura")
public class FaturaResource {


    @Inject
    FaturaService faturaService;

    @GET
    @Path("cartao/{id}")
    public Response listarFaturasCartao(@PathParam("id") UUID idCartao) {
        return Response.ok(faturaService.findFaturaByIdCartao(idCartao)).build();
    }

    @GET
    @Path("cartao/{id}/datas")
    public Response listarFaturasCartao(@PathParam("id") UUID idCartao,
                                        @QueryParam("dataInicio") String dataInicio,
                                        @QueryParam("dataFim") String dataFim) {
        return Response.ok(faturaService.findFaturaIdCartaoAndData(idCartao, dataInicio, dataFim)).build();
    }

    @GET
    @Path("contasApagar")
    public Response listarContasFaturasAPagar(@QueryParam("data") String date) {
        return null;
    }

}
