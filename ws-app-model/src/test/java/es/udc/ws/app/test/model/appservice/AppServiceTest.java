package es.udc.ws.app.test.model.appservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.ExcursionService;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class AppServiceTest {

    private static ExcursionService excursionService = null;
    private static SqlReservaDao reservaDao = null;
    private static SqlExcursionDao excursionDao = null;
    private final long EX_ID_NO_VALIDO = -1;
    private final String CIUDAD_VALIDA = "A Coruña";
    private final String CIUDAD_VALIDA_ALT = "Ferrol";
    private final String CIUDAD_NO_VALIDA = "";
    private final String EMAIL_VALIDO = "holi@yo.com";
    private final String EMAIL_VALIDO_ALT = "user@dominio.com";
    private final String EMAIL_NO_VALIDO = "";
    private final String TB_VALIDA = "1234567890123456";
    private final String TB_NO_VALIDA = "";

    @BeforeAll
    public static void init() {
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        excursionService = ExcursionServiceFactory.getService();
        excursionDao = SqlExcursionDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private Excursion getValidExcursion(String ciudad) {
        return new Excursion(ciudad, "una excursion en "+ciudad, LocalDateTime.now().plusYears(1),
                (float) 22, 30);
    }

    private Excursion getValidExcursion() {
        return getValidExcursion("Coruña");
    }

    public void removeExcursion(Long excursionId) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                excursionDao.remove(connection, excursionId);

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

    public void removeReserva(Long reservaId) throws InstanceNotFoundException{
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                reservaDao.remove(connection, reservaId);
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
    public Excursion addExcursionNotChecked(Excursion excursion) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Excursion createdExcursion = excursionDao.create(connection, excursion);
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

    public Reserva addReservaNotChecked(Reserva reserva) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                Reserva createdReserva = reservaDao.create(connection, reserva);
                connection.commit();
                return createdReserva;
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

    public Excursion findExcursion(Long excursionId) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            return excursionDao.find(connection, excursionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reserva findReserva(Long reservaId) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try (Connection connection = dataSource.getConnection()) {
            return reservaDao.find(connection, reservaId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidacionDatosEntradaExcursion() {

        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = new Excursion("", "Descripcion", LocalDateTime.now().plusDays(5),
                    (float)15, 40);
            excursionService.addExcursion(excursion);
        });

        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = new Excursion("Ciudad", "", LocalDateTime.now().plusDays(5),
                    (float)15, 40);
            excursionService.addExcursion(excursion);
        });

        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = new Excursion("Ciudad", "Descripcion", LocalDateTime.now().plusDays(5),
                    (float)401, 40);
            excursionService.addExcursion(excursion);
        });

        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = new Excursion("Ciudad", "Descripcion", LocalDateTime.now().plusDays(5),
                    (float)40, -40);
            excursionService.addExcursion(excursion);
        });
    }

    @Test
    public void testValidacionDatosEntradaReserva() throws ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException {

        Excursion excursion = new Excursion("Sevilla", "Excursion por Sevilla", LocalDateTime.now().plusHours(80),
                (float)15, 40);
        Excursion excursionFound = excursionService.addExcursion(excursion);

        assertThrows(InputValidationException.class, () -> {
            excursionService.addReserva(excursion.getExcursionId(), "", 3, TB_VALIDA);
        });

        assertThrows(InputValidationException.class, () -> {
            excursionService.addReserva(excursion.getExcursionId(), "algo@algo.es", -3, TB_VALIDA);
        });

        assertThrows(InputValidationException.class, () -> {
            excursionService.addReserva(excursion.getExcursionId(), "algo@algo.es", 3, TB_NO_VALIDA);
        });

        removeExcursion(excursionFound.getExcursionId());
    }

    @Test
    public void testAddExcursionAndFindExcursion() throws InputValidationException, InstanceNotFoundException {

        Excursion excursion = getValidExcursion();
        Excursion addedExcursion = null;

        // Create Excursion
        LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

        addedExcursion = excursionService.addExcursion(excursion);

        LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

        // Find Excursion
        Excursion foundExcursion = findExcursion(addedExcursion.getExcursionId());

        assertEquals(addedExcursion, foundExcursion);
        assertEquals(foundExcursion.getCiudad(),excursion.getCiudad());
        assertEquals(foundExcursion.getDescripcion(),excursion.getDescripcion());
        assertEquals(foundExcursion.getFechaComienzo(),excursion.getFechaComienzo());
        assertEquals(foundExcursion.getCuotaPersona(),excursion.getCuotaPersona());
        assertEquals(foundExcursion.getNumPlazas(),excursion.getNumPlazas());
        assertEquals(foundExcursion.getPlazasDisponibles(),excursion.getPlazasDisponibles());
        assertTrue((foundExcursion.getFechaAlta().compareTo(beforeCreationDate) >= 0)
                && (foundExcursion.getFechaAlta().compareTo(afterCreationDate) <= 0));
        removeExcursion(addedExcursion.getExcursionId());

    }

    @Test
    public void testAddExcursionFueraDePlazo() throws ExcursionFueraDePlazo {

        Excursion excursionFueraDePlazo = new Excursion("coruña", "una excursion en coruña", LocalDateTime.now().plusHours(20),
                (float) 20, 20);

        assertThrows(InputValidationException.class, () -> {
            excursionService.addExcursion(excursionFueraDePlazo);
        });
    }

    @Test
    public void testUpdateExcursionCambiarDescripcion() throws UpdateExcursionFueraDePlazo, ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException, InvalidUpdateDataException {

        Excursion addedExcursion = null;
        Excursion excursion = new Excursion("Leon", "Compuesta por 3 rutas de montaña y 2 por un pueblo.", LocalDateTime.now().plusDays(5),
                (float)15, 40);
        addedExcursion = excursionService.addExcursion(excursion);

        // Se le añaden datos a la descripcion.
        Excursion excursionToUpdate = new Excursion(addedExcursion.getExcursionId(), addedExcursion.getCiudad(), addedExcursion.getDescripcion()+" Descanso entre rutas en manantial.", addedExcursion.getFechaComienzo(),
                (float)10, 40);

        excursionService.updateExcursion(excursionToUpdate);

        Excursion updatedExcursion = findExcursion(addedExcursion.getExcursionId());

        excursionToUpdate.setFechaAlta(addedExcursion.getFechaAlta());
        assertEquals(excursionToUpdate, updatedExcursion);
        removeExcursion(excursionToUpdate.getExcursionId());
    }

    @Test
    public void testUpdateExcursionCambiarNumPlazas() throws InputValidationException, InstanceNotFoundException, NumPlazasNoDisponible, PlazoDeReservaCerrado {

        // create
        Excursion addedExcursion = null;
        Excursion excursion = new Excursion("Bilbao", "Descripcion de excursion en Bilbao", LocalDateTime.now().plusHours(80),
                (float)10, 40);
        addedExcursion = excursionService.addExcursion(excursion);

        // 5 reservas
        Reserva reserva = excursionService.addReserva(addedExcursion.getExcursionId(), EMAIL_VALIDO_ALT, 5, TB_VALIDA);

        Excursion excursionToUpdate = new Excursion(addedExcursion.getExcursionId(), "nueva ciudad", "nueva descripcion", addedExcursion.getFechaComienzo(),
                (float)10, 4);

        assertThrows(InputValidationException.class, () -> {
            excursionService.updateExcursion(excursionToUpdate);
        });
        removeExcursion(excursionToUpdate.getExcursionId());
    }

    @Test
    public void testUpdateExcursionFechaComienzo() throws UpdateExcursionFueraDePlazo, ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException, InvalidUpdateDataException {

        Excursion addedExcursion = null;
        Excursion excursion = new Excursion("ciudad", "descripcion", LocalDateTime.now().plusHours(73),
                (float)15, 40);
        addedExcursion = excursionService.addExcursion(excursion);

        // Modificada cuota por persona
        Excursion excursionToUpdate = new Excursion(addedExcursion.getExcursionId(), "nueva ciudad", "nueva descripcion", addedExcursion.getFechaComienzo().minusHours(10),
                (float)15, 40);

        assertThrows(InvalidUpdateDataException.class, () -> {
            excursionService.updateExcursion(excursionToUpdate);
        });
        removeExcursion(addedExcursion.getExcursionId());
    }

    @Test
    public void testUpdateExcursion72hAfter() throws ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException, UpdateExcursionFueraDePlazo, InvalidUpdateDataException {

        Excursion addedExcursion = null;
        Excursion excursion = new Excursion("Sevilla", "Excursion por Sevilla", LocalDateTime.now().plusHours(80),
                (float)15, 40);
        addedExcursion = excursionService.addExcursion(excursion);

        // Modificada cuota por persona
        Excursion excursionToUpdate = new Excursion(addedExcursion.getExcursionId(), addedExcursion.getCiudad(), addedExcursion.getDescripcion(), addedExcursion.getFechaComienzo(),
                (float)10, 40);

        excursionService.updateExcursion(excursionToUpdate);

        Excursion updatedExcursion = findExcursion(addedExcursion.getExcursionId());

        excursionToUpdate.setFechaAlta(addedExcursion.getFechaAlta());
        assertEquals(excursionToUpdate, updatedExcursion);
        removeExcursion(updatedExcursion.getExcursionId());
    }

    @Test
    public void testFindExcursion() throws InstanceNotFoundException, ExcursionFueraDePlazo, InputValidationException {
        Excursion excursion = excursionService.addExcursion(new Excursion(CIUDAD_VALIDA, "una excursion en coruña",
                LocalDateTime.now().plusMonths(1), (float) 15, 60));
        Excursion foundExcursion = findExcursion(excursion.getExcursionId());
        assertEquals(excursion, foundExcursion);
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testFindNonExistentExcursion() throws InstanceNotFoundException {
        assertThrows(InstanceNotFoundException.class, () -> {
            findExcursion(EX_ID_NO_VALIDO);
        });
    }

    @Test
    public void testFindExcursionByCity() throws InstanceNotFoundException, ExcursionFueraDePlazo, InputValidationException {
        //Se crea una lista con tres excursiones válidas con misma ciudad pero distintos datos
        List<Excursion> excursiones = new LinkedList<Excursion>();
        Excursion excursion1 = excursionService.addExcursion(new Excursion(CIUDAD_VALIDA_ALT, "una excursion en "+CIUDAD_VALIDA_ALT,
                LocalDateTime.now().plusDays(15), (float) 40, 50));
        excursiones.add(excursion1);
        Excursion excursion2 = excursionService.addExcursion(new Excursion(CIUDAD_VALIDA_ALT, "una excursion en "+CIUDAD_VALIDA_ALT,
                LocalDateTime.now().plusDays(25), (float) 30, 70));
        excursiones.add(excursion2);
        Excursion excursion3 = excursionService.addExcursion(new Excursion(CIUDAD_VALIDA_ALT, "una excursion en "+CIUDAD_VALIDA_ALT,
                LocalDateTime.now().plusDays(30), (float) 45, 40));
        excursiones.add(excursion3);

        //Se encuentran todas las excuriones añadidas
        List<Excursion> foundExcursiones = excursionService.findByCity(CIUDAD_VALIDA_ALT,LocalDateTime.now().plusDays(14),LocalDateTime.now().plusDays(31));
        assertEquals(excursiones, foundExcursiones);


        //Se encuentran las excursiones dentro del rango dentro de 20 y 30 días
        foundExcursiones = excursionService.findByCity(CIUDAD_VALIDA_ALT,LocalDateTime.now().plusDays(20),LocalDateTime.now().plusDays(30));
        assertEquals(2, foundExcursiones.size());
        assertEquals(excursiones.get(1), foundExcursiones.get(0));
        assertEquals(excursiones.get(2), foundExcursiones.get(1));

        //No se encuentra ninguna excursión con un nombre de ciudad no válido
        foundExcursiones = excursionService.findByCity("Madrid",null,null);
        assertEquals(0, foundExcursiones.size());
        for (Excursion ex : excursiones) {
            removeExcursion(ex.getExcursionId());
        }
    }

    @Test
    public void testFindExcursionByCityInvalidRange() throws InstanceNotFoundException {
        assertThrows(RuntimeException.class, () -> {
            LocalDateTime fecha = LocalDateTime.now();
            excursionService.findByCity(getValidExcursion().getCiudad(), fecha, fecha.minusDays(2));
        });
    }

    @Test
    public void testReservarExcursionAndFindReserva() throws InstanceNotFoundException,
            ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        Reserva reserva = null;
        try{
            //Reservar una excursión
            LocalDateTime fechaPreReserva = LocalDateTime.now().withNano(0);
            reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO_ALT, 3, TB_VALIDA);
            LocalDateTime fechaPostReserva = LocalDateTime.now().withNano(0);

            Reserva foundReserva = findReserva(reserva.getReservaId());

            //Comprobar la reserva encontrada
            assertEquals(reserva, foundReserva);
            assertEquals(reserva.getReservaId(), foundReserva.getReservaId());
            assertEquals(reserva.getExcursionId(), foundReserva.getExcursionId());
            assertEquals(EMAIL_VALIDO_ALT, foundReserva.getEmailUsuario());
            assertEquals(reserva.getNumPlazas(), foundReserva.getNumPlazas());
            assertEquals(TB_VALIDA, foundReserva.getTarjetaBancaria());
            assertTrue((foundReserva.getFechaReserva().compareTo(fechaPreReserva) >= 0)
                    && (foundReserva.getFechaReserva().compareTo(fechaPostReserva) <= 0));


        } catch (InstanceNotFoundException | InputValidationException e) {
            e.printStackTrace();
        } finally {
            removeReserva(reserva.getReservaId());
            removeExcursion(excursion.getExcursionId());
        }
    }

    @Test
    public void testReservarExcursionEmailNoValido() throws ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException {
        Excursion excursion = excursionService.addExcursion(getValidExcursion(CIUDAD_VALIDA));
        assertThrows(InputValidationException.class, () -> excursionService.addReserva(excursion.getExcursionId(), EMAIL_NO_VALIDO, 3, TB_VALIDA));
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testReservarExcursionPlazasNoDisponibles() throws ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException, NumPlazasNoDisponible, PlazoDeReservaCerrado {
        Excursion excursion = new Excursion("Barcelona", "Excursion por Barcelona", LocalDateTime.now().plusHours(80),
                (float)15, 10);

        excursion = excursionService.addExcursion(excursion);
        Excursion finalExcursion = excursion;

        // HACER 7 RESERVAS PARA QUE QUEDEN 3

        excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 5, TB_VALIDA);
        excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 2, TB_VALIDA);

        assertThrows(NumPlazasNoDisponible.class, () -> excursionService.addReserva(finalExcursion.getExcursionId(), EMAIL_VALIDO, 4, TB_VALIDA));
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testReservarExcursionPlazasFueraDeRango() throws InstanceNotFoundException, ExcursionFueraDePlazo, InputValidationException {
        Excursion excursion = excursionService.addExcursion(getValidExcursion(CIUDAD_VALIDA));
        try {
            assertThrows(InputValidationException.class, () -> excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 6, TB_VALIDA));
        } finally {
            removeExcursion(excursion.getExcursionId());
        }
    }

    @Test
    public void testReservarExcursionTBNoValida() throws InstanceNotFoundException, ExcursionFueraDePlazo, InputValidationException {
        Excursion excursion = excursionService.addExcursion(getValidExcursion(CIUDAD_VALIDA));
        try {
            assertThrows(InputValidationException.class, () -> excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_NO_VALIDA));
        } finally {
            removeExcursion(excursion.getExcursionId());
        }
    }

    @Test
    public void testReservaFueraDePlazo() throws PlazoDeReservaCerrado, InstanceNotFoundException {
        Excursion excursion = new Excursion(CIUDAD_VALIDA, "una excursion en "+CIUDAD_VALIDA,
                LocalDateTime.now().plusHours(20),
                (float) 22, 30, LocalDateTime.now());
        Excursion excursion1 = addExcursionNotChecked(excursion);
        try{
            assertThrows(PlazoDeReservaCerrado.class, () -> excursionService.addReserva(excursion1.getExcursionId(),
                    EMAIL_VALIDO, 3, TB_VALIDA));
        } finally {
            removeExcursion(excursion1.getExcursionId());
        }
    }

    @Test
    public void testRemoveReserva() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException{
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        Reserva reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);
        Reserva foundReserva = findReserva(reserva.getReservaId());
        removeReserva(foundReserva.getReservaId());
        assertThrows(InstanceNotFoundException.class, ()-> findReserva(foundReserva.getReservaId()));
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testCancelarReserva() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException, ReservaYaCancelada, CancelationDateTooClose, EmailCancelacionReservaDistinto {
        Reserva reserva, reserva2 = null;
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);

        assertEquals(27, findExcursion(excursion.getExcursionId()).getPlazasDisponibles());

        Reserva foundReserva = findReserva(reserva.getReservaId());
        LocalDateTime fechaPreCancelacionReserva = LocalDateTime.now().withNano(0);
        excursionService.cancelarReserva(foundReserva.getReservaId(), reserva.getEmailUsuario());
        LocalDateTime fechaPostCancelacionReserva = LocalDateTime.now().withNano(0);

        assertEquals(excursion.getPlazasDisponibles(), findExcursion(excursion.getExcursionId()).getPlazasDisponibles());

        // Se comprueba que la fecha de cancelacion sea justo la anterior
        foundReserva = findReserva(reserva.getReservaId());
        assertTrue((foundReserva.getFechaCancelacion().compareTo(fechaPreCancelacionReserva) >= 0)
                && (foundReserva.getFechaReserva().compareTo(fechaPostCancelacionReserva) <= 0));
        removeReserva(reserva.getReservaId());
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testCancelarReservaEmailDistinto() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException {
        Reserva reserva = null;
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);

        // Se intenta cancelar con un email distinto al registrado
        Reserva foundReserva = findReserva(reserva.getReservaId());
        assertThrows(EmailCancelacionReservaDistinto.class, () -> excursionService.cancelarReserva(foundReserva.getReservaId(), "inventado@udc.es"));
        removeReserva(reserva.getReservaId());
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testCancelarReservaYaCancelada() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException, ReservaYaCancelada, CancelationDateTooClose, EmailCancelacionReservaDistinto {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        Reserva reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);

        // Se intenta cancelar otra vez
        excursionService.cancelarReserva(reserva.getReservaId(), reserva.getEmailUsuario());
        Reserva foundReserva = findReserva(reserva.getReservaId());
        assertThrows(ReservaYaCancelada.class, () -> excursionService.cancelarReserva(foundReserva.getReservaId(), foundReserva.getEmailUsuario()));
        removeReserva(reserva.getReservaId());
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testCancelarReservaQueNoExiste() throws ExcursionFueraDePlazo, InputValidationException, InstanceNotFoundException {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        // Se intenta cancelar reserva con id no existente
        assertThrows(InstanceNotFoundException.class, () -> excursionService.cancelarReserva((long)100, "alguno@udc.es"));
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testCancelarReservaDateTooClose() throws InputValidationException, InstanceNotFoundException, NumPlazasNoDisponible, PlazoDeReservaCerrado {
        Excursion excursion = new Excursion(CIUDAD_VALIDA, "una excursion en "+CIUDAD_VALIDA,
                LocalDateTime.now().plusHours(20),
                (float) 22, 30, LocalDateTime.now());
        excursion = addExcursionNotChecked(excursion);
        Reserva reserva = new Reserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA, LocalDateTime.now(), excursion.getCuotaPersona()*3);
        Reserva createdReserva = addReservaNotChecked(reserva);
        // Se intenta cancelar reserva con id no existente
        // CancelationDateTooClose
        assertThrows(CancelationDateTooClose.class, () -> excursionService.cancelarReserva(createdReserva.getReservaId(), "alguno@udc.es"));
        removeReserva(createdReserva.getReservaId());
        removeExcursion(excursion.getExcursionId());
    }

    @Test
    public void testFindbyUser() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        Excursion excursion1 = getValidExcursion(CIUDAD_VALIDA_ALT);
        excursion = excursionService.addExcursion(excursion);
        excursion1 = excursionService.addExcursion(excursion1);
        Reserva reserva = null;
        Reserva reserva1 = null;
        List<Reserva> foundReserva = null;
        try{
            //Reservar una excursión
            reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);
            reserva1 = excursionService.addReserva(excursion1.getExcursionId(), EMAIL_VALIDO, 4, TB_VALIDA);

            foundReserva = excursionService.findbyUser(reserva.getEmailUsuario());

            //Comprobar la reserva encontrada
            assertEquals(reserva, foundReserva.get(0));
            assertEquals(reserva.getReservaId(), foundReserva.get(0).getReservaId());
            assertEquals(reserva.getExcursionId(), foundReserva.get(0).getExcursionId());
            assertEquals(EMAIL_VALIDO, foundReserva.get(0).getEmailUsuario());
            assertEquals(reserva.getNumPlazas(), foundReserva.get(0).getNumPlazas());
            assertEquals(TB_VALIDA, foundReserva.get(0).getTarjetaBancaria());

            assertEquals(reserva1, foundReserva.get(1));
            assertEquals(reserva1.getReservaId(), foundReserva.get(1).getReservaId());
            assertEquals(reserva1.getExcursionId(), foundReserva.get(1).getExcursionId());
            assertEquals(EMAIL_VALIDO, foundReserva.get(1).getEmailUsuario());
            assertEquals(reserva1.getNumPlazas(), foundReserva.get(1).getNumPlazas());
            assertEquals(TB_VALIDA, foundReserva.get(1).getTarjetaBancaria());

        }catch(InstanceNotFoundException | InputValidationException e) {
            e.printStackTrace();
        }finally{
            removeReserva(reserva.getReservaId());
            removeReserva(reserva1.getReservaId());
            removeExcursion(excursion.getExcursionId());
            removeExcursion(excursion1.getExcursionId());
        }
    }

    @Test
    public void testFindbyUserNoResults() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        Excursion excursion1 = getValidExcursion(CIUDAD_VALIDA_ALT);
        excursion = excursionService.addExcursion(excursion);
        excursion1 = excursionService.addExcursion(excursion1);
        Reserva reserva = null;
        Reserva reserva1 = null;
        List<Reserva> foundReserva = null;

        //Reservar una excursión
        reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);
        reserva1 = excursionService.addReserva(excursion1.getExcursionId(), EMAIL_VALIDO, 4, TB_VALIDA);

        List<Reserva> listaReservasVacia = new ArrayList<>();

        // No encontrara ninguna de las 2 reservas porque el correo es distinto
        assertEquals(listaReservasVacia, excursionService.findbyUser("correo@udc.es"));
        removeReserva(reserva.getReservaId());
        removeReserva(reserva1.getReservaId());
        removeExcursion(excursion.getExcursionId());
        removeExcursion(excursion1.getExcursionId());
    }
/*
    @Test
    public void testFindbyUserCancelada() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException, CancelationDateTooClose, EmailCancelacionReservaDistinto, ReservaYaCancelada {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        excursion = excursionService.addExcursion(excursion);
        Reserva reserva = null;
        List<Reserva> foundReserva = null;

        //Reservar una excursión
        reserva = excursionService.addReserva(excursion.getExcursionId(), EMAIL_VALIDO, 3, TB_VALIDA);

        //Cancelarla
        Reserva res = findReserva(reserva.getReservaId());
        excursionService.cancelarReserva(res.getReservaId(), reserva.getEmailUsuario());

        foundReserva = excursionService.findbyUser("holi@yo.com");

        assertEquals(res,foundReserva.get(0));
        removeReserva(reserva.getReservaId());
        removeExcursion(excursion.getExcursionId());
    }
*/
    @Test
    public void testFindbyUserDosCorreosDistintos() throws ExcursionFueraDePlazo, InputValidationException, NumPlazasNoDisponible, PlazoDeReservaCerrado, InstanceNotFoundException {
        Excursion excursion = getValidExcursion(CIUDAD_VALIDA);
        Excursion excursion1 = getValidExcursion(CIUDAD_VALIDA_ALT);
        excursion = excursionService.addExcursion(excursion);
        excursion1 = excursionService.addExcursion(excursion1);
        Reserva reserva = null;
        Reserva reserva1 = null;
        List<Reserva> foundReserva = null;

        // Dos reservas de 2 usuarios distintos
        reserva = excursionService.addReserva(excursion.getExcursionId(), "correo1@udc.es", 3, TB_VALIDA);
        reserva1 = excursionService.addReserva(excursion1.getExcursionId(), "correo2@udc.es", 4, TB_VALIDA);

        List<Reserva> listaReservas = new ArrayList<>();
        listaReservas.add(reserva);
        listaReservas.add(reserva1);

        // Busca por un correo y luego por el otro
        assertEquals(listaReservas.get(0), excursionService.findbyUser("correo1@udc.es").get(0));
        assertEquals(listaReservas.get(1), excursionService.findbyUser("correo2@udc.es").get(0));
        removeReserva(reserva.getReservaId());
        removeReserva(reserva1.getReservaId());
        removeExcursion(excursion.getExcursionId());
        removeExcursion(excursion1.getExcursionId());
    }

    @Test
    public void testFindbyUserCorreoInvalido(){
        assertThrows(InputValidationException.class, () -> excursionService.findbyUser(""));
    }

    @Test
    public void testFindbyUserNoSeEncuentranReservas() throws InputValidationException {
        assertEquals(0,excursionService.findbyUser("correo1@udc.es").size());
    }

}

