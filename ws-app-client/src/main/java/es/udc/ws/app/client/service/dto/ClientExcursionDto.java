package es.udc.ws.app.client.service.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class ClientExcursionDto {
    private Long excursionId;
    private String ciudad;
    private String descripcion;
    private LocalDateTime fechaComienzo;
    private float cuotaPersona;
    private int numPlazas;
    private int plazasDisponibles;

    public ClientExcursionDto(){
    }

    public ClientExcursionDto(Long excursionId, String ciudad, String descripcion, LocalDateTime fechaComienzo,
                              float cuotaPersona, int numPlazas, int plazasDisponibles){
        this.excursionId=excursionId;
        this.ciudad=ciudad;
        this.descripcion=descripcion;
        this.fechaComienzo=fechaComienzo;
        this.cuotaPersona=cuotaPersona;
        this.numPlazas=numPlazas;
        this.plazasDisponibles=plazasDisponibles;
    }

    public ClientExcursionDto(Long excursionId, String ciudad, String descripcion, LocalDateTime fechaComienzo,
                              float cuotaPersona, int numPlazas){
        this.excursionId=excursionId;
        this.ciudad=ciudad;
        this.descripcion=descripcion;
        this.fechaComienzo=fechaComienzo;
        this.cuotaPersona=cuotaPersona;
        this.numPlazas=numPlazas;
    }

    public Long getExcursionId() {return excursionId;}
    public void setExcursionId(Long excursionId) {this.excursionId = excursionId;}

    public String getCiudad() {return ciudad;}
    public void setCiudad(String ciudad) {this.ciudad = ciudad;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public LocalDateTime getFechaComienzo() {return fechaComienzo;}
    public void setFechaComienzo(LocalDateTime fechaComienzo) {this.fechaComienzo = fechaComienzo;}

    public float getCuotaPersona() {return cuotaPersona;}
    public void setCuotaPersona(float cuotaPersona) {this.cuotaPersona = cuotaPersona;}

    public int getNumPlazas() {return numPlazas;}
    public void setNumPlazas(int numPlazas) {this.numPlazas = numPlazas;}

    public int getPlazasDisponibles() {return plazasDisponibles;}
    public void setPlazasDisponibles(int plazasDisponibles) {this.plazasDisponibles = plazasDisponibles;}



    @Override
    public String toString() {
        Duration duration = Duration.between(LocalDateTime.now(), fechaComienzo);
        // total seconds of difference (using Math.abs to avoid negative values)
        long seconds = Math.abs(duration.getSeconds());
        long days = seconds / 86400;
        seconds -= (days * 86400);
        long hours = seconds / 3600;
        seconds -= (hours * 3600);
        long minutes = seconds / 60;
        seconds -= (minutes * 60);

        float percentage = (((float)numPlazas-plazasDisponibles)/numPlazas) * 100;
        String excursion = "Id Excursión: " + excursionId;
        excursion += ", Plazas ocupadas: " + (numPlazas-plazasDisponibles);
        excursion += ", Plazas totales: " + numPlazas;
        excursion += ", Precio: " + cuotaPersona;
        excursion += ", Descripcion: " + descripcion;
        excursion += ", Tiempo restante: " + days + " dias " + hours + " horas " + minutes + " minutos ";
        excursion += ", Porcentaje de plazas ocupadas: " + Math.round(percentage) + "% ";



        return  excursion;
    }
}
