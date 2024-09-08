package br.com.emerson.domain.exception;

import br.com.emerson.domain.message.MessageErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ExceptionMapperHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        MessageErrorResponse messageError = MessageErrorResponse.builder().message(exception.getMessage()).build();
        log.warn("Ocorreu uma falha na requisicao--->" + exception.getMessage());
        exception.printStackTrace();
        return Response.serverError().entity(messageError).build();
    }
}
