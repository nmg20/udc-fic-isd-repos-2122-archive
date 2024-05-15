package es.udc.ws.app.client.service.exceptions;

public class ClientExcursionFueraDePlazo extends Exception{

    private Long excursionId;

    public ClientExcursionFueraDePlazo(Long excursionId) {
        super("Excursion con el id=" + excursionId + " no se puede crear porque entre la Fecha de alta y la fecha de " +
                "comienzo hay menos de 72h de diferencia.");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}
