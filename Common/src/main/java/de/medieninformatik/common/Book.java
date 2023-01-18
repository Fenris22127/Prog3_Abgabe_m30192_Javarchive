package de.medieninformatik.common;

import java.io.Serializable;
import java.util.List;

/**
 * Ein Record um die Bücher zu speichern und mit JSON serialisierbar zu machen.
 *
 * @param id              Ein {@link Integer}: Die ID des Buches, welches der Primary Key in der Datenbank ist
 * @param title           Ein {@link String}: Der Titel des Buches
 * @param publisher       Ein {@link String}: Der Verlag des Buches
 * @param publicationDate Ein {@link Integer}: Das Veröffentlichungsdatum des Buches
 * @param pages           Ein {@link Integer}: Die Seitenzahl des Buches
 * @param isbn            Ein {@link String}: Die ISBN des Buches
 * @param authors         Eine {@link List Liste} von {@link Integer}: Die Autoren des Buches
 * @param subfields       Eine {@link List Liste} von {@link Integer}: Die Subfelder des Buches
 * @param rating          Ein {@link Double}: Die Bewertung des Buches
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
public record Book(
        Integer id,
        String title,
        String publisher,
        Integer publicationDate,
        Integer pages,
        String isbn,
        List<Integer> authors,
        List<Integer> subfields,
        Double rating
) implements Serializable {
}
