package es.udc.ws.app.model.excursionservice.exceptions;

public class PlazoDeReservaCerrado extends Exception{
    private Long excursionId;

    public PlazoDeReservaCerrado(Long excursionId) {
        super("La excursión con el id=\"" + excursionId + "\" no se puede reservar porque faltan menos de " +
                "24h para que empiece.");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}

