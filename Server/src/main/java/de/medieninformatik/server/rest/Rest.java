package de.medieninformatik.server.rest;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Book;
import de.medieninformatik.server.database.BookQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verarbeitet die Anfragen der Clients und stellt die benötigten Methoden bereit.
 *
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
@Path("books")
public class Rest {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(Rest.class.getName());

    private static final Book testbook = new Book(
            0, "x", "x", 0, 0, "x", List.of(0,0), List.of(0,0), 4.04);

    /**
     * Gibt alle Bücher in der Datenbank im JSON Format zurück.
     *
     * @return Eine {@link Response}, welches eine {@link List} mit {@link Book} Objekten im JSON Format beinhaltet
     */
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getAllBooks() {
        LOGGER.log(Level.INFO, "{0}Getting all books{1}\n", new Object[]{Ansi.CYAN, Ansi.RESET});
        List<Book> allBooks = BookQuery.getAllBooks();
        //TODO: Database & Server blocking each other
        return Response.ok().entity(allBooks).build();
        //TODO: Case: DB Error, like DB empty
    }

    /**
     * Gibt das Buch mit der übergebenen ID in der Datenbank im JSON Format zurück.
     *
     * @return Eine {@link Response}, welche ein {@link Book} Objekt im JSON Format beinhaltet
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getBook(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "{0}Getting book with ID {1}{2}\n", new Object[]{Ansi.CYAN, id, Ansi.RESET});
        Book book = BookQuery.getBook(id);
        //TODO: Database & Server blocking each other
        return Response.ok().entity(book).build();
        //TODO: Case: ID not in Database
    }

    /**
     * Erhält ein Buch im JSON Format und fügt dies der Datenbank und dem GUI hinzu. <br>
     * <b>Diese Methode ist nur über den Master Client ausführbar!</b>
     *
     * @param book Das neue Buch
     * @return Eine {@link Response}, welches das Ergebnis des Vorgangs meldet
     */
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response createBook(Book book) {
        LOGGER.log(Level.INFO, "{0}Creating new book\n{1}\n{2}\n",
                new Object[]{Ansi.CYAN, book, Ansi.RESET});
        //TODO: Database stuff here
        return Response.ok().build();
        //TODO: Case: Couldn't add book
        //TODO: Case: No rights
    }

    /**
     * Erhält eine ID und ein Buch im JSON Format und ändert das Buch mit der ID zu dem Inhalt des übergebenen Buches.
     * <br>
     * <b>Diese Methode ist nur über den Master Client ausführbar!</b>
     *
     * @param id Die ID des zu ändernden Buches
     * @param book Das veränderte Buch
     * @return Eine {@link Response}, welches das Ergebnis des Vorgangs meldet
     */
    @POST
    @Path("edit/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response editBook(@PathParam("id") int id, Book book) {
        LOGGER.log(Level.INFO, "{0}Editing book with ID {1}\n{2}\n{3}\n",
                new Object[]{Ansi.CYAN, id, book, Ansi.RESET});
        //TODO: Database stuff here
        return Response.ok().build();
        //TODO: Case: Couldn't edit book
        //TODO: Case: No rights
    }

    /**
     * Löscht das Buch mit der ID. <br>
     * <b>Diese Methode ist nur über den Master Client ausführbar!</b>
     *
     * @param id Die ID des zu löschenden Buches
     * @return Eine {@link Response}, welches das Ergebnis des Vorgangs meldet
     */
    @DELETE
    @Path("delete/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "{0}Deleting book with ID {1}\n{2}\n",
                new Object[]{Ansi.CYAN, id, Ansi.RESET});
        //TODO: Database stuff here
        //TODO: Do GUI stuff here
        return Response.ok().build();
        //TODO: Case: Couldn't delete book
        //TODO: Case: No rights
    }
}
