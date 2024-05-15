package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.ClientExcursionServiceFactory;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientExcursionService clientExcursionService =
                ClientExcursionServiceFactory.getService();
        if("-addExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[]{4});

            // -addExc <city> <description> <date> <price> <maxPlaces>

            try {
                Long excursionId = clientExcursionService.addExcursion(new ClientExcursionDto(null,
                        args[1], args[2], LocalDateTime.parse(args[3]), Float.valueOf(args[4]),
                        Short.valueOf(args[5]), Integer.valueOf(args[5])));

                System.out.println("Excursión " + excursionId + " creada correctamente.");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-reserve".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {2, 4});

            // -reserve <userEmail> <excursionId> <creditCardNumber> <places>

            try {
                Long reservaId = clientExcursionService.addReserva(Long.parseLong(args[2]),
                        args[1], Integer.valueOf(args[4]), args[3]);

                System.out.println("Reserva " + reservaId + " creada correctamente.");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            // -cancel <reservationId> <userEmail>

            try {
                clientExcursionService.cancelarReserva(Long.parseLong(args[1]),args[2]);

                System.out.println("Reserva " + args[1] + " cancelada correctamente.");

            } catch (InputValidationException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-updateExc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {1, 5, 6});

            // -updateExc <excId> <city> <description> <date> <price> <maxPlaces>

            try {
                clientExcursionService.updateExcursion(new ClientExcursionDto(Long.valueOf(args[1]), args[2], args[3], LocalDateTime.parse(args[4]), Float.valueOf(args[5]),
                        Integer.valueOf(args[6])));

                System.out.println("Excursion " + args[1] + " actualizada correctamente.");

            } catch (ClientExcursionFueraDePlazo | ClientInvalidUpdateDataException | ClientUpdateExcursionFueraDePlazo | InputValidationException |
                    InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-findReservations".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // -findReservations <userEmail>

            try {
                List<ClientReservaDto> reservas = clientExcursionService.findbyUser(args[1]);
                System.out.println("Encontradas " + reservas.size() +
                        " reserva(s) de '" + args[1] + "'");

                for (int i = 0; i < reservas.size(); i++) {
                    ClientReservaDto reservaDto = reservas.get(i);
                    System.out.print("["+i+"] ");
                    System.out.println(reservaDto.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-findExcursions".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {});

            // -findExcursions <city> <fromDate> <toDate>

            try {
                List<ClientExcursionDto> excursiones = clientExcursionService.findByCity(args[1],
                                                                        LocalDate.parse(args[2]),
                                                                        LocalDate.parse(args[3]));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formatFromDate = LocalDate.parse(args[2]).format(formatter);
                String formatToDate = LocalDate.parse(args[3]).format(formatter);

                System.out.println("Encontradas " + excursiones.size() +
                        " excursion(es) en " + args[1] + " de " + formatFromDate +
                        " a " + formatToDate);

                for (int i = 0; i < excursiones.size(); i++) {
                    ClientExcursionDto excursionDto = excursiones.get(i);
                    System.out.print("["+i+"] ");
                    System.out.println(excursionDto.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-findExcByPlaces".equalsIgnoreCase(args[0])){

            validateArgs(args, 3, new int[] {});
            // -findExcByPlaces <numPlazas> <fromDate> <toDate>

            try {
                List<ClientExcursionDto> excursiones = clientExcursionService.findByAvailableSlot(Integer.parseInt(args[1]),
                        LocalDate.parse(args[2]));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formatFromDate = LocalDate.parse(args[2]).format(formatter);
                String formattoDate = LocalDate.parse(args[2]).plusDays(90).format(formatter);

                System.out.println("Encontradas " + excursiones.size() +
                        " excursion(es) en " + args[1] + " de " + formatFromDate +
                        " a " + formattoDate);

                for (int i = 0; i < excursiones.size(); i++) {
                    ClientExcursionDto excursionDto = excursiones.get(i);
                    System.out.print("["+i+"] ");
                    System.out.println(excursionDto.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-removeExcursion".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // -removeExcursion <excursionId>

            try {
                clientExcursionService.deleteExcursion(Long.parseLong(args[1]));

                System.out.println("Excursion " + args[1] + " eliminada correctamente.");

            } catch (InputValidationException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [añadir]    ExcursionServiceClient -a <title> <hours> <minutes> <description> <price>\n" +
                "    [borrar] ExcursionServiceClient -r <movieId>\n" +
                "    [actualizar] ExcursionServiceClient -u <movieId> <title> <hours> <minutes> <description> <price>\n" +
                "    [buscar]   ExcursionServiceClient -f <keywords>\n" +
                "    [reservar]    ExcursionServiceClient -b <excursionId> <emailUsuario> <numPlazas> <tarjetaBancaria>\n" +
                "    [get]    ExcursionServiceClient -g <reservaId>\n");
    }
}