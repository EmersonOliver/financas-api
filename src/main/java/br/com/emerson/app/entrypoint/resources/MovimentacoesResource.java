package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.app.dto.request.MovimentacaoRequest;
import br.com.emerson.core.entity.MovimentacaoCarteiraEntity;
import br.com.emerson.core.service.MovimentacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("movimentacoes")
public class MovimentacoesResource {


    @Inject
    MovimentacaoService movimentacaoService;

    @POST
    @Path("gerar/movimentacao/{id}")
    public Response gerarMovimentacao(@PathParam("id") Long idCarteira, MovimentacaoRequest request) {
        movimentacaoService.gerarMovimentacaoCarteira(idCarteira, request);
        return Response.ok().build();
    }

    @GET
    @Path("carteira/{id}")
    public Response listarMovimentacoesByIdCarteira(@PathParam("id") Long idCarteira) {
        List<MovimentacaoCarteiraEntity> result = this.movimentacaoService.listarMovimentacaoByIdCarteira(idCarteira);
        return result.isEmpty() ? Response.status(Response.Status.NO_CONTENT).build() : Response.ok(result).build();
    }

    @GET
    @Path("carteira/data/{id}")
    public Response listarMovimentacoesByIdCarteiraAndDataMovimentacao(@PathParam("id") Long idCarteira,@QueryParam("periodo") List<String> range) {
        List<LocalDate> datas = range.stream()
                .map(data -> LocalDate.parse(data, DateTimeFormatter.ISO_LOCAL_DATE))
                .collect(Collectors.toList());
        List<MovimentacaoCarteiraEntity> result = this.movimentacaoService.listarMovimentacaoByIdCarteiraAndData(idCarteira, datas);
        return Response.ok(result).build();
    }

}
