package de.medieninformatik.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Ansi;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BooksClientController {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(BooksClientController.class.getName());

    @FXML
    AnchorPane subfields;

    @FXML
    TextField textSearch;

    @FXML
    private void getFilters() {
        String text = "";
        List<Integer> selectedSubfields = new LinkedList<>();
        ObservableList<Node> subfieldBoxes = subfields.getChildren();
        if (!textSearch.getText().isBlank()) {
            text = textSearch.getText();
        }
        for (int i = 0; i < subfieldBoxes.size(); i++) {
            CheckBox c = (CheckBox) subfieldBoxes.get(i);
            if (c.isSelected()) selectedSubfields.add(i + 1);
        }

        Filter filter = new Filter(text, selectedSubfields);
        final ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(filter);
        }
        catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, String.format("%sUnable to parse Filter to JSON!%s", Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "{0}Filter -> JSON%n{1}{2}%n",
                new Object[]{Ansi.GREEN, json, Ansi.RESET});
    }

    @FXML
    private void clearFilters() {
        ObservableList<Node> subfieldBoxes = subfields.getChildren();
        for (Node subfieldBox : subfieldBoxes) {
            CheckBox c = (CheckBox) subfieldBox;
            c.setSelected(false);
        }
        textSearch.setText("");
    }

    @FXML
    private void testFunction() {
        LOGGER.log(Level.INFO, "{0}Entered test function!{1}%n",
                new Object[]{Ansi.CYAN, Ansi.RESET});
        BooksClient client = new BooksClient("http://localhost:3306/rest");
        client.getBook(1);
    }
}
