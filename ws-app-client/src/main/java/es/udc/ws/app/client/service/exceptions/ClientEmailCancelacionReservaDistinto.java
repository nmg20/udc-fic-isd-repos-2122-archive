package es.udc.ws.app.client.service.exceptions;

public class ClientEmailCancelacionReservaDistinto extends Exception{
    private Long reservaId;
    private String email;

    public ClientEmailCancelacionReservaDistinto(Long reservaId){
        super("Email no asociado a la reserva con id=" + reservaId);
        this.reservaId = reservaId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}