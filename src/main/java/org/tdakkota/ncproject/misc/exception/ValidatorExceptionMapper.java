package org.tdakkota.ncproject.misc.exception;

import org.tdakkota.ncproject.api.APIError;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ValidatorExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    /**
     * Map an exception to a {@link Response}. Returning
     * {@code null} results in a {@link Response.Status#NO_CONTENT}
     * response. Throwing a runtime exception results in a
     * {@link Response.Status#INTERNAL_SERVER_ERROR} response.
     *
     * @param exception the exception to map to a response.
     * @return a response mapped from the supplied exception.
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream().
                map(c -> c.getPropertyPath().toString() + ": " + c.getMessage()).
                collect(Collectors.joining(", "));

        return Response.status(Response.Status.BAD_REQUEST).
                type(MediaType.APPLICATION_JSON_TYPE).
                entity(new APIError(message)).
                build();
    }
}