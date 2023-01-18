package de.medieninformatik.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.logging.Logger;

/**
 * Verarbeitet die Anfragen der Clients und stellt die benötigten Methoden bereit.
 * @author Elisa Johanna Woelk (m30192)
 */
@Path("books")
public class Rest {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private final Logger LOGGER = Logger.getLogger(Rest.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getAllBooks() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("All Books here");
        //...
        return Response.ok(stringBuilder.toString()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getBook(@PathParam("id") int id) {
        StringBuilder stringBuilder = new StringBuilder();
        //...
        stringBuilder.append("Book" + id);
        return Response.ok(stringBuilder.toString()).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBook(String book) {
        StringBuilder stringBuilder = new StringBuilder();
        //...
        stringBuilder.append(book);
        return Response.ok(stringBuilder.toString()).build();
    }

    @POST
    @Path("{id}/{title}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response editBook(@PathParam("id") int id, @PathParam("title") String title) {
        StringBuilder stringBuilder = new StringBuilder();
        //...
        stringBuilder.append(id + " -> " + title);
        return Response.ok(stringBuilder.toString()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBook(@PathParam("id") int id) {
        StringBuilder stringBuilder = new StringBuilder();
        //...
        return Response.ok(stringBuilder.toString()).build();
    }

    /*@GET
    @Path("/seats")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getAllSeats() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (seats[row][col].isBooked()) {
                    stringBuilder.append("[X] ");
                }
                else {
                    stringBuilder.append("[ ] ");
                }
            }
            stringBuilder.append(System.lineSeparator());
        }
        return Response.ok(stringBuilder.toString()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getAllReservations() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                stringBuilder.append(seats[row][col].toString()).append(System.lineSeparator());
            }
        }
        return Response.ok(stringBuilder.toString()).build();
    }

    @GET
    @Path("{row}/{col}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReservation(
            @PathParam("row") int row,
            @PathParam("col") int col) {
        logger.log(INFO, "Getting Reservation for Seat in row {0} column {1}", new Object[] {row, col});
        Response response;
        try {
            Reservation r = seats[row][col];
            response = Response.ok(r.toString()).build();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            logger.log(WARNING, "Seat {0}-{1} is not a valid seat!", new Object[]{row, col});
            response = Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkReservation(
            @QueryParam("row") int row,
            @QueryParam("column") int col) {
        Response response;
        try {
            boolean exists = seats[row][col].isBooked();
            response = Response.ok(exists).build();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            logger.log(WARNING, "Seat {0}-{1} is not a valid seat!", new Object[]{row, col});
            response = Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @PUT
    @Path("{row}/{col}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response makeReservation(
            @PathParam("row") int row,
            @PathParam("col") int col,
            String name) {
        Response response;
        try {
            if (!seats[row][col].isBooked()) {
                seats[row][col].setName(name);
                seats[row][col].setBooked(true);
                logger.log(INFO, "Created new Reservation under {0} for seat {1}-{2}", new Object[]{name, row, col});
                response = Response.noContent().status(Response.Status.OK).build();
            }
            else {
                logger.log(INFO, "Seat {0}-{1} is already booked under {2}", new Object[]{row, col, seats[row][col].getName()});
                response = Response.noContent().status(Response.Status.CONFLICT).build();
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            logger.log(WARNING, "Seat {0}-{1} is not a valid seat!", new Object[]{row, col});
            response = Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @DELETE
    @Path("{row}/{col}")
    public Response deleteReservation(
            @PathParam("row") int row,
            @PathParam("col") int col) {
        Response response;
        try {
            if (seats[row][col].isBooked()) {
                seats[row][col].setBooked(false);
                seats[row][col].setName("");
                logger.log(INFO, "Reservation for seat {0}-{1} under {2} was deleted!",
                        new Object[]{row, col, seats[row][col].getName()});
                response = Response.noContent().status(Response.Status.OK).build();
            } else {
                logger.log(WARNING, "Reservation for seat {0}-{1} could not be deleted, seat is not booked!",
                        new Object[]{row, col});
                response = Response.noContent().status(Response.Status.NOT_FOUND).build();
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            logger.log(WARNING, "Seat {0}-{1} is not a valid seat!", new Object[]{row, col});
            response = Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }*/
}
