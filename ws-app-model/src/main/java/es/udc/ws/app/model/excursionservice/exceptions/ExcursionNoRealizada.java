package es.udc.ws.app.model.excursionservice.exceptions;

public class ExcursionNoRealizada extends Throwable {

    private Long excursionId;
    public ExcursionNoRealizada(Long excursionId) {
        super("La reserva con id= \"" + excursionId + "\n ya ha sido previamente eliminada");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId){
        this.excursionId = excursionId;
    }
}
