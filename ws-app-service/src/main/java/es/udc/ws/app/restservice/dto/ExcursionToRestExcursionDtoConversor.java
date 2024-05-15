package es.udc.ws.app.restservice.dto;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.excursion.Excursion;

public class ExcursionToRestExcursionDtoConversor {
    public static List<RestExcursionDto> toRestExcusionDtos(List<Excursion> excursiones) {
        List<RestExcursionDto> excursionDtos = new ArrayList<>(excursiones.size());
        for (int i = 0; i < excursiones.size(); i++) {
            Excursion excursion = excursiones.get(i);
            excursionDtos.add(toRestExcusrionDto(excursion));
        }
        return excursionDtos;
    }

    public static RestExcursionDto toRestExcusrionDto(Excursion excursion) {
        return new RestExcursionDto(excursion.getExcursionId(), excursion.getCiudad(), excursion.getDescripcion(),
                excursion.getFechaComienzo(), excursion.getCuotaPersona(), excursion.getNumPlazas(), excursion.getPlazasDisponibles());
    }

    public static Excursion toExcursion(RestExcursionDto excursion) {
        return new Excursion(excursion.getExcursionId(), excursion.getCiudad(), excursion.getDescripcion(),
                excursion.getFechaComienzo(), excursion.getCuotaPersona(), excursion.getNumPlazas(),
                excursion.getPlazasDisponibles());
    }
}
