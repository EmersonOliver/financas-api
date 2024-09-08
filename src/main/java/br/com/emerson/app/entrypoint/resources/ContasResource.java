package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.ContaRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("contas")
public class ContasResource {

    @POST
    @Path("cadastrar")
    public Response persistirConta(ContaRequest request) {
        return null;
    }

}
