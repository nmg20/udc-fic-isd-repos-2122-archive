package es.udc.ws.app.model.excursion;

import java.time.LocalDateTime;

public class Excursion {
    private Long excursionId;
    private String ciudad;
    private String descripcion;
    private LocalDateTime fechaComienzo;
    private float cuotaPersona;
    private int numPlazas;
    private int plazasDisponibles;
    private LocalDateTime fechaAlta;

    public Excursion(String ciudad, String descripcion,
                     LocalDateTime fechaComienzo, float cuotaPersona, int numPlazas) {
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.fechaComienzo = (fechaComienzo != null) ? fechaComienzo.withNano(0) : null;
        this.cuotaPersona = cuotaPersona;
        this.numPlazas = numPlazas;
        this.plazasDisponibles = numPlazas;
    }

    public Excursion(String ciudad, String descripcion,
                     LocalDateTime fechaComienzo, float cuotaPersona, int numPlazas, LocalDateTime fechaAlta) {
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.fechaComienzo = (fechaComienzo != null) ? fechaComienzo.withNano(0) : null;
        this.cuotaPersona = cuotaPersona;
        this.numPlazas = numPlazas;
        this.plazasDisponibles = numPlazas;
        this.fechaAlta = fechaAlta;
    }

    public Excursion(Long excursionId, String ciudad, String descripcion,
                     LocalDateTime fechaComienzo, float cuotaPersona, int numPlazas) {
        this(ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas);
        this.excursionId = excursionId;
    }
    public Excursion(Long excursionId, String ciudad, String descripcion,
                     LocalDateTime fechaComienzo, float cuotaPersona, int numPlazas, int plazasDisponibles) {
        this(ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas);
        this.plazasDisponibles=plazasDisponibles;
        this.excursionId = excursionId;
    }

    public Excursion(Long excursionId, String ciudad, String descripcion,
                     LocalDateTime fechaComienzo, float cuotaPersona, int numPlazas, LocalDateTime fechaAlta) {
        this(excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas);
        this.fechaAlta = (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    public Long getExcursionId(){ return excursionId;}

    public void setExcursionId(Long excursionId){ this.excursionId=excursionId;}

    public String getCiudad(){ return ciudad;}

    public void setCiudad(String ciudad){ this.ciudad=ciudad;}

    public String getDescripcion(){ return descripcion;}

    public void setDescripcion(String descripcion){ this.descripcion=descripcion;}

    public LocalDateTime getFechaComienzo(){ return fechaComienzo;}

    public void setFechaComienzo(LocalDateTime fechaComienzo){
        this.fechaComienzo = (fechaComienzo != null) ? fechaComienzo.withNano(0) : null;
    }

    public float getCuotaPersona(){ return cuotaPersona;}

    public void setCuotaPersona(float cuotaPersona){ this.cuotaPersona=cuotaPersona;}

    public int getNumPlazas(){ return numPlazas;}

    public void setNumPlazas(int numPlazas){ this.numPlazas=numPlazas;}

    public LocalDateTime getFechaAlta(){ return fechaAlta;}

    public void setFechaAlta(LocalDateTime fechaAlta){
        this.fechaAlta = (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    public int getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(int plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Excursion excursion = (Excursion) o;

        if (Float.compare(excursion.cuotaPersona, cuotaPersona) != 0) return false;
        if (numPlazas != excursion.numPlazas) return false;
        if (plazasDisponibles != excursion.plazasDisponibles) return false;
        if (!excursionId.equals(excursion.excursionId)) return false;
        if (!ciudad.equals(excursion.ciudad)) return false;
        if (!descripcion.equals(excursion.descripcion)) return false;
        if (!fechaComienzo.equals(excursion.fechaComienzo)) return false;
        return fechaAlta.equals(excursion.fechaAlta);
    }

    @Override
    public int hashCode() {
        int result = excursionId.hashCode();
        result = 31 * result + ciudad.hashCode();
        result = 31 * result + descripcion.hashCode();
        result = 31 * result + fechaComienzo.hashCode();
        result = 31 * result + (cuotaPersona != +0.0f ? Float.floatToIntBits(cuotaPersona) : 0);
        result = 31 * result + numPlazas;
        result = 31 * result + plazasDisponibles;
        result = 31 * result + fechaAlta.hashCode();
        return result;
    }
}
