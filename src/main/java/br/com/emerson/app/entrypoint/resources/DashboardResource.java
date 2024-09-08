package br.com.emerson.app.entrypoint.resources;

import br.com.emerson.core.service.DashboardService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("dashboard")
public class DashboardResource {

    @Inject
    DashboardService dashboardService;

    @GET
    @Path("loadCards")
    public Response cartoes() {
        return Response.ok(dashboardService.cartoesAll()).build();
    }

    @GET
    @Path("loadFaturasByData")
    public Response faturasByData(@QueryParam("dataInicio") String dataInicio, @QueryParam("dataFim") String dataFim) throws ParseException {
        List<Date> range = List.of(new SimpleDateFormat("yyyy-MM-dd").parse(dataInicio), new SimpleDateFormat("yyyy-MM-dd").parse(dataFim));
        return Response.ok(dashboardService.findFaturaByData(range)).build();
    }

    @GET
    @Path("loadFaturas")
    public Response loadFaturas() {
        return null;
    }
}
