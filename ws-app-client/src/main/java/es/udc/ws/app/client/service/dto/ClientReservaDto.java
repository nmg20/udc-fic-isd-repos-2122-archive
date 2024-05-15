package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientReservaDto {

    private Long reservaId;
    private Long excursionId;
    private String emailUsuario;
    private int numPlazas;
    private String tarjetaBancaria;
    private LocalDateTime fechaReserva;
    private float costeTotal;
    private LocalDateTime fechaCancelacion;

    public ClientReservaDto(){
    }

    public ClientReservaDto(Long reservaId, Long excursionId, String emailUsuario, int numPlazas,
                            String tarjetaBancaria, float costeTotal, LocalDateTime fechaCancelacion){
        this.reservaId = reservaId;
        this.excursionId = excursionId;
        this.emailUsuario = emailUsuario;
        this.numPlazas = numPlazas;
        this.tarjetaBancaria = tarjetaBancaria;
        this.costeTotal=costeTotal;
        this.fechaCancelacion = fechaCancelacion;
    }

    public ClientReservaDto(Long reservaId, Long excursionId, String emailUsuario, int numPlazas,
                          String tarjetaBancaria, float costeTotal){
        this.reservaId = reservaId;
        this.excursionId = excursionId;
        this.emailUsuario = emailUsuario;
        this.numPlazas = numPlazas;
        this.tarjetaBancaria = tarjetaBancaria;
        this.costeTotal=costeTotal;
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

        String reserva = "Id Excursion: " + excursionId;
        if(fechaCancelacion == null){
            reserva += ", No cancelada (sin fecha de cancelacion)";
        }else{
            reserva += ", Fecha cancelacion: "+ fechaCancelacion.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        }

        reserva += ", Tarjeta: " + tarjetaBancaria+
                ", Plazas: " + numPlazas+
                ", Precio por persona: " + costeTotal/numPlazas+
                ", Email: " + emailUsuario;

        return reserva;
    }
}