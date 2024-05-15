package es.udc.ws.app.client.service.exceptions;

public class ClientInvalidUpdateDataException extends Exception{

    private Long excursionId;

    public ClientInvalidUpdateDataException(Long excursionId) {
        super("Excursion con el id=" + excursionId + " no se puede actualizar porque no se puede" +
                " adelantar la fecha de comienzo.");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}