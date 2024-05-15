package es.udc.ws.app.model.excursionservice;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

// Interfaz del servicio del modelo
public interface ExcursionService {

    public Excursion addExcursion(Excursion excursion) throws InputValidationException;

    public void updateExcursion(Excursion excursion) throws InputValidationException,
            InstanceNotFoundException, UpdateExcursionFueraDePlazo, InvalidUpdateDataException, ExcursionFueraDePlazo;

    //public List<Excursion> findByCity(String ciudad) throws InstanceNotFoundException, InputValidationException;

    public List<Excursion> findByCity(String ciudad, LocalDateTime fecha1, LocalDateTime fecha2)
            throws InputValidationException;

    public Reserva addReserva(Long excursionId, String email, int numPlazas, String tarjetaBancaria)
            throws InputValidationException, InstanceNotFoundException, NumPlazasNoDisponible, PlazoDeReservaCerrado;

    public void cancelarReserva(Long reservaId, String email) throws InstanceNotFoundException, InputValidationException, ReservaYaCancelada, CancelationDateTooClose, NumPlazasNoDisponible, PlazoDeReservaCerrado, EmailCancelacionReservaDistinto;

    public  List<Reserva> findbyUser(String emailUsuario) throws InputValidationException;

    public List<Excursion> findByAvailableSlot(int plazasDisponibles, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws InputValidationException;

    public void deleteExcursion(Long excursionId) throws InstanceNotFoundException, InputValidationException, ReservaNoCancelada;
}