package de.medieninformatik.client;

import de.medieninformatik.common.Book;

/**
 * Das Interface, welches die benötigten Methoden für Clients bereitstellt.
 *
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.5
 * @since 17.0.5
 */
public interface IBooksOverview {

    /**
     * Gibt alle Bücher zurück.
     */
    void getAllBooks();

    /**
     * Gibt ein Buch zurück.
     */
    void getBook(int id);

    /**
     * Erstellt ein neues Buch.
     * @param book Ein {@link Book}-Objekt: Das zu erstellende Buch
     */
    void createBook(Book book);

    /**
     * Verändert ein bereits existierendes Buch.
     * @param id Die ID des zu verändernden Buches
     * @param book Ein {@link Book}-Objekt: Die, am Buch vorzunehmenden Änderungen
     */
    void editBook(Book book);

    /**
     * Löscht ein Buch.
     * @param id Die ID des zu löschenden Buches
     */
    void deleteBook(Book book);
}
