package de.medieninformatik.common;

import java.io.Serializable;

/**
 * Ein Record um die Autoren der Bücher zu speichern und mit JSON serialisierbar zu machen.
 *
 * @param id        Ein {@link Integer}: Die ID des Autors, welches der Primary Key in der Datenbank ist
 * @param firstName Ein {@link String}: Der Vorname des Autors
 * @param lastName  Ein {@link String}: Der Nachname des Autors
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
public record Author(
        Integer id,
        String firstName,
        String lastName
) implements Serializable {
}
