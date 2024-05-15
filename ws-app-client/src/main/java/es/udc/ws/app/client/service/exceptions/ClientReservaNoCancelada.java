package es.udc.ws.app.client.service.exceptions;

public class ClientReservaNoCancelada extends Exception{

    private Long excursionId;
    public ClientReservaNoCancelada(Long excursionId) {
        super("La excursion con id = " + excursionId + " no tiene todas sus reservas canceladas");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId){
        this.excursionId = excursionId;
    }

}
