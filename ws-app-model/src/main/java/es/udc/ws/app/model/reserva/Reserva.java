package es.udc.ws.app.model.reserva;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {

    private Long reservaId;
    private Long excursionId;
    private String emailUsuario;
    private int numPlazas;
    private String tarjetaBancaria;
    private LocalDateTime fechaReserva;
    private float costeTotal;
    private LocalDateTime fechaCancelacion;

    public Reserva(Long excursionId, String emailUsuario, int numPlazas, String tarjetaBancaria,
                   LocalDateTime fechaReserva, float costeTotal){
        this.excursionId = excursionId;
        this.emailUsuario = emailUsuario;
        this.numPlazas = numPlazas;
        this.tarjetaBancaria = tarjetaBancaria;
        this.fechaReserva = (fechaReserva != null) ? fechaReserva.withNano(0) : null;
        this.costeTotal = costeTotal;
        this.fechaCancelacion = null;
    }

    public Reserva(Long reservaId, Long excursionId, String emailUsuario, int numPlazas, String tarjetaBancaria,
                   LocalDateTime fechaReserva, float costeTotal){
        this(excursionId, emailUsuario, numPlazas, tarjetaBancaria, fechaReserva, costeTotal);
        this.reservaId = reservaId;
        this.fechaCancelacion = null;
    }

    public Long getReservaId(){ return reservaId;}

    public void setReservaId(Long reservaId){ this.reservaId=reservaId;}

    public Long getExcursionId(){ return excursionId;}

    public void setExcursionId(Long excursionId){ this.excursionId=excursionId;}

    public String getEmailUsuario(){ return emailUsuario;}

    public void setEmailUsuario(String emailUsuario){ this.emailUsuario=emailUsuario;}

    public int getNumPlazas(){ return numPlazas;}

    public void setNumPlazas(int numPlazas){ this.numPlazas=numPlazas;}

    public String getTarjetaBancaria(){ return tarjetaBancaria;}

    public void setTarjetaBancaria(String tarjetaBancaria){ this.tarjetaBancaria=tarjetaBancaria;}

    public LocalDateTime getFechaReserva(){ return fechaReserva;}

    public void setFechaReserva(LocalDateTime fechaReserva){
        this.fechaReserva = (fechaReserva != null) ? fechaReserva.withNano(0) : null;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reserva reserva = (Reserva) o;

        if (numPlazas != reserva.numPlazas) return false;
        if (Float.compare(reserva.costeTotal, costeTotal) != 0) return false;
        if (!reservaId.equals(reserva.reservaId)) return false;
        if (!excursionId.equals(reserva.excursionId)) return false;
        if (!emailUsuario.equals(reserva.emailUsuario)) return false;
        if (!tarjetaBancaria.equals(reserva.tarjetaBancaria)) return false;
        if (!fechaReserva.equals(reserva.fechaReserva)) return false;
        return Objects.equals(fechaCancelacion, reserva.fechaCancelacion);
    }

    @Override
    public int hashCode() {
        int result = reservaId.hashCode();
        result = 31 * result + excursionId.hashCode();
        result = 31 * result + emailUsuario.hashCode();
        result = 31 * result + numPlazas;
        result = 31 * result + tarjetaBancaria.hashCode();
        result = 31 * result + fechaReserva.hashCode();
        result = 31 * result + (costeTotal != +0.0f ? Float.floatToIntBits(costeTotal) : 0);
        result = 31 * result + (fechaCancelacion != null ? fechaCancelacion.hashCode() : 0);
        return result;
    }
}
