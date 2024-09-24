package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.core.enums.SituacaoParcelaEnum;
import br.com.emerson.core.service.ParcelaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("parcelas")
public class ParcelaResource {


    @Inject
    ParcelaService parcelaService;

    @GET
    @Path("/cartao/{idCartao}")
    public Response loadParcelasByCartao(@PathParam("idCartao") UUID cartao) {
        return null;
    }

    @PUT
    @Path("atualizar/situacao/{id}")
    public Response atualizarSituacaoParcela(@PathParam("id") Long idParcela, SituacaoParcelaEnum situacao){
        parcelaService.atualizarSituacaoParcela(idParcela,situacao);
        return Response.accepted().build();
    }
}
