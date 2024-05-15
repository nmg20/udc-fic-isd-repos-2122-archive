package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestReservaDto {

    private Long reservaId;
    private Long excursionId;
    private String emailUsuario;
    private int numPlazas;
    private String tarjetaBancaria;
    private LocalDateTime fechaReserva;
    private float costeTotal;
    private LocalDateTime fechaCancelacion;

    public RestReservaDto(){
    }

    public RestReservaDto(Long reservaId, Long excursionId, String emailUsuario, int numPlazas,
                          String tarjetaBancaria, float costeTotal, LocalDateTime fechaCancelacion){
        this.reservaId = reservaId;
        this.excursionId = excursionId;
        this.emailUsuario = emailUsuario;
        this.numPlazas = numPlazas;
        this.tarjetaBancaria = tarjetaBancaria;
        this.costeTotal=costeTotal;
        this.fechaCancelacion = fechaCancelacion;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getNumPlazas() {
        return numPlazas;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
    }

    public String getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(String tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public float getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(float costeTotal) {
        this.costeTotal = costeTotal;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    @Override
    public String toString() {

        String reserva = "Id Excursión: " + excursionId;
        if(fechaCancelacion == null){
            reserva += ", No cancelada";
        }else{
            reserva += ", Cancelada (" + fechaCancelacion + ")";
        }

        reserva += ", Tarjeta: '" + tarjetaBancaria + '\'';
        reserva += ", Plazas: " + numPlazas;
        reserva += ", Precio: " + costeTotal + '}';

        return  reserva;
    }
}