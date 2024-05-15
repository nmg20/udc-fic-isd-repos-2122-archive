package es.udc.ws.app.model.excursionservice.exceptions;

public class InvalidUpdateDataException extends Exception{

    private Long excursionId;

    public InvalidUpdateDataException(Long excursionId) {
        super("Excursion con el id=\"" + excursionId + "\n no se puede actualizar porque no se puede" +
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