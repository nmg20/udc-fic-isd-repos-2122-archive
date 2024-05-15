package es.udc.ws.app.model.excursionservice.exceptions;

public class ExcursionFueraDePlazo extends Exception{

    private Long excursionId;

    public ExcursionFueraDePlazo(Long excursionId) {
        super("Excursion con el id=\"" + excursionId + "\n no se puede crear porque entre la Fecha de alta y la fecha de " +
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
