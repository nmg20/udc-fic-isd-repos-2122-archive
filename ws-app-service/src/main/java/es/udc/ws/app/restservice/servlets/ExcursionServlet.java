package es.udc.ws.app.restservice.servlets;


import es.udc.ws.app.model.excursionservice.ExcursionService;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.restservice.dto.ExcursionToRestExcursionDtoConversor;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestExcursionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ExcursionServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        excursion = ExcursionServiceFactory.getService().addExcursion(excursion);

        excursionDto = ExcursionToRestExcursionDtoConversor.toRestExcusrionDto(excursion);
        String excursionURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + excursion.getExcursionId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", excursionURL);
        ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_CREATED,
                JsonToRestExcursionDtoConversor.toObjectNode(excursionDto), headers);
    }

    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws
            InputValidationException, IOException, InstanceNotFoundException {

        if(req.getParameter("excursionId") != null){

            Long excursionId = Long.parseLong(req.getParameter("excursionId"));
            try {
                ExcursionServiceFactory.getService().deleteExcursion(excursionId);

            } catch (ReservaNoCancelada e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        AppExceptionToJsonConversor.toReservaNoCancelada(e),
                        null);
                return;
            }


        }else{
        Long excursionId = ServletUtils.getIdFromPath(req, "excursion");
        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        if (!excursionId.equals(excursionDto.getExcursionId())) {
            throw new InputValidationException("Petición no válida: excursionId no válida");
        }
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        try {
            ExcursionServiceFactory.getService().updateExcursion(excursion);
        } catch (UpdateExcursionFueraDePlazo e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toUpdateExcursionFueraDePlazoException(e),
                    null);
            return;
        } catch (InvalidUpdateDataException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    AppExceptionToJsonConversor.toInvalidUpdateDataException(e),
                    null);
            return;
        } catch (ExcursionFueraDePlazo e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                    AppExceptionToJsonConversor.toExcursionFueraDePlazoException(e),
                    null);
            return;

        }
    }
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }


    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);
        List<Excursion> excursiones = null;
        List<Excursion> excursiones2 = null;

        if(req.getParameter("ciudad") != null) {
            String ciudad = req.getParameter("ciudad");
            if (req.getParameter("fromDate") == null || req.getParameter("toDate") == null) {
                excursiones = ExcursionServiceFactory.getService().findByCity(ciudad, null, null);
            } else {
                LocalDate fromDate = LocalDate.parse(req.getParameter("fromDate"));
                LocalDate toDate = LocalDate.parse(req.getParameter("toDate"));

                excursiones = ExcursionServiceFactory.getService().findByCity(ciudad, fromDate.atStartOfDay(), toDate.atStartOfDay().plusHours(23));
            }
            List<RestExcursionDto> excursionDtos = ExcursionToRestExcursionDtoConversor.toRestExcusionDtos(excursiones);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestExcursionDtoConversor.toArrayNode(excursionDtos), null);
        }else{
            int plazasDisponibles = Integer.parseInt(ServletUtils.getMandatoryParameter(req, "plazasDisponibles"));
        if(req.getParameter("fromDate") == null ){
            excursiones2 = ExcursionServiceFactory.getService().findByAvailableSlot(plazasDisponibles,null,null);
        }else{
            LocalDate fromDate = LocalDate.parse(req.getParameter("fromDate"));


            excursiones2 = ExcursionServiceFactory.getService().findByAvailableSlot(plazasDisponibles, fromDate.atStartOfDay(),fromDate.atStartOfDay().plusDays(90));
        }


        List<RestExcursionDto> excursionDtos2 = ExcursionToRestExcursionDtoConversor.toRestExcusionDtos(excursiones2);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestExcursionDtoConversor.toArrayNode(excursionDtos2), null);


        }


    }

}
