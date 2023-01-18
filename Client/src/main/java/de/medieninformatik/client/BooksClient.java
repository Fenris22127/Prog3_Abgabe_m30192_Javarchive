package de.medieninformatik.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Book;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Der Client, welcher sich mit dem Server verbindet, um Bücher zu erhalten, erstellen, bearbeiten oder zu löschen.
 *
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
public class BooksClient implements IBooksOverview {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(BooksClient.class.getName());

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Ein {@link String} mit Format Specifiers, welche die {@link #URI} und die Buch-ID aneinanderhängt,
     * um einen Pfad zu bilden.
     */
    private static final String URIFORMAT = "%s/%d";

    /**
     * Der Teil der {@link java.net.URI URI}, welche den Pfad zu der Klasse mit aufrufbaren Methoden bestimmt
     */
    private static final String URI = "/books";

    /**
     * Die {@link Client}-Instanz dieses Clients, welche für die Serverkommunikation notwendig ist
     */
    private final Client client;

    /**
     * Die Basis-{@link java.net.URI URI}, um sich mit dem Server zu verbinden
     */
    private final String baseURI;


    /**
     * Erstellt einen neuen {@link Client} an der {@link java.net.URI URI}, welche in {@link #baseURI} gespeichert wird.
     *
     * @param uri Ein {@link String}: Die zu verwendende {@link java.net.URI URI}
     */
    public BooksClient(String uri) {
        this.baseURI = uri;
        this.client = ClientBuilder.newClient();
    }

    /**
     * Gibt die Statusinformationen der {@link Response Rückmeldung} einer Methode aus.
     *
     * @param response Ein {@link Response}-Objekt: Die {@link Response Rückmeldung} einer serverseitigen,
     *                 angesprochenen Methode
     * @return Ein {@link Integer}: Der Status der {@link Response Rückmeldung}
     */
    private static int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        LOGGER.log(Level.INFO, "{0}Status: {1} {2}{3}",
                new Object[]{Ansi.CYAN, code, reason, Ansi.RESET});
        return code;
    }

    @Override
    public void getAllBooks() {
        WebTarget target = getTarget("GET", String.format("%s/%s", URI, "getAll"));
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
        int status = status(response);
        if (status == 200) {
            try {
                List<Book> bookList;
                bookList = new ObjectMapper().readValue(response.readEntity(String.class), new TypeReference<>() {
                });
                LOGGER.log(Level.INFO, "{0}JSON -> List<Book>\n{1} books in list.{2}",
                        new Object[]{Ansi.GREEN, bookList.size(), Ansi.RESET});
                //do something
            }
            catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "{0}Unable to parse JSON to List<Book>!{1}",
                        new Object[]{Ansi.RED, Ansi.RESET});
                e.printStackTrace();
            }
        }
        else {
            LOGGER.log(Level.SEVERE, "{0}Error {1}: Unable get all books!\nReason: {2}{3}",
                    new Object[]{Ansi.RED, status, response.getStatusInfo().getReasonPhrase(), Ansi.RESET});
        }
    }

    @Override
    public void getBook(int id) {
        WebTarget target = getTarget("GET", String.format(URIFORMAT, URI, id));
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
        int status = status(response);
        if (status == 200) {
            String responseBook = response.readEntity(String.class);
            LOGGER.log(Level.INFO, "{0}Selected book {1}!\n{2}{3}",
                    new Object[]{Ansi.GREEN, id, responseBook, Ansi.RESET});
        }
    }

    @Override
    public void createBook(Book book) {
        try {
            String bookToJson = MAPPER.writeValueAsString(book);
            WebTarget target = getTarget("PUT", URI);
            Entity<String> entity = Entity.entity(bookToJson, MediaType.APPLICATION_JSON);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).put(entity);
            int status = status(response);
            if (status == 200) {
                //add book to GUI
                LOGGER.log(Level.INFO, "{0}Book created!\n\"{1}\" by {2}{3}",
                        new Object[]{Ansi.GREEN, book.title(), book.authors(), Ansi.RESET});
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "{0}Unable to parse new Book to JSON!{1}",
                    new Object[]{Ansi.RED, Ansi.RESET});
            e.printStackTrace();
        }
    }

    @Override
    public void editBook(Book book) {
        try {
            String bookToJson = MAPPER.writeValueAsString(book);
            WebTarget target = getTarget("POST", String.format("%s/edit/%d", URI, book.id()));
            Entity<String> entity = Entity.entity(bookToJson, MediaType.APPLICATION_JSON);
            Response response = target.request().accept(MediaType.APPLICATION_JSON).post(entity);
            int status = status(response);
            if (status == 200) {
                //add book to GUI
                LOGGER.log(Level.INFO, "{0}Book edited!\n\"{1}\" by {2}{3}",
                        new Object[]{Ansi.GREEN, book.title(), book.authors(), Ansi.RESET});
            }
            else {
                LOGGER.log(Level.SEVERE, "{0}Error {1}: Unable edit book!\nReason: {2}{3}",
                        new Object[]{Ansi.RED, status, response.getStatusInfo().getReasonPhrase(), Ansi.RESET});
            }
        }
        catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "{0}Unable to parse edited Book to JSON!{1}",
                    new Object[]{Ansi.RED, Ansi.RESET});
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(Book book) {
        WebTarget target = getTarget("DELETE", String.format("%s/delete/%d", URI, book.id()));
        Response response = target.request().delete();
        int status = status(response);
        if (status == 200) {
            //do something (with sql)
            LOGGER.log(Level.INFO, "{0}Book deleted!\n\"{1}\" by {2}{3}",
                    new Object[]{Ansi.GREEN, book.title(), book.authors(), Ansi.RESET});
        }
        else {
            LOGGER.log(Level.SEVERE, "{0}Error {1}: Unable delete book!\nReason: {2}{3}",
                    new Object[]{Ansi.RED, status, response.getStatusInfo().getReasonPhrase(), Ansi.RESET});
        }
    }

    /**
     * Gibt den Pfad zu der Zielmethode im Format "--- [Annotation] [Basis-URI][Spezifischer Pfad]" in der Konsole aus
     * und gibt den Rückgabewert der {@link WebTarget Ressource} diesem Pfad zurück.
     *
     * @param annotation Ein {@link String}: Die Annotation der Zielmethode
     * @param uri        Ein {@link String}: Der spezifische Pfad zu der Zielmethode mit entsprechend benötigten Parametern
     * @return Ein {@link WebTarget}-Objekt: Der Rückgabewert der {@link WebTarget Ressource} am angegebenen Pfad
     */
    private WebTarget getTarget(String annotation, String uri) {
        //Ausgabe: --- [POST/PUT/...] [Base-Path][Specific Path]
        LOGGER.log(Level.INFO, "{0}--- {1} {2}{3}{4}",
                new Object[]{Ansi.CYAN, annotation, baseURI, uri, Ansi.RESET});
        return client.target(baseURI + uri);
    }
}
