package es.udc.ws.app.model.excursion;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlExcursionDao {

    public Excursion create(Connection connection, Excursion excursion);

    public void update(Connection connection, Excursion excursion)
            throws InstanceNotFoundException;

    public Excursion find(Connection connection, Long excursionId)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long excursionId)
            throws InstanceNotFoundException;

    public List<Excursion> findByCity(Connection connection, String ciudad, LocalDateTime fecha1,
            LocalDateTime fecha2);

    public List<Excursion> findByAvailableSlot(Connection connection, int numPlazas, LocalDateTime fechaInicio, LocalDateTime fechaFin);



}
