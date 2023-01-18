package de.medieninformatik.server.database;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Book;
import de.medieninformatik.server.database.utils.BookQueryResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.medieninformatik.server.database.BookQuery.getAuthorsForBook;
import static de.medieninformatik.server.database.BookQuery.getSubfieldsForBook;

public class SubfieldQuery {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(SubfieldQuery.class.getName());

    private static final Connection connection = DBModel.getInstance().getConnection();

    private static final String GET_BOOKS_WITH_SUBFIELD = """
            SELECT
                b.ID, b.Title, b.Publisher, b.PublicationDate, b.Pages, b.ISBN, b.Rating,
                s.ID, s.Name
            FROM booksubfields bs
                     INNER JOIN book b
                                ON bs.BookID = b.ID
                     INNER JOIN subfield s
                                ON bs.SubfieldID = s.ID
            WHERE s.Name LIKE '%s'
            ORDER BY b.Title;
            """;

    private static Statement statement;

    public SubfieldQuery() {
        try {
            statement = connection.createStatement();
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create statement from SQl!");
            e.printStackTrace();
        }
    }

    public static List<Book> getBooksWithSubfield() {
        List<Book> booksWithSubfield = new LinkedList<>();
        List<BookQueryResult> bookQueryResult = new LinkedList<>();
        try {
            //get all books and add them to a list of BookQueryResult books
            ResultSet rs = statement.executeQuery(GET_BOOKS_WITH_SUBFIELD);
            while (rs.next()) {
                bookQueryResult.add(new BookQueryResult(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("publicationdate"),
                        rs.getInt("pages"),
                        rs.getString("isbn"),
                        rs.getDouble("rating")
                ));
            }
            //get authors and subfields for each queryBook and add them to the list of books
            for (BookQueryResult queryBook : bookQueryResult) {
                List<Integer> authors = getAuthorsForBook(queryBook.id());
                List<Integer> subfields = Objects.requireNonNull(
                        getSubfieldsForBook(queryBook.id()),
                        String.format("List of subfields for book %d is null!", queryBook.id()));
                booksWithSubfield.add(new Book(
                        queryBook.id(),
                        queryBook.title(),
                        queryBook.publisher(),
                        queryBook.publicationdate(),
                        queryBook.pages(),
                        queryBook.isbn(),
                        authors,
                        subfields,
                        queryBook.rating()
                ));
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get all books with subfield!%s",
                    Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        return booksWithSubfield;
    }
}
