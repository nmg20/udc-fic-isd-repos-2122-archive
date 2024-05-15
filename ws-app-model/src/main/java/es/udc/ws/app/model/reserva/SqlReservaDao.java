package es.udc.ws.app.model.reserva;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlReservaDao {
    public Reserva create(Connection connection, Reserva reserva);

    public void cancelar(Connection connection, Reserva reservaId) throws InstanceNotFoundException;

    public Reserva find(Connection connection, Long reservaId) throws InstanceNotFoundException;

    public List<Reserva> findbyUser(Connection connection, String emailUsuario) throws InputValidationException;

    public void remove(Connection connection, Long reservaId) throws InstanceNotFoundException;

    public List<Reserva> findByExcursionId(Connection connection, Long excursionId) throws InputValidationException;
}
