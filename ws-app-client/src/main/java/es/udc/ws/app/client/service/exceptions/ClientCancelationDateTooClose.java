package es.udc.ws.app.client.service.exceptions;

public class ClientCancelationDateTooClose extends Exception{
    private Long reservaId;

    public ClientCancelationDateTooClose(Long reservaId){
        super("La excursion asociada a la reserva con id=" + reservaId + " esta a menos de 48 horas de su inicio");
        this.reservaId = reservaId;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId){
        this.reservaId = reservaId;
    }
}
