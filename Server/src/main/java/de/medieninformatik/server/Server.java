package de.medieninformatik.server;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.server.rest.BookApplication;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Startet den Server auf der lokalen Maschine.
 * @author Elisa Johanna Woelk (m30192)
 */
public class Server {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    /**
     * Die {@link URI} für diese Implementation des Servers
     */
    private static String uri = "http://localhost:3306/rest";

    /**
     * Startet den Server auf der lokalen Maschine über die angegebene {@link #uri URI}.
     * @param args <-
     * @throws URISyntaxException   Wird geworfen, wenn der übergebene String nicht in eine {@link URI} konvertiert
     *                              werden kann.
     * @throws IOException  Wird geworfen, wenn beim Starten des Servers ein Problem auftritt oder eine Eingabe in der
     *                      Konsole nicht gelesen werden kann.
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        URI baseUri = new URI(uri);
        ResourceConfig config = ResourceConfig.forApplicationClass(BookApplication.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        StaticHttpHandler handler = new StaticHttpHandler("web");
        handler.setFileCacheEnabled(false);
        ServerConfiguration serverConfig = server.getServerConfiguration();
        serverConfig.addHttpHandler(handler, "/");

        if(!server.isStarted()) server.start();
        LOGGER.log(Level.INFO, "{0}http://localhost:3306/rest{1}",
                new Object[]{Ansi.CYAN, Ansi.RESET});
        LOGGER.log(Level.INFO, "{0}Server started!{1}",
                new Object[]{Ansi.CYAN, Ansi.RESET});
        LOGGER.log(Level.INFO, "{0}Enter stops the server!{1}",
                new Object[]{Ansi.CYAN, Ansi.RESET});
        System.in.read();
        server.shutdownNow();
    }

    /**
     * Erlaubt es, die {@link URI}, welche vom Server verwendet wird, zu bestimmen.
     * @param uri Ein {@link String}: Die {@link URI}, welche vom Server verwendet werden soll
     */
    public static void setUri(String uri) {
        Server.uri = uri;
    }
}
