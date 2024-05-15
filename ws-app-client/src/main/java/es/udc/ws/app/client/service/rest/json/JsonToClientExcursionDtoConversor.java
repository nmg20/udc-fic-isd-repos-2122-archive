package es.udc.ws.app.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientExcursionDtoConversor {
    public static ObjectNode toObjectNode(ClientExcursionDto excursion) throws IOException {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if (excursion.getExcursionId() != null) {
            excursionObject.put("excursionId", excursion.getExcursionId());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDateTime = null;
        if(excursion.getFechaComienzo() !=null) {
            formattedDateTime = excursion.getFechaComienzo().format(formatter);
        }

        excursionObject.put("ciudad", excursion.getCiudad()).
                put("descripcion", excursion.getDescripcion()).
                put("fechaComienzo", formattedDateTime).
                put("cuotaPersona", excursion.getCuotaPersona()).
                put("numPlazas", excursion.getNumPlazas()).
                put("plazasDisponibles", excursion.getPlazasDisponibles());

        return excursionObject;
    }

    public static ClientExcursionDto toClientExcursionDto(InputStream jsonExcursion) throws ParsingException {
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);
            if (rootNode.getNodeType()!=JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientExcursionDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientExcursionDto> toClientExcursionDtos(InputStream jsonExcursiones) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursiones);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode excursionesArray = (ArrayNode) rootNode;
                List<ClientExcursionDto> excursionDtos = new ArrayList<>(excursionesArray.size());
                for (JsonNode excursionNode : excursionesArray) {
                    excursionDtos.add(toClientExcursionDto(excursionNode));
                }
                return excursionDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientExcursionDto toClientExcursionDto(JsonNode excursionNode) throws ParsingException {
        if (excursionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode excursionObject = (ObjectNode) excursionNode;

            JsonNode excursionIdNode = excursionObject.get("excursionId");
            Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

            String ciudad = excursionObject.get("ciudad").textValue().trim();
            String descripcion = excursionObject.get("descripcion").textValue().trim();
            String fechaComienzoAux =  excursionObject.get("fechaComienzo").textValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaComienzo = LocalDateTime.parse(fechaComienzoAux, formatter);
            float cuotaPersona =  excursionObject.get("cuotaPersona").floatValue();
            int numPlazas = excursionObject.get("numPlazas").intValue();
            int plazasDisponibles = excursionObject.get("plazasDisponibles").intValue();

            return new ClientExcursionDto(excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas,
                    plazasDisponibles);
        }
    }
}
