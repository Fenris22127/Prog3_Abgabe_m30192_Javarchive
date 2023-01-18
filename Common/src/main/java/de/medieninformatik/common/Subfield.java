package de.medieninformatik.common;

import java.io.Serializable;

/**
 * Ein Record um die Subfelds der Bücher zu speichern und mit JSON serialisierbar zu machen.
 *
 * @param id   Ein {@link Integer}: Die ID des Subfelds, welches der Primary Key in der Datenbank ist
 * @param name Ein {@link String}: Die Bezeichnung des Subfelds
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
public record Subfield(
        Integer id,
        String name
) implements Serializable {
}
