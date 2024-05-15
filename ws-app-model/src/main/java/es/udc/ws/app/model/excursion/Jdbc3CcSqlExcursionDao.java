package es.udc.ws.app.model.excursion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlExcursionDao extends AbstractSqlExcursionDao{

    @Override
    public Excursion create(Connection connection, Excursion excursion) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Excursion"
                + " (ciudad, descripcion, fechaComienzo, cuotaPersona, numPlazas, plazasDisponibles, fechaAlta)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, excursion.getCiudad());
            preparedStatement.setString(i++, excursion.getDescripcion());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaComienzo()));
            preparedStatement.setFloat(i++, excursion.getCuotaPersona());
            preparedStatement.setInt(i++, excursion.getNumPlazas());
            preparedStatement.setInt(i++, excursion.getPlazasDisponibles());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaAlta()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long excursionId = resultSet.getLong(1);

            Excursion excursionAux = new Excursion(excursionId, excursion.getCiudad(), excursion.getDescripcion(),
                    excursion.getFechaComienzo(), excursion.getCuotaPersona(), excursion.getNumPlazas(), excursion.getFechaAlta());
            excursionAux.setPlazasDisponibles(excursion.getPlazasDisponibles());

            /* Return excursion. */
            return excursionAux;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
