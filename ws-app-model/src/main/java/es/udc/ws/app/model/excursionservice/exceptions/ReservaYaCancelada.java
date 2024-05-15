package es.udc.ws.app.model.excursionservice.exceptions;


public class ReservaYaCancelada extends Exception {

    private Long reservaId;

    public ReservaYaCancelada(Long reservaId){
        super("La reserva con id= \"" + reservaId + "\n ya ha sido previamente eliminada");
        this.reservaId = reservaId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}
