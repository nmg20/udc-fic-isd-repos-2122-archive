package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.reserva.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaToRestReservaDtoConversor {

    public static List<RestReservaDto> toRestReservaDto(List<Reserva> reservas) {
        List<RestReservaDto> reservaDtos = new ArrayList<>(reservas.size());
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            reservaDtos.add(toRestReservaDto(reserva));
        }
        return reservaDtos;
    }

    public static RestReservaDto toRestReservaDto(Reserva reserva) {
        return new RestReservaDto(reserva.getReservaId(),
                reserva.getExcursionId(),
                reserva.getEmailUsuario(),
                reserva.getNumPlazas(),
                //reserva.getTarjetaBancaria());
                reserva.getTarjetaBancaria().substring(reserva.getTarjetaBancaria().length() - 4),
                reserva.getCosteTotal(),
                reserva.getFechaCancelacion());
    }

    public static Reserva toReserva(RestReservaDto reserva) {
        return new Reserva(reserva.getReservaId(),
                reserva.getExcursionId(),
                reserva.getEmailUsuario(),
                reserva.getNumPlazas(),
                reserva.getTarjetaBancaria(),
                reserva.getFechaCancelacion(),
                reserva.getCosteTotal());
    }

}