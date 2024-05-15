package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else if (errorType.equals("CancelationDateTooClose")) { //400 bad request
                    return toClientCancelationDateTooClose(rootNode);
                }else if (errorType.equals("EmailCancelacionReservaDistinto")) { //400
                    return toClientEmailCancelationReservaDistinto(rootNode);
                }else if (errorType.equals("InvalidUpdateDataException")) { //400
                    return toClientInvalidUpdateDataException(rootNode);
                }else if (errorType.equals("ReservaYaCancelada")) { //400
                    return toClientReservaYaCancelada(rootNode);
                }else if (errorType.equals("UpdateExcursionFueraDePlazo")) { //400
                    return toClientUpdateExcursionFueraDePlazo(rootNode);
                }else if(errorType.equals("ReservaNoCancelada")) {
                    return toClientReservaNoCancelada(rootNode);
                }else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else if (errorType.equals("PlazoDeReservaCerrado")) { //404
                    return toClientPlazoDeReservaCerrado(rootNode);
                } else if (errorType.equals("NumPlazasNoDisponible")) { //404
                    return toClientNumPlazasNoDisponible(rootNode);
                } else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("ExcursionFueraDePlazo")) { //410
                    return toClientExcursionFueraDePlazo(rootNode);
                } else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    private static ClientCancelationDateTooClose toClientCancelationDateTooClose(JsonNode rootNode) {
        Long reservaId = rootNode.get("reservaId").longValue();
        return new ClientCancelationDateTooClose(reservaId);
    }

    private static ClientEmailCancelacionReservaDistinto toClientEmailCancelationReservaDistinto(JsonNode rootNode) {
        Long reservaId = rootNode.get("reservaId").longValue();
        return new ClientEmailCancelacionReservaDistinto(reservaId);
    }

    private static ClientExcursionFueraDePlazo toClientExcursionFueraDePlazo(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientExcursionFueraDePlazo(excursionId);
    }

    private static ClientInvalidUpdateDataException toClientInvalidUpdateDataException(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientInvalidUpdateDataException(excursionId);
    }

    private static ClientNumPlazasNoDisponible toClientNumPlazasNoDisponible(JsonNode rootNode) {
        int numPlazas = rootNode.get("numPlazas").intValue();
        int plazasDisponibles = rootNode.get("plazasDisponibles").intValue();
        return new ClientNumPlazasNoDisponible(numPlazas, plazasDisponibles);
    }

    private static ClientPlazoDeReservaCerrado toClientPlazoDeReservaCerrado(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientPlazoDeReservaCerrado(excursionId);
    }

    private static ClientReservaYaCancelada toClientReservaYaCancelada(JsonNode rootNode) {
        Long reservaId = rootNode.get("reservaId").longValue();
        return new ClientReservaYaCancelada(reservaId);
    }

    private static ClientUpdateExcursionFueraDePlazo toClientUpdateExcursionFueraDePlazo(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientUpdateExcursionFueraDePlazo(excursionId);
    }

    private static ClientReservaNoCancelada toClientReservaNoCancelada(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientReservaNoCancelada(excursionId);
    }



}
