package org.tdakkota.ncproject.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tdakkota.ncproject.entities.IncidentEvent;

import javax.websocket.*;

public class IncidentEventEncoder implements Decoder.Text<IncidentEvent>, Encoder.Text<IncidentEvent> {
    ObjectMapper jackson = new ObjectMapper();

    /**
     * Encode the given object into a String.
     *
     * @param object the object being encoded.
     * @return the encoded object as a string.
     * @throws EncodeException The provided object could not be encoded as a string
     */
    @Override
    public String encode(IncidentEvent object) throws EncodeException {
        try {
            return jackson.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        }
    }

    /**
     * Decode the given String into an object of type T.
     *
     * @param s string to be decoded.
     * @return the decoded message as an object of type T
     * @throws DecodeException If the provided string cannot be decoded to type T
     */
    @Override
    public IncidentEvent decode(String s) throws DecodeException {
        try {
            return jackson.readValue(s, IncidentEvent.class);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
    }
}
