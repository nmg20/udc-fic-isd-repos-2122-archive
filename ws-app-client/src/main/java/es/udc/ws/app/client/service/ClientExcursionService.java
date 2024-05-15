package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Interfaz del servicio del modelo
public interface ClientExcursionService {

    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException;

    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException,
            InstanceNotFoundException, ClientUpdateExcursionFueraDePlazo, ClientInvalidUpdateDataException, ClientExcursionFueraDePlazo;

    public List<ClientExcursionDto> findByCity(String ciudad, LocalDate fecha1, LocalDate fecha2)
            throws InputValidationException;

    public Long addReserva(Long excursionId, String email, int numPlazas, String tarjetaBancaria)
            throws InputValidationException, InstanceNotFoundException, ClientNumPlazasNoDisponible, ClientPlazoDeReservaCerrado;

    public void cancelarReserva(Long reservaId, String email) throws InstanceNotFoundException, InputValidationException,
            ClientReservaYaCancelada, ClientCancelationDateTooClose, ClientNumPlazasNoDisponible, ClientPlazoDeReservaCerrado,
            ClientEmailCancelacionReservaDistinto;

    public  List<ClientReservaDto> findbyUser(String emailUsuario) throws InputValidationException;

    public List<ClientExcursionDto> findByAvailableSlot(int plazasDisponibles, LocalDate fechaInicio) throws InputValidationException;

    public void deleteExcursion(Long excursionId) throws InstanceNotFoundException, InputValidationException, ClientReservaNoCancelada;

}