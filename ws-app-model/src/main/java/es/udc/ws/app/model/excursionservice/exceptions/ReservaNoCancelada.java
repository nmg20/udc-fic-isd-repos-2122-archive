package es.udc.ws.app.model.excursionservice.exceptions;

public class ReservaNoCancelada extends Throwable{

    private Long excursionId;
    public ReservaNoCancelada(Long excursionId) {
        super("La excursion con id= \"" + excursionId + "\n no tiene todas sus reservas canceladas");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId){
        this.excursionId = excursionId;
    }

}
