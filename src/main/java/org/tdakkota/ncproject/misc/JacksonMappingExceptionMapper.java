package org.tdakkota.ncproject.misc;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.tdakkota.ncproject.api.APIError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class JacksonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
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
    public Response toResponse(JsonMappingException exception) {
        return Response.status(Response.Status.BAD_REQUEST).
                type(MediaType.APPLICATION_JSON_TYPE).
                entity(new APIError(exception.getMessage())).
                build();
    }
}