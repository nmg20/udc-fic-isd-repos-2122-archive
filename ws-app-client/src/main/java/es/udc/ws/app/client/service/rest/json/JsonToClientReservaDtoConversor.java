package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientReservaDtoConversor {
    public static ObjectNode toObjectNode(ClientReservaDto reserva) {

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

    public static ClientReservaDto toClientReservaDto(InputStream jsonReserva) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);
            if (rootNode.getNodeType()!=JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientReservaDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientReservaDto> toClientReservaDtos(InputStream jsonReservas) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReservas);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode reservasArray = (ArrayNode) rootNode;
                List<ClientReservaDto> reservaDtos = new ArrayList<>(reservasArray.size());
                for (JsonNode reservaNode : reservasArray) {
                    reservaDtos.add(toClientReservaDto(reservaNode));
                }
                return reservaDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ClientReservaDto toClientReservaDto(JsonNode reservaNode) throws ParsingException {

        if (reservaNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode reservaObject = (ObjectNode) reservaNode;

            JsonNode reservaIdNode = reservaObject.get("reservaId");
            Long reservaId = (reservaIdNode != null) ? reservaIdNode.longValue() : null;

            Long excursionId = reservaObject.get("excursionId").longValue();
            String emailUsuario = reservaObject.get("emailUsuario").textValue().trim();
            int numPlazas = reservaObject.get("numPlazas").intValue();
            String tarjetaBancaria = reservaObject.get("tarjetaBancaria").textValue().trim();
            float costeTotal = (float)reservaObject.get("costeTotal").asDouble();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String fechaAux = null;
            LocalDateTime fechaCancelacion = null;

            if(reservaObject.get("fechaCancelacion").textValue() != null){
                fechaAux = reservaObject.get("fechaCancelacion").textValue();
                fechaCancelacion = LocalDateTime.parse(fechaAux, formatter);
            }


            return new ClientReservaDto(reservaId, excursionId, emailUsuario,
                    numPlazas, tarjetaBancaria, costeTotal, fechaCancelacion);
        }
    }
}
