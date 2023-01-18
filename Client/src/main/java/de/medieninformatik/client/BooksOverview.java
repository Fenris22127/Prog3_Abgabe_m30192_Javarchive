package de.medieninformatik.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BooksOverview extends Application {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(BooksOverview.class.getName());

    private static final String BASE_URI = "http://localhost:3306/rest";

    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("books-overview.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1100, 800);
            primaryStage.setScene(scene);
            //...
            primaryStage.show();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not find FXML file!");
            e.printStackTrace();
        }
    }
}
