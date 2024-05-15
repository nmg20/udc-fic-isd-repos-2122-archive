package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToRestExcursionDtoConversor {

    public static ObjectNode toObjectNode(RestExcursionDto excursion){
        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();
        if(excursion.getExcursionId()!=null){
            excursionObject.put("excursionId",excursion.getExcursionId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedDateTime = null;
        if(excursion.getFechaComienzo() !=null) {
             formattedDateTime = excursion.getFechaComienzo().format(formatter);
        }
        excursionObject.put("ciudad",excursion.getCiudad()).
        put("descripcion",excursion.getDescripcion()).
        put("fechaComienzo", formattedDateTime).
        put("cuotaPersona",excursion.getCuotaPersona()).
        put("numPlazas",excursion.getNumPlazas()).
        put("plazasDisponibles",excursion.getPlazasDisponibles());
        return excursionObject;
    }

    public static ArrayNode toArrayNode(List<RestExcursionDto> excursiones){
        ArrayNode excursionesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < excursiones.size(); i++) {
            RestExcursionDto movieDto = excursiones.get(i);
            ObjectNode movieObject = toObjectNode(movieDto);
            excursionesNode.add(movieObject);
        }
        return excursionesNode;
    }

    public static RestExcursionDto toRestExcursionDto(InputStream jsonMovie) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonMovie);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode excursionesNode = (ObjectNode) rootNode;

                JsonNode excursionIdNode = excursionesNode.get("excursionId");
                Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

                String ciudad = excursionesNode.get("ciudad").textValue().trim();
                String descripcion = excursionesNode.get("descripcion").textValue().trim();
                String fechaComienzoAux =  excursionesNode.get("fechaComienzo").textValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime fechaComienzo = LocalDateTime.parse(fechaComienzoAux, formatter);
                float cuotaPersona =  excursionesNode.get("cuotaPersona").floatValue();
                int numPlazas = excursionesNode.get("numPlazas").intValue();
                return new RestExcursionDto(excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
