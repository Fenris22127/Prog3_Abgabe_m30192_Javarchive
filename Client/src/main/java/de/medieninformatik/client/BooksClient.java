package de.medieninformatik.client;

import de.medieninformatik.common.IReservation;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Der Client, welcher sich mit dem Server verbindet, um Reservierungen zu erstellen, anzusehen oder zu löschen.
 * @author Elisa Johanna Woelk (m30192)
 */
public class ReservationClient implements IReservation {

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf den Standard zurück.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Rot.
     */
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Grün.
     */
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Cyan.
     */
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Die {@link Client}-Instanz dieses Clients, welche für die Serverkommunikation notwendig ist
     */
    private final Client client;

    /**
     * Die Basis-{@link java.net.URI URI}, um sich mit dem Server zu verbinden
     */
    private final String baseURI;

    /**
     * Der Teil der {@link java.net.URI URI}, welche den Pfad zu der Klasse mit aufrufbaren Methoden bestimmt
     */
    private static final String URI = "/books";


    /**
     * Erstellt einen neuen {@link Client} an der {@link java.net.URI URI}, welche in {@link #baseURI} gespeichert wird.
     * @param uri Ein {@link String}: Die zu verwendende {@link java.net.URI URI}
     */
    public ReservationClient(String uri) {
        this.baseURI = uri;
        this.client = ClientBuilder.newClient();
    }

    /**
     * Gibt alle Sitzplätze entweder als [ ] (frei) oder als [X] (reserviert) aus. <br>
     * Ziel ist eine Methode mit der {@link jakarta.ws.rs.GET @GET}-Annotation und dem Pfad, welcher sich aus der
     * {@link #URI} und "/seats" ergibt.
     */
    @Override
    public void getAllSeats() {
        WebTarget target = getTarget("GET", String.format("%s/seats", URI));
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        if (status(response) == 200) {
            String reservation = response.readEntity(String.class);
            System.out.println(reservation);
        }
    }

    /**
     * Gibt alle Reservationen, wie in der Klasse 'Reservation' angegeben, als {@link String} aus.
     */
    @Override
    public void getAllReservations() {
        WebTarget target = getTarget("GET", URI);
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        if (status(response) == 200) {
            String reservation = response.readEntity(String.class);
            System.out.println(ANSI_CYAN + reservation + ANSI_RESET);
        }
    }

    /**
     * Gibt die Reservation des Sitzplatzes an der angegebenen Stelle zurück. <br>
     * Ziel ist eine Methode mit der {@link jakarta.ws.rs.GET @GET}-Annotation und dem Pfad, welcher sich aus der
     * {@link #URI} und den Angaben für die Reihe und Spalte des Sitzplatzes ergibt.
     * @param row Die Reihe des Sitzplatzes
     * @param col Die Spalte des Sitzplatzes
     */
    @Override
    public void getReservation(int row, int col) {
        WebTarget target = getTarget("GET", String.format("%s/%d/%d", URI, row, col));
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        int status = status(response);
        if (status == 200) {
            String responseBook = response.readEntity(String.class);
            System.out.printf("%sSelected Book #%d: %s%s%n", ANSI_CYAN, row, responseBook, ANSI_RESET);
        }
        if (status == 404) {
            //System.out.printf(INVALID_SEAT, ANSI_RED, row + 1, col + 1, ANSI_RESET);
        }
    }

    /**
     * Gibt zurück, ob der Sitzplatz an der angegebenen Stelle bereits reserviert ist. <br>
     * Ziel ist eine Methode mit der {@link jakarta.ws.rs.GET @GET}-Annotation und dem Pfad, welcher sich aus der
     * {@link #URI} und den Angaben für die Reihe und Spalte des Sitzplatzes ergibt.
     * @param row Die Reihe des zu prüfenden Sitzplatzes
     * @param col Die Spalte des zu prüfenden Sitzplatzes
     */
    @Override
    public void hasReservation(int row, int col) {
        WebTarget target = getTarget("GET", String.format("%s/check?row=%d&column=%d", URI, row, col));
        Response response = target.request().accept(MediaType.TEXT_PLAIN).get();
        int status = status(response);
        if (status == 200) {
            String reservation = response.readEntity(String.class);
            String ans = (Boolean.parseBoolean(reservation) ? "" : " not");
            System.out.printf("%sSeat %d-%d is%s booked.%s%n", ANSI_GREEN, row, col, ans, ANSI_RESET);
        }
        if (status == 404) {
            //System.out.printf(INVALID_SEAT, ANSI_RED, row + 1, col + 1, ANSI_RESET);
        }
    }

    /**
     * Erstellt eine Reservierung des Sitzplatzes an der angegebenen Stelle. <br>
     * Ziel ist eine Methode mit der {@link jakarta.ws.rs.PUT @PUT}-Annotation und dem Pfad, welcher sich aus der
     * {@link #URI} und den Angaben für die Reihe und Spalte des Sitzplatzes ergibt.
     * @param row Die Reihe des zu buchenden Sitzplatzes
     * @param col Die Spalte des zu buchenden Sitzplatzes
     * @param name Ein {@link String}: Der Name, auf welchen der Sitzplatz reserviert wird
     */
    @Override
    public void makeReservation(int row, int col, String name) {
        WebTarget target = getTarget("PUT", URI);
        Entity<String> entity = Entity.entity(name, MediaType.TEXT_PLAIN);
        Response response = target.request().put(entity);
        int status = status(response);
        if (status == 200) {
            System.out.printf("%sSeat %d-%d is booked under %s.%s%n", ANSI_GREEN, row, col, name, ANSI_RESET);
        }
        if (status == 404) {
            //System.out.printf(INVALID_SEAT, ANSI_RED, row + 1, col + 1, ANSI_RESET);
        }
    }

    /**
     * Löscht die Reservierung des Sitzplatzes an der angegebenen Stelle. <br>
     * Ziel ist eine Methode mit der {@link jakarta.ws.rs.DELETE @DELETE}-Annotation und dem Pfad, welcher sich aus der
     * {@link #URI} und den Angaben für die Reihe und Spalte des Sitzplatzes ergibt.
     * @param row Die Reihe des Sitzplatzes
     * @param col Die Spalte des Sitzplatzes
     */
    @Override
    public void deleteReservation(int row, int col) {
        WebTarget target = getTarget("DELETE", URI);
        Response response = target.request().delete();
        int status = status(response);
        if (status == 200) {
            System.out.printf("%sReservation for seat %d-%d was deleted.%s%n", ANSI_GREEN, row, col, ANSI_RESET);
        }
        if (status == 404) {
            //System.out.printf(INVALID_SEAT, ANSI_RED, row + 1, col + 1, ANSI_RESET);
        }
    }

    /**
     * Gibt den Pfad zu der Zielmethode im Format "--- [Annotation] [Basis-URI][Spezifischer Pfad]" in der Konsole aus
     * und gibt den Rückgabewert der {@link WebTarget Ressource} diesem Pfad zurück.
     * @param annotation Ein {@link String}: Die Annotation der Zielmethode
     * @param uri Ein {@link String}: Der spezifische Pfad zu der Zielmethode mit entsprechend benötigten Parametern
     * @return Ein {@link WebTarget}-Objekt: Der Rückgabewert der {@link WebTarget Ressource} am angegebenen Pfad
     */
    private WebTarget getTarget(String annotation , String uri) {
        //Ausgabe: --- [POST/PUT/...] [Base-Path][Specific Path]
        System.out.printf("%n--- %s %s%s%n", annotation, baseURI, uri);
        return client.target(baseURI + uri);
    }

    /**
     * Gibt die Statusinformationen der {@link Response Rückmeldung} einer Methode aus.
     * @param response Ein {@link Response}-Objekt: Die {@link Response Rückmeldung} einer serverseitigen,
     *                 angesprochenen Methode
     * @return Ein {@link Integer}: Der Status der {@link Response Rückmeldung}
     */
    private int status(Response response) {
        int code = response.getStatus();
        String reason = response.getStatusInfo().getReasonPhrase();
        System.out.printf("Status: %d %s%n", code, reason);
        return code;
    }
}
