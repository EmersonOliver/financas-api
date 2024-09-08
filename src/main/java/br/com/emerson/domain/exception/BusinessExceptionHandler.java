package br.com.emerson.domain.exception;

import br.com.emerson.domain.message.MessageErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class BusinessExceptionHandler implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        MessageErrorResponse messageError = MessageErrorResponse.builder().message(exception.getMessage()).build();
        log.warn("Ocorreu uma falha na validacao dos dados--->" + exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(messageError).build();
    }
}
