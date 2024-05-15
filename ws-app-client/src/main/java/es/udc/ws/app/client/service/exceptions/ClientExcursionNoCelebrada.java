package es.udc.ws.app.client.service.exceptions;

public class ClientExcursionNoCelebrada extends Exception{

    private Long excursionId;

    public ClientExcursionNoCelebrada(Long excursionId) {
        super("Excursion con el id=" + excursionId + " aun no ha sido celebrada.");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}
