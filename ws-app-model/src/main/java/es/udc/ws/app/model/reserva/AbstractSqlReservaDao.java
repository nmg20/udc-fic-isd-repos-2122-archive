package es.udc.ws.app.model.reserva;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class AbstractSqlReservaDao implements SqlReservaDao {

    protected AbstractSqlReservaDao() {
    }

    @Override
    public void cancelar(Connection connection, Reserva reserva) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Reserva"
                + " SET fechaCancelacion = ? WHERE excursionId = ? AND emailUsuario = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(reserva.getFechaCancelacion()));
            preparedStatement.setLong(i++, reserva.getExcursionId());
            preparedStatement.setString(i++, reserva.getEmailUsuario());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(reserva.getReservaId(),
                        Excursion.class.getName());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Reserva find(Connection connection, Long reservaId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "SELECT excursionId, emailUsuario, numPlazas, tarjetaBancaria, fechaReserva, costeTotal, fechaCancelacion " +
                "FROM Reserva WHERE reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1, j = 0;
            preparedStatement.setLong(i++, reservaId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new InstanceNotFoundException(reservaId, Reserva.class.getName());
            }
            /* Get results. */
            i = 1;
            Long excursionId = resultSet.getLong(i++);
            String emailUsuario = resultSet.getString(i++);
            int numPlazas = resultSet.getInt(i++);
            String tarjetaBancaria = resultSet.getString(i++);
            LocalDateTime fechaReserva = resultSet.getTimestamp(i++).toLocalDateTime();
            float costeTotal = resultSet.getFloat(i++);

            LocalDateTime fechaCancelacion = null;
            j = i++;

            if(resultSet.getTimestamp(j) != null){
                fechaCancelacion = resultSet.getTimestamp(j).toLocalDateTime();
            }

            Reserva reserva = new Reserva(reservaId,excursionId,emailUsuario,numPlazas,tarjetaBancaria,
                    fechaReserva,costeTotal);

            reserva.setFechaCancelacion(fechaCancelacion);

            return reserva;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> findbyUser(Connection connection, String email){
        /* Create "queryString". */
        String queryString = "SELECT reservaId, excursionId, numPlazas, tarjetaBancaria, fechaReserva, costeTotal, fechaCancelacion" +
                " FROM Reserva WHERE emailUsuario = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, email);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Reserva> list = new ArrayList<>();

            while(resultSet.next()) {
                /* Get results. */
                i = 1;
                Long reservaId = resultSet.getLong(i++);
                Long excursionId = resultSet.getLong(i++);
                int numPlazas = resultSet.getInt(i++);
                String tarjetaBancaria = resultSet.getString(i++);
                LocalDateTime fechaReserva = resultSet.getTimestamp(i++).toLocalDateTime();
                float costeTotal = resultSet.getFloat(i++);

                LocalDateTime fechaCancelacion = null;

                if(resultSet.getObject(i) != null){
                    fechaCancelacion = resultSet.getTimestamp(i).toLocalDateTime();
                }

                /*
                if(resultSet.getTimestamp(i++) != null){
                    fechaCancelacion = resultSet.getTimestamp(i).toLocalDateTime();
                }*/

                Reserva reserva = new Reserva(reservaId,excursionId,email,numPlazas,tarjetaBancaria,fechaReserva,costeTotal);
                reserva.setFechaCancelacion(fechaCancelacion);

                list.add(reserva);
                Collections.sort(list, new Comparator<Reserva>() {
                    @Override
                    public int compare(Reserva o1, Reserva o2) {
                        if(o1.getFechaReserva().isBefore(o2.getFechaReserva())){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                });
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long reservaId) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Reserva WHERE reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservaId.longValue());

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reservaId,
                        Reserva.class.getName());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public List<Reserva> findByExcursionId(Connection connection, Long excursionId) throws InputValidationException {

        /* Create "queryString". */
        String queryString = "SELECT reservaId, emailUsuario, numPlazas, tarjetaBancaria, fechaReserva, costeTotal, fechaCancelacion" +
                " FROM Reserva WHERE excursionId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Reserva> list = new ArrayList<>();

            while(resultSet.next()) {
                /* Get results. */
                i = 1;
                Long reservaId = resultSet.getLong(i++);
                String emailUsuario = resultSet.getString(i++);
                int numPlazas = resultSet.getInt(i++);
                String tarjetaBancaria = resultSet.getString(i++);
                LocalDateTime fechaReserva = resultSet.getTimestamp(i++).toLocalDateTime();
                float costeTotal = resultSet.getFloat(i++);

                LocalDateTime fechaCancelacion = null;

                if(resultSet.getObject(i) != null){
                    fechaCancelacion = resultSet.getTimestamp(i).toLocalDateTime();
                }

                /*
                if(resultSet.getTimestamp(i++) != null){
                    fechaCancelacion = resultSet.getTimestamp(i).toLocalDateTime();
                }*/

                Reserva reserva = new Reserva(reservaId,excursionId,emailUsuario,numPlazas,tarjetaBancaria,fechaReserva,costeTotal);
                reserva.setFechaCancelacion(fechaCancelacion);

                list.add(reserva);
                Collections.sort(list, new Comparator<Reserva>() {
                    @Override
                    public int compare(Reserva o1, Reserva o2) {
                        if(o1.getFechaReserva().isBefore(o2.getFechaReserva())){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                });
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
