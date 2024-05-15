package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.restservice.dto.ReservaToRestReservaDtoConversor;
import es.udc.ws.app.restservice.dto.RestReservaDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ReservaServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);

        Long excursionId = ServletUtils.getMandatoryParameterAsLong(req,"excursionId");
        String emailUsuario = ServletUtils.getMandatoryParameter(req,"emailUsuario");
        String tarjetaBancaria = ServletUtils.getMandatoryParameter(req,"tarjetaBancaria");
        int numPlazas = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "numPlazas"));

        Reserva reserva = null;

        try {
            reserva = ExcursionServiceFactory.getService().addReserva(excursionId, emailUsuario,
                    numPlazas, tarjetaBancaria);
        } catch (NumPlazasNoDisponible e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    AppExceptionToJsonConversor.toReservaNumPlazasNoDisponibleException(e),
                    null);
            return;
        } catch (PlazoDeReservaCerrado e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    AppExceptionToJsonConversor.toReservaPlazoDeReservaCerradoException(e),
                    null);
            return;
        }

        RestReservaDto reservaDto = ReservaToRestReservaDtoConversor.toRestReservaDto(reserva);
        String reservaURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + reserva.getReservaId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", reservaURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestReservaDtoConversor.toObjectNode(reservaDto), headers);
    }

    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);
        Long reservaId = Long.valueOf(req.getParameter("reservaId"));
        String emailUsuario = req.getParameter("emailUsuario");

        try {
            ExcursionServiceFactory.getService().cancelarReserva(reservaId, emailUsuario);
        } catch (ReservaYaCancelada e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toReservaReservaYaCanceladaException(e),
                    null);
            return;
        } catch (CancelationDateTooClose e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toReservaCancelationDateTooCloseException(e),
                    null);
            return;
        } catch (NumPlazasNoDisponible e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    AppExceptionToJsonConversor.toReservaNumPlazasNoDisponibleException(e),
                    null);
            return;
        } catch (PlazoDeReservaCerrado e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toReservaPlazoDeReservaCerradoException(e),
                    null);
            return;
        } catch (EmailCancelacionReservaDistinto e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toReservaEmailCancelacionReservaDistintoException(e),
                    null);
            return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String emailUsuario = req.getParameter("emailUsuario");

        List<Reserva> reservas = ExcursionServiceFactory.getService().findbyUser(emailUsuario);

        List<RestReservaDto> reservaDtos = ReservaToRestReservaDtoConversor.toRestReservaDto(reservas);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestReservaDtoConversor.toArrayNode(reservaDtos), null);
    }
}
