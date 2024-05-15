package es.udc.ws.app.client.service.exceptions;

public class ClientUpdateExcursionFueraDePlazo extends Exception{

    private Long excursionId;

    public ClientUpdateExcursionFueraDePlazo(Long excursionId) {
        super("Excursion con el id=" + excursionId + " no se puede actualizar porque faltan menos de " +
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