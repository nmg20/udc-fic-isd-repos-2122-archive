package es.udc.ws.app.model.excursionservice.exceptions;

public class UpdateExcursionFueraDePlazo extends Exception{

    private Long excursionId;

    public UpdateExcursionFueraDePlazo(Long excursionId) {
        super("Excursion con el id=\"" + excursionId + "\n no se puede actualizar porque faltan menos de " +
                "72h para que empiece.");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}