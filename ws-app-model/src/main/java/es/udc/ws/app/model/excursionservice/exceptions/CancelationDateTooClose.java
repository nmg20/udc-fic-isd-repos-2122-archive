package es.udc.ws.app.model.excursionservice.exceptions;

public class CancelationDateTooClose extends Exception{
    private Long reservaId;

    public CancelationDateTooClose(Long reservaId){
        super("La excursion asociada a la reserva con id= \"" + reservaId + "\n esta a menos de 48 horas de su inicio");
        this.reservaId = reservaId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}
