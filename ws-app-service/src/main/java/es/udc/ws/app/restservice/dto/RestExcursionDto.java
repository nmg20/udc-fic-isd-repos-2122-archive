package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestExcursionDto {
    private Long excursionId;
    private String ciudad;
    private String descripcion;
    private LocalDateTime fechaComienzo;
    private float cuotaPersona;
    private int numPlazas;
    private int plazasDisponibles;

    public RestExcursionDto(){
    }

    public RestExcursionDto(Long excursionId, String ciudad, String descripcion, LocalDateTime fechaComienzo,
                            float cuotaPersona, int numPlazas, int plazasDisponibles){
        this.excursionId=excursionId;
        this.ciudad=ciudad;
        this.descripcion=descripcion;
        this.fechaComienzo=fechaComienzo;
        this.cuotaPersona=cuotaPersona;
        this.numPlazas=numPlazas;
        this.plazasDisponibles=plazasDisponibles;
    }

    public RestExcursionDto(Long excursionId, String ciudad, String descripcion, LocalDateTime fechaComienzo,
                            float cuotaPersona, int numPlazas){
        this.excursionId=excursionId;
        this.ciudad=ciudad;
        this.descripcion=descripcion;
        this.fechaComienzo=fechaComienzo;
        this.cuotaPersona=cuotaPersona;
        this.numPlazas=numPlazas;
        this.plazasDisponibles=numPlazas;
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
        return "ExcursionDto [excursionId=" + excursionId + ", ciudad=" + ciudad
                + ", descripcion=" + descripcion
                + ", fechaComienzo=" + fechaComienzo + ", cuotaPersona=" + cuotaPersona
                + ", numPlazas=" + numPlazas + ", plazasDisponibles=" + plazasDisponibles + "]";
    }
}
