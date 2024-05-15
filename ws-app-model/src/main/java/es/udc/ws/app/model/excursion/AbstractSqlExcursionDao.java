package es.udc.ws.app.model.excursion;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlExcursionDao implements SqlExcursionDao {
    private Object Excursion;

    protected AbstractSqlExcursionDao(){}

    @Override
    public Excursion find(Connection connection, Long excursionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT ciudad, descripcion, "
                + " fechaComienzo, cuotaPersona, numPlazas, plazasDisponibles, fechaAlta FROM Excursion WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(excursionId,
                        Excursion.class.getName());
            }

            /* Get results. */
            i = 1;
            String ciudad = resultSet.getString(i++);
            String descripcion = resultSet.getString(i++);
            Timestamp fechaComienzoAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaComienzo = fechaComienzoAsTimestamp.toLocalDateTime();
            float cuotaPersona = resultSet.getShort(i++);
            int numPlazas = resultSet.getInt(i++);
            int plazasDisponibles = resultSet.getInt(i++);
            Timestamp fechaAltaAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaAlta = fechaAltaAsTimestamp.toLocalDateTime();

            Excursion excursion = new Excursion(excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona,
                    numPlazas, fechaAlta);
            excursion.setPlazasDisponibles(plazasDisponibles);

            /* Return excursion. */
            return excursion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Excursion excursion)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Excursion"
                + " SET ciudad = ?, descripcion = ?, fechaComienzo = ?, "
                + "cuotaPersona = ?, numPlazas = ?, plazasDisponibles = ?, fechaAlta = ? WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, excursion.getCiudad());
            preparedStatement.setString(i++, excursion.getDescripcion());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaComienzo()));
            preparedStatement.setFloat(i++, excursion.getCuotaPersona());
            preparedStatement.setInt(i++, excursion.getNumPlazas());
            preparedStatement.setInt(i++, excursion.getPlazasDisponibles());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaAlta()));
            preparedStatement.setLong(i++, excursion.getExcursionId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(excursion.getExcursionId(),
                        Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Excursion> findByCity(Connection connection, String ciudad, LocalDateTime fecha1,
                                      LocalDateTime fecha2) {
        String query = "SELECT excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas, plazasDisponibles, fechaAlta" +
                " FROM Excursion WHERE ciudad=? AND plazasDisponibles > 0";
        if((fecha1!=null) && (fecha2!=null))
            query = query + " AND fechaComienzo > ? AND fechaComienzo < ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, ciudad);
            if((fecha1!=null) && (fecha2!=null)) {
                preparedStatement.setTimestamp(2, Timestamp.valueOf(fecha1));
                preparedStatement.setTimestamp(3, Timestamp.valueOf(fecha2));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Excursion> excursiones = new ArrayList<>();
            while (resultSet.next()){
                int i = 1;
                Long excursionId = resultSet.getLong(i++);
                String ciudad2 = resultSet.getString(i++);
                String descripcion = resultSet.getString(i++);
                Timestamp fechaComienzoTS = resultSet.getTimestamp(i++);
                LocalDateTime fechaComienzo = fechaComienzoTS.toLocalDateTime();
                float cuotaPersona = resultSet.getFloat(i++);
                int numPlazas = resultSet.getInt(i++);
                int plazasDisponibles = resultSet.getInt(i++);
                Timestamp fechaAltaTS = resultSet.getTimestamp(i++);
                LocalDateTime fechaAlta = fechaAltaTS.toLocalDateTime();

                Excursion excursion = new Excursion(excursionId, ciudad2, descripcion, fechaComienzo, cuotaPersona,
                        numPlazas, fechaAlta);
                excursion.setPlazasDisponibles(plazasDisponibles);

                excursiones.add(excursion);
            }
            return excursiones;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long excursionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Excursion WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId.longValue());

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(excursionId,
                        Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public List<Excursion> findByAvailableSlot(Connection connection, int plazasDisponibles, LocalDateTime fechaInicio, LocalDateTime fechaFin){


        /*En la capa Modelo debe añadirse una funcionalidad que permita obtener las excursiones que aún
        se puedan reservar en las que queden menos plazas libres que un cierto número que recibirá como
        parámetro. Opcionalmente, podrá recibir como parámetros una fecha de inicio y una fecha de fin
        (ambas futuras), en cuyo caso devolverá las excursiones en las que queden menos plazas que las
        indicadas y que se celebren entre esas dos fechas. La información devuelta de cada excursión
        incluirá, además de la información proporcionada al darla de alta, el número de plazas disponibles
        en ese momento.*/
        String query = "SELECT excursionId, ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas, plazasDisponibles, fechaAlta" +
                " FROM Excursion WHERE plazasDisponibles < ?";
        if((fechaInicio!=null) && (fechaFin!=null))
            query = query + " AND fechaComienzo > ? AND fechaComienzo < ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, plazasDisponibles);
            if((fechaInicio!=null) && (fechaFin!=null)) {
                preparedStatement.setTimestamp(2, Timestamp.valueOf(fechaInicio));
                preparedStatement.setTimestamp(3, Timestamp.valueOf(fechaFin));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Excursion> excursiones = new ArrayList<>();
            while (resultSet.next()){
                int i = 1;
                Long excursionId = resultSet.getLong(i++);
                String ciudad2 = resultSet.getString(i++);
                String descripcion = resultSet.getString(i++);
                Timestamp fechaComienzoTS = resultSet.getTimestamp(i++);
                LocalDateTime fechaComienzo = fechaComienzoTS.toLocalDateTime();
                float cuotaPersona = resultSet.getFloat(i++);
                int numPlazas = resultSet.getInt(i++);
                int plazasDisponibles2 = resultSet.getInt(i++);
                Timestamp fechaAltaTS = resultSet.getTimestamp(i++);
                LocalDateTime fechaAlta = fechaAltaTS.toLocalDateTime();

                Excursion excursion = new Excursion(excursionId, ciudad2, descripcion, fechaComienzo, cuotaPersona,
                        numPlazas, fechaAlta);
                excursion.setPlazasDisponibles(plazasDisponibles2);

                excursiones.add(excursion);
            }
            return excursiones;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
