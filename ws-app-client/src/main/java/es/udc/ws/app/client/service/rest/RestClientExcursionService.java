package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExcursionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RestClientExcursionService implements ClientExcursionService{

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientExcursionService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "excursion").
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientExcursionDtoConversor.toClientExcursionDto(response.getEntity().getContent()).getExcursionId();
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException, InstanceNotFoundException, ClientUpdateExcursionFueraDePlazo, ClientInvalidUpdateDataException, ClientExcursionFueraDePlazo {
        try {
            HttpResponse response = Request.Put(getEndpointAddress() + "excursion/" + excursion.getExcursionId()).
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        } catch (InputValidationException | InstanceNotFoundException | ClientUpdateExcursionFueraDePlazo | ClientInvalidUpdateDataException | ClientExcursionFueraDePlazo e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientExcursionDto> findByCity(String ciudad, LocalDate fromDate, LocalDate toDate) throws InputValidationException {
        try {
            HttpResponse response = Request.Get(getEndpointAddress()
                            + "excursion?ciudad=" + URLEncoder.encode(ciudad, "UTF-8")
                            + "&fromDate=" + URLEncoder.encode(String.valueOf(fromDate), "UTF-8")
                            + "&toDate=" + URLEncoder.encode(String.valueOf(toDate), "UTF-8")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientExcursionDtoConversor.toClientExcursionDtos((response.getEntity()
                    .getContent()));
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long addReserva(Long excursionId, String email, int numPlazas, String tarjetaBancaria) throws InputValidationException,
            InstanceNotFoundException, ClientNumPlazasNoDisponible, ClientPlazoDeReservaCerrado {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "reserva").
                    bodyForm(
                            Form.form().
                                    add("emailUsuario", email).
                                    add("excursionId", String.valueOf(excursionId)).
                                    add("numPlazas", Integer.toString(numPlazas)).
                                    add("tarjetaBancaria", tarjetaBancaria).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientReservaDtoConversor.toClientReservaDto(
                    response.getEntity().getContent()).getReservaId();

        } catch (InputValidationException | InstanceNotFoundException | ClientNumPlazasNoDisponible | ClientPlazoDeReservaCerrado e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelarReserva(Long reservaId, String email) throws InstanceNotFoundException, InputValidationException, ClientReservaYaCancelada, ClientCancelationDateTooClose, ClientNumPlazasNoDisponible, ClientPlazoDeReservaCerrado, ClientEmailCancelacionReservaDistinto {
        try {

            HttpResponse response = Request.Put(getEndpointAddress()
                    + "reserva?reservaId=" + URLEncoder.encode(String.valueOf(reservaId), "UTF-8")
                    + "&emailUsuario=" + URLEncoder.encode(email, "UTF-8")).
            execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException | InputValidationException | ClientReservaYaCancelada | ClientCancelationDateTooClose | ClientNumPlazasNoDisponible | ClientPlazoDeReservaCerrado | ClientEmailCancelacionReservaDistinto e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientReservaDto> findbyUser(String emailUsuario){
        try{
        HttpResponse response = Request.Get(getEndpointAddress()
                        + "reserva?emailUsuario=" + URLEncoder.encode(emailUsuario, "UTF-8")).
                execute().returnResponse();

        validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientReservaDtoConversor.toClientReservaDtos(response.getEntity()
                    .getContent());

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientExcursionDto> findByAvailableSlot(int numPlazas, LocalDate fechaInicio) throws InputValidationException {
        try {
            HttpResponse response = Request.Get(getEndpointAddress()
                            + "excursion?plazasDisponibles=" + URLEncoder.encode(String.valueOf(numPlazas), "UTF-8")
                            + "&fromDate=" + URLEncoder.encode(String.valueOf(fechaInicio), "UTF-8")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);
            return JsonToClientExcursionDtoConversor.toClientExcursionDtos((response.getEntity()
                    .getContent()));
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteExcursion(Long excursionId) throws InstanceNotFoundException, InputValidationException,ClientReservaNoCancelada {
        try {

            HttpResponse response = Request.Put(getEndpointAddress()
                            + "excursion?excursionId=" + URLEncoder.encode(String.valueOf(excursionId), "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException | InputValidationException | ClientReservaNoCancelada e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientExcursionDto excursion) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientExcursionDtoConversor.toObjectNode(excursion));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private InputStream toInputStream(ClientReservaDto reserva) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientReservaDtoConversor.toObjectNode(reserva));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND: //404
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST: //400
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN: //403
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE: //410
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
