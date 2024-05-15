package es.udc.ws.app.model.excursionservice;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;


public class ExcursionServiceImpl implements ExcursionService{
    /*
     * IMPORTANT: Some JDBC drivers require "setTransactionIsolation" to be called
     * before "setAutoCommit".
     */

    private final DataSource dataSource;
    private SqlExcursionDao excursionDao = null;
    private SqlReservaDao reservaDao = null;

    public ExcursionServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        excursionDao = SqlExcursionDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private void validateExcursion(Excursion excursion) throws InputValidationException {

        PropertyValidator.validateMandatoryString("ciudad", excursion.getCiudad());
        PropertyValidator.validateMandatoryString("descripcion", excursion.getDescripcion());
        PropertyValidator.validateDouble("cuotaPersona", excursion.getCuotaPersona(), 0, 400);
        PropertyValidator.validateNotNegativeLong("numPlazas", excursion.getNumPlazas());
        PropertyValidator.validateNotNegativeLong("plazasDisponibles", excursion.getPlazasDisponibles());

        if(excursion.getNumPlazas() < 1){
            throw new InputValidationException("El numPlazas debe ser > 0");
        }
    }

    private void validateAddExcursion(Excursion excursion) throws InputValidationException {

        if(excursion.getFechaComienzo().isBefore(excursion.getFechaAlta().plusHours(72))){
            throw new InputValidationException("Excursión fuera de plazo");
        }
    }

    private void validateReserva(String email, int numPlazas, String tarjetaBancaria) throws InputValidationException {

        PropertyValidator.validateMandatoryString("email", email);
        PropertyValidator.validateNotNegativeLong("numPlazas", numPlazas);
        PropertyValidator.validateCreditCard(tarjetaBancaria);
    }

    private void validateAddReserva(int numPlazas) throws InputValidationException {

        if ((numPlazas < 1) || (numPlazas > 5))
            throw new InputValidationException("Número de plazas fuera de rango");
    }

    private void validateCity(String ciudad) throws InputValidationException {
        PropertyValidator.validateMandatoryString("ciudad", ciudad);
    }

    private void validateFindByCity(LocalDateTime fecha1, LocalDateTime fecha2) throws RuntimeException {
        if ((fecha1 != null) && (fecha2 != null)) {
            if (fecha2.isBefore(fecha1)) {
                throw new RuntimeException("Rango de fechas no válido: La segunda fecha debe ser posterior a la primera.");
            }
        }
    }

    private void validateEmail(String email) throws InputValidationException {
        PropertyValidator.validateMandatoryString("email", email);
    }

    private void validatePlazasDisponibles(int plazasDisponibles) throws InputValidationException {
        if(plazasDisponibles < 1){
            throw new InputValidationException("Número incorrecto de plazas libres");
        }
    }

    private void validatefindByAvailableSlot(LocalDateTime fecha1, LocalDateTime fecha2){
        if ((fecha1 != null) && (fecha2 != null)) {
            if (fecha2.isBefore(fecha1)) {
                throw new RuntimeException("Rango de fechas no válido: La segunda fecha debe ser posterior a la primera.");
            }
        }

        if(fecha1.isBefore(LocalDateTime.now())){
            throw new RuntimeException("La fecha debe ser futura.");
        }
    }

    @Override
    public Excursion addExcursion(Excursion excursion) throws InputValidationException {

        excursion.setFechaAlta(LocalDateTime.now().withNano(0));
        excursion.setPlazasDisponibles(excursion.getNumPlazas());
        validateExcursion(excursion);
        validateAddExcursion(excursion);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Excursion createdExcursion = excursionDao.create(connection, excursion);

                /* Commit. */
                connection.commit();

                return createdExcursion;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateExcursion(Excursion excursion) throws InputValidationException, InstanceNotFoundException,
            UpdateExcursionFueraDePlazo, InvalidUpdateDataException{

        validateExcursion(excursion);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Excursion excursion_aux = excursionDao.find(connection, excursion.getExcursionId());

                if (LocalDateTime.now().plusHours(72).isAfter(excursion_aux.getFechaComienzo())) {
                    throw new UpdateExcursionFueraDePlazo(excursion.getExcursionId());
                }

                if (excursion.getFechaComienzo().isBefore(excursion_aux.getFechaComienzo())){
                    throw new InvalidUpdateDataException(excursion.getExcursionId());
                }

                if(excursion.getNumPlazas() < (excursion_aux.getNumPlazas() - excursion_aux.getPlazasDisponibles())) {
                    throw new InputValidationException("El numero de plazas debe ser > "+ (excursion_aux.getNumPlazas() - excursion_aux.getPlazasDisponibles()));
                }
                excursion.setPlazasDisponibles(excursion.getNumPlazas()-(excursion_aux.getNumPlazas()-excursion_aux.getPlazasDisponibles()));
                excursion.setFechaAlta(excursion_aux.getFechaAlta().withNano(0));

                /* Do work. */
                excursionDao.update(connection, excursion);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Excursion> findByCity(String ciudad, LocalDateTime fecha1, LocalDateTime fecha2)
            throws InputValidationException {
        validateCity(ciudad);
        validateFindByCity(fecha1, fecha2);
        try (Connection connection = dataSource.getConnection()) {
            return excursionDao.findByCity(connection, ciudad, fecha1, fecha2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reserva addReserva(Long excursionId, String email, int numPlazas, String tarjetaBancaria)
            throws InputValidationException, InstanceNotFoundException, NumPlazasNoDisponible, PlazoDeReservaCerrado {

        validateReserva(email, numPlazas, tarjetaBancaria);
        validateAddReserva(numPlazas);

        try(Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Excursion excursion = excursionDao.find(connection, excursionId);
                LocalDateTime limit = excursion.getFechaComienzo().minusHours(24);

                if (LocalDateTime.now().isAfter(limit))
                    throw new PlazoDeReservaCerrado(excursionId);

                if (numPlazas > excursion.getPlazasDisponibles())
                    throw new NumPlazasNoDisponible(numPlazas,excursion.getPlazasDisponibles());

                excursion.setPlazasDisponibles(excursion.getPlazasDisponibles()-numPlazas);
                excursionDao.update(connection,excursion);

                Reserva reserva = new Reserva(excursionId, email, numPlazas,
                        tarjetaBancaria, LocalDateTime.now(), numPlazas*excursion.getCuotaPersona());

                reserva.setFechaCancelacion(null);

                reserva = reservaDao.create(connection, reserva);
                connection.commit();
                return reserva;
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> findbyUser(String emailUsuario) throws InputValidationException {
        validateEmail(emailUsuario);
        try (Connection connection = dataSource.getConnection()) {
            return reservaDao.findbyUser(connection, emailUsuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Excursion> findByAvailableSlot(int plazasDisponibles, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws InputValidationException {

        validatePlazasDisponibles(plazasDisponibles);
        validatefindByAvailableSlot(fechaInicio, fechaFin);

        try (Connection connection = dataSource.getConnection()){
            return excursionDao.findByAvailableSlot(connection, plazasDisponibles, fechaInicio, fechaFin);

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteExcursion(Long excursionId) throws InstanceNotFoundException, InputValidationException,ReservaNoCancelada  {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {

            int cancelada = 0;
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                List<Reserva> foundReserva = reservaDao.findByExcursionId(connection, excursionId);
                Excursion excursion = excursionDao.find(connection, excursionId);
                LocalDateTime fechaComienzo = excursion.getFechaComienzo();


                if(fechaComienzo.isBefore(LocalDateTime.now())){
                    excursionDao.remove(connection, excursionId);

                }


                if(fechaComienzo.isAfter(LocalDateTime.now()) && foundReserva.isEmpty() ){
                    excursionDao.remove(connection, excursionId);


                }

                if(fechaComienzo.isAfter(LocalDateTime.now()) && !foundReserva.isEmpty()){

                    for(int i=0; i< foundReserva.size(); i++){

                        if(foundReserva.get(i).getFechaCancelacion()!= null && foundReserva.get(i).getFechaCancelacion().isBefore(LocalDateTime.now())){

                            cancelada++;
                        }
                    }

                    if(cancelada == foundReserva.size()){
                        excursionDao.remove(connection, excursionId);

                    }else{
                        throw new ReservaNoCancelada(excursionId);
                    }

                }

                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelarReserva(Long reservaId, String email) throws InstanceNotFoundException, InputValidationException, ReservaYaCancelada, CancelationDateTooClose, EmailCancelacionReservaDistinto {

        validateEmail(email);

        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Reserva foundReserva = reservaDao.find(connection,reservaId);
                Excursion excursion = excursionDao.find(connection, foundReserva.getExcursionId());
                LocalDateTime fechaComienzo = excursion.getFechaComienzo();

                if(foundReserva.getFechaCancelacion() != null){
                    throw new ReservaYaCancelada(reservaId);
                }

                if (LocalDateTime.now().plus(48, ChronoUnit.HOURS).isAfter(fechaComienzo)) {
                    throw new CancelationDateTooClose(reservaId);
                }

                if(!email.equals(foundReserva.getEmailUsuario())){
                    throw new EmailCancelacionReservaDistinto(reservaId,email);
                }
                /* Do work. */
                excursion.setPlazasDisponibles(excursion.getPlazasDisponibles()+reservaDao.find(connection, reservaId).getNumPlazas());
                excursionDao.update(connection,excursion);

                foundReserva.setFechaCancelacion(LocalDateTime.now());
                reservaDao.cancelar(connection, foundReserva);

                /* Commit. */
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
