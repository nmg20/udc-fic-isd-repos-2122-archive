package es.udc.ws.app.model.excursionservice.exceptions;

public class EmailCancelacionReservaDistinto extends Exception{
    private Long reservaId;
    private String email;

    public EmailCancelacionReservaDistinto(Long reservaId, String email){
        super("Email="+email+"no asociado a la reserva con id= \"" + reservaId);
        this.reservaId = reservaId;
        this.email = email;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}