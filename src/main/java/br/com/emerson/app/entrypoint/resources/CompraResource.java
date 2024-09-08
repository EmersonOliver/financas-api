package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.ComprasRequest;
import br.com.emerson.core.service.ComprasService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("compra")
public class CompraResource {


    @Inject
    ComprasService comprasService;

    @POST
    @Path("cadastrar")
    public Response cadastrarCompra(ComprasRequest compras) {
        comprasService.salvar(compras);
        return Response.ok().build();
    }

    @PUT
    @Path("lancamentos/{id}")
    public Response lancamentos(@PathParam("id") Long id, ComprasRequest compras) {
        return null;
    }

}
