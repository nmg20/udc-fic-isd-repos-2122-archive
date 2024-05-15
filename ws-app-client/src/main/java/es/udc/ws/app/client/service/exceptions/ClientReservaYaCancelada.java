package es.udc.ws.app.client.service.exceptions;

public class ClientReservaYaCancelada extends Exception {

    private Long reservaId;

    public ClientReservaYaCancelada(Long reservaId){
        super("La reserva con id=" + reservaId + " ya ha sido previamente eliminada");
        this.reservaId = reservaId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}