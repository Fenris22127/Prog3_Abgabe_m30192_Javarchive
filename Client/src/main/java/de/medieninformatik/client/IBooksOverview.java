package de.medieninformatik.common;

/**
 * Das Interface, welches die benötigten Methoden für Clients bereitstellt.
 * @author Elisa Johanna Woelk (m30192)
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
    //void createBook(Book book);
    void createBook(String title);
    /**
     * Verändert ein bereits existierendes Buch.
     * @param id Die ID des zu verändernden Buches
     * @param book Ein {@link Book}-Objekt: Die, am Buch vorzunehmenden Änderungen
     */
    //void editBook(int id, Book book);
    void editBook(int id, String title);

    /**
     * Löscht ein Buch.
     * @param id Die ID des zu löschenden Buches
     */
    void deleteBook(int id);
}
