package de.medieninformatik.server.database;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Book;
import de.medieninformatik.server.database.utils.BookQueryResult;
import de.medieninformatik.server.database.utils.SQL;

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

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
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

    private static final String DELETE_BOOK = """
            DELETE FROM book WHERE ID = %d;
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
            ResultSet rs = statement.executeQuery("SELECT count(*) FROM book");
            while (rs.next()) {
                System.out.println(rs.getInt("count(*)"));
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create statement from SQl!");
            e.printStackTrace();
        }
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
                List<Integer> authors = getAuthorsForBook(queryBook.id());
                List<Integer> subfields = Objects.requireNonNull(
                        getSubfieldsForBook(queryBook.id()),
                        String.format("List of subfields for book %d is null!", queryBook.id()));
                allBooksDB.add(new Book(
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
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get all books!%s",
                    Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        return allBooksDB;
    }

    static List<Integer> getAuthorsForBook(int id) {
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

    static List<Integer> getSubfieldsForBook(int id) {
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

    public static Book getBook(int id) {
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
            List<Integer> authors = getAuthorsForBook(queryBook.id());
            List<Integer> subfields = getSubfieldsForBook(queryBook.id());
            return new Book(
                    queryBook.id(),
                    queryBook.title(),
                    queryBook.publisher(),
                    queryBook.publicationdate(),
                    queryBook.pages(),
                    queryBook.isbn(),
                    authors,
                    subfields,
                    queryBook.rating()
            );
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get book with id %d!%s",
                    Ansi.RED, id, Ansi.RESET));
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteBook(int id) {
        try {
            int initialSize = count("book");
            statement.execute(String.format(DELETE_BOOK, id));
            int newSize = count("book");
            if (initialSize > newSize) return true;
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to get delete book!%s",
                    Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addBook(Book book) {
        String bookSQL = String.format(
                SQL.ADD_BOOK,
                book.id(),
                book.title(),
                book.publisher(),
                book.publicationDate(),
                book.pages(),
                book.isbn(),
                book.rating()
        );
        try {
            statement.execute(bookSQL);
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database#addBooks(): Error while executing SQL to add new book!");
            e.printStackTrace();
        }

        for (int i = 0; i < book.authors().size(); i++) {
            int currentSize = count("author");

            try {
                //statement.execute(String.format(SQL.ADD_AUTHOR), book.authors().get(i))
                //TODO: Add authors -> NewAuthor & NewBook?
                String bookAuthorSQL = String.format(
                        SQL.ADD_BOOK_AUTHORS, book.id(), book.authors().get(i));
                statement.execute(bookAuthorSQL);
            }
            catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error while executing SQL to add bookAuthors!");
                e.printStackTrace();
            }
        }

        for (int i = 0; i < book.subfields().size(); i++) {
            String bookSubsSQL = String.format(
                    SQL.ADD_BOOK_SUBFIELDS, book.id(), book.subfields().get(i));
            try {
                statement.execute(bookSubsSQL);
            }
            catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error while executing SQL to add bookSubfields!");
                e.printStackTrace();
            }
        }
        return false;
    }

    private static int count(String tableName) {
        int size = 0;
        try {
            ResultSet rs = statement.executeQuery(String.format("SELECT count(*) FROM %s", tableName));
            while (rs.next()) {
                size = rs.getInt("count(*)");
            }
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("%sCould not execute SQL to count elements in table!%s",
                    Ansi.RED, Ansi.RESET));
            e.printStackTrace();
        }
        return size;
    }
}
