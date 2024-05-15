package es.udc.ws.app.model.reserva;

import es.udc.ws.app.model.excursion.Excursion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao{

    @Override
    public Reserva create(Connection connection, Reserva reserva){
        String queryString = "INSERT INTO Reserva"
                + " (excursionId, emailUsuario, numPlazas, tarjetaBancaria, fechaReserva, costeTotal, fechaCancelacion)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserva.getExcursionId());
            preparedStatement.setString(i++, reserva.getEmailUsuario());
            preparedStatement.setInt(i++, reserva.getNumPlazas());
            preparedStatement.setString(i++, reserva.getTarjetaBancaria());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(reserva.getFechaReserva()));
            preparedStatement.setFloat(i++, reserva.getCosteTotal());
            preparedStatement.setTimestamp(i++, null);

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long reservaId = resultSet.getLong(1);

            /* Return reserva. */
            Reserva reservaAux = new Reserva(reservaId, reserva.getExcursionId(), reserva.getEmailUsuario(),
                    reserva.getNumPlazas(), reserva.getTarjetaBancaria(), reserva.getFechaReserva(),
                    reserva.getCosteTotal());

            reservaAux.setFechaCancelacion(reserva.getFechaCancelacion());

            return reservaAux;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
