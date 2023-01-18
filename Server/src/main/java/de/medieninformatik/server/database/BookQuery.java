package de.medieninformatik.server.database;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Subfield;
import jakarta.ws.rs.core.Link;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BookQuery {

    private static final Logger LOGGER = Logger.getLogger(BookQuery.class.getName());

    private static final Connection connection = DBModel.getInstance().getConnection();

    private static Statement statement;
    private static final String ALL_BOOKS = """
            SELECT * from book;
            """;

    private static final String GET_BOOK_BY_ID = """
            SELECT * from book WHERE ID = %d;
            """;

    private static final String GET_AUTHORS_FOR_BOOK = """
            SELECT a.ID
            FROM bookauthors ba
                     INNER JOIN author a
                                ON ba.AuthorID = a.ID
            WHERE ba.BookID = %d;
            """;

    private static final String GET_SUBFIELDS_FOR_BOOK = """
            SELECT
                s.ID
            FROM booksubfields bs
                     INNER JOIN subfield s
                                ON bs.SubfieldID = s.ID
            WHERE bs.BookID = %d;
            """;

    public BookQuery() {
        try {
            statement = connection.createStatement();
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create statement from SQl!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            statement = connection.createStatement();
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create statement from SQl!");
            e.printStackTrace();
        }
        getAllBooks();
    }

    public static List<Book> getAllBooks() {
        List<Book> allBooksDB = new LinkedList<>();
        List<BookQueryResult> bookQueryResult = new LinkedList<>();
        try {
            //get all books and add them to a list of BookQueryResult books
            ResultSet rs = statement.executeQuery(ALL_BOOKS);
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
                List<Integer> authors = getAuthorsForBook(queryBook.id);
                List<Integer> subfields = Objects.requireNonNull(
                        getSubfieldsForBook(queryBook.id),
                        String.format("List of subfields for book %d is null!", queryBook.id));
                allBooksDB.add(new Book(
                        queryBook.id,
                        queryBook.title,
                        queryBook.publisher,
                        queryBook.publicationdate,
                        queryBook.pages,
                        queryBook.isbn,
                        authors,
                        subfields,
                        queryBook.rating
                ));
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get all books!%s",
                    Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        return allBooksDB;
    }

    private static List<Integer> getAuthorsForBook(int id) {
        List<Integer> authors = new LinkedList<>();
        try {
            ResultSet rs = statement.executeQuery(String.format(GET_AUTHORS_FOR_BOOK, id));
            while (rs.next()) {
                authors.add(rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get authors for book with id %d!%s",
                    Ansi.RED, id, Ansi.RESET));
            e.printStackTrace();
        }
        if (authors.isEmpty()) {
            throw new IllegalStateException(
                    String.format("List of authors for book %d is empty!", id));
        }
        return Objects.requireNonNull(authors, String.format("List of authors for book %d must not be null!", id));
    }

    private static List<Integer> getSubfieldsForBook(int id) {
        List<Integer> subfields = new LinkedList<>();
        try {
            ResultSet rs = statement.executeQuery(String.format(GET_SUBFIELDS_FOR_BOOK, id));
            while (rs.next()) {
                subfields.add(rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get subfields for book with id %d!%s",
                    Ansi.RED, id, Ansi.RESET));
            e.printStackTrace();
        }
        if (subfields.isEmpty()) {
            throw new IllegalStateException(
                    String.format("List of subfields for book %d is empty!", id));
        }
        return Objects.requireNonNull(subfields, String.format("List of subfields for book %d must not be null!", id));
    }

    public Book getBook(int id) {
        try {
            ResultSet rs = statement.executeQuery(String.format(GET_BOOK_BY_ID, id));
            BookQueryResult queryBook = null;
            while (rs.next()) {
                queryBook = new BookQueryResult(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("publisher"),
                rs.getInt("publicationdate"),
                rs.getInt("pages"),
                rs.getString("isbn"),
                rs.getDouble("rating")
                );
            }
            assert queryBook != null;
            List<Integer> authors = getAuthorsForBook(queryBook.id);
            List<Integer> subfields = getSubfieldsForBook(queryBook.id);
            return new Book(
                    queryBook.id,
                    queryBook.title,
                    queryBook.publisher,
                    queryBook.publicationdate,
                    queryBook.pages,
                    queryBook.isbn,
                    authors,
                    subfields,
                    queryBook.rating
            );
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get book with id %d!%s",
                    Ansi.RED, id, Ansi.RESET));
            e.printStackTrace();
        }
        return null;
    }

    private record BookQueryResult(
            int id,
            String title,
            String publisher,
            int publicationdate,
            int pages,
            String isbn,
            double rating
    ){}
}
