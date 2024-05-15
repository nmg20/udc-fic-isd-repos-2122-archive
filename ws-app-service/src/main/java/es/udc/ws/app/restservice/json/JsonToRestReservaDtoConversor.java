package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.app.restservice.dto.RestReservaDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonToRestReservaDtoConversor {

    public static ObjectNode toObjectNode(RestReservaDto reserva) {

        ObjectNode reservaNode = JsonNodeFactory.instance.objectNode();

        if (reserva.getReservaId() != null) {
            reservaNode.put("reservaId", reserva.getReservaId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDateTime = null;
        if(reserva.getFechaCancelacion() != null) {
            formattedDateTime = reserva.getFechaCancelacion().format(formatter);
        }

        reservaNode.put("excursionId", reserva.getExcursionId()).
                put("emailUsuario", reserva.getEmailUsuario()).
                put("fechaCancelacion", formattedDateTime).
                put("numPlazas", reserva.getNumPlazas()).
                put("tarjetaBancaria", reserva.getTarjetaBancaria()).
                put("costeTotal", reserva.getCosteTotal());

        return reservaNode;
    }

    public static ArrayNode toArrayNode(List<RestReservaDto> reservas){
        ArrayNode reservasNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < reservas.size(); i++) {
            RestReservaDto reservaDto = reservas.get(i);
            ObjectNode reservaObject = toObjectNode(reservaDto);
            reservasNode.add(reservaObject);
        }
        return reservasNode;
    }

    public static RestReservaDto toRestReservaDto(InputStream jsonReserva) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode reservasNode = (ObjectNode) rootNode;

                JsonNode reservaIdNode = reservasNode.get("reservaId");
                Long reservaId = (reservaIdNode != null) ? reservaIdNode.longValue() : null;
/*
                                    add("emailUsuario", email).
                                    add("excursionId", String.valueOf(excursionId)).
                                    add("numPlazas", Integer.toString(numPlazas)).
                                    add("tarjetaBancaria", tarjetaBancaria).
 */
                Long excursionId = reservasNode.get("excursionId").longValue();
                String emailUsuario = reservasNode.get("emailUsuario").textValue().trim();
                int numPlazas = reservasNode.get("numPlazas").intValue();
                String tarjetaBancaria = reservasNode.get("tarjetaBancaria").textValue().trim();

                return new RestReservaDto(reservaId, excursionId, emailUsuario,
                        numPlazas, tarjetaBancaria, 0.0F, null);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
