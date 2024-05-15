package es.udc.ws.app.model.excursionservice.exceptions;

public class NumPlazasNoDisponible extends Exception{

    private int numPlazas;
    private int plazasDisponibles;

    public NumPlazasNoDisponible(int numPlazas, int plazasDisponibles) {
        super("No se pueden reservar " + numPlazas + " plazas, el número de plazas restantes para esta excursión es de"
                + plazasDisponibles + ".");
        this.numPlazas = numPlazas;
        this.plazasDisponibles = plazasDisponibles;
    }

    public int getNumPlazas() {
        return numPlazas;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
    }

    public int getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(int plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }
}
