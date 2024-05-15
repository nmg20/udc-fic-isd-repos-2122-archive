package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AppExceptionToJsonConversor {

    public static ObjectNode toReservaNumPlazasNoDisponibleException(NumPlazasNoDisponible ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NumPlazasNoDisponible");
        exceptionObject.put("numPlazas", ex.getNumPlazas());
        exceptionObject.put("plazasDisponibles", ex.getPlazasDisponibles());

        return exceptionObject;
    }

    public static ObjectNode toReservaPlazoDeReservaCerradoException(PlazoDeReservaCerrado ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "PlazoDeReservaCerrado");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }

    public static ObjectNode toReservaReservaYaCanceladaException(ReservaYaCancelada ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservaYaCancelada");
        exceptionObject.put("reservaId", ex.getReservaId());

        return exceptionObject;
    }

    public static ObjectNode toReservaCancelationDateTooCloseException(CancelationDateTooClose ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "CancelationDateTooClose");
        exceptionObject.put("reservaId", ex.getReservaId());

        return exceptionObject;
    }

    public static ObjectNode toReservaEmailCancelacionReservaDistintoException(EmailCancelacionReservaDistinto ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "EmailCancelacionReservaDistinto");
        exceptionObject.put("reservaId", ex.getReservaId());

        return exceptionObject;
    }

    public static ObjectNode toInvalidUpdateDataException(InvalidUpdateDataException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidUpdateDataException");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }

    public static ObjectNode toUpdateExcursionFueraDePlazoException(UpdateExcursionFueraDePlazo ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "UpdateExcursionFueraDePlazo");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }

    public static ObjectNode toExcursionFueraDePlazoException(ExcursionFueraDePlazo ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ExcursionFueraDePlazo");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }

    public static ObjectNode toExcursionNoCelebrada(ExcursionNoCelebrada ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ExcursionNoCelebrada");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }

    public static ObjectNode toReservaNoCancelada(ReservaNoCancelada ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservaNoCancelada");
        exceptionObject.put("excursionId", ex.getExcursionId());

        return exceptionObject;
    }





}