package de.medieninformatik.server.database;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Subfield;
import de.medieninformatik.server.database.utils.SQL;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    /**
     * Erstellt einen {@link Logger} für diese Klasse
     */
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    private static final Connection connection = DBModel.getInstance().getConnection();

    private static Statement statement;

    public static void start() {
        try {
            statement = connection.createStatement();
            LOGGER.log(Level.INFO, "{0}Connection to database established!{1}\n",
                    new Object[]{Ansi.GREEN, Ansi.RESET});
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database#main(): Error while accessing the database");
            e.printStackTrace();
        }
        dropAll();
        createTables();
        addData();
    }

    private static void createTables() {
        try {
            statement.execute(SQL.CREATE_BOOK_TABLE);
            statement.execute(SQL.CREATE_BOOK_SUBFIELDS_TABLE);
            statement.execute(SQL.CREATE_SUBFIELDS_TABLE);
            statement.execute(SQL.CREATE_BOOK_AUTHORS_TABLE);
            statement.execute(SQL.CREATE_AUTHORS_TABLE);
        }
        catch (SQLException se) {
            if (se.getClass().equals(SQLTimeoutException.class)) {
                LOGGER.log(Level.SEVERE, "Database#createTables(): Timeout while executing statement!");
            }
            if (se.getClass().equals(SQLSyntaxErrorException.class)) {
                LOGGER.log(Level.SEVERE, "Error in SQL-Syntax!");
            }
            else {
                LOGGER.log(Level.SEVERE, "Error while accessing the database");
            }
            se.printStackTrace();
        }
    }

    private static void addData() {
        addBooks();
        addAuthors();
        addSubfields();
        addForeignKeys();
        LOGGER.log(Level.INFO, "{0}---- Finished setting up database ----{1}\n",
                new Object[]{Ansi.GREEN, Ansi.RESET});
        try {
            statement.close();
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not close connection!");
            e.printStackTrace();
        }
    }

    private static void addBooks() {
        LOGGER.log(Level.INFO, "{0}Adding books{1}\n", new Object[]{Ansi.CYAN, Ansi.RESET});
        List<Book> books = Data.createBookList();
        for (Book book : books) {
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
                LOGGER.log(Level.SEVERE, "Database#addBooks(): Error while executing SQL to add books!");
                e.printStackTrace();
            }

            for (int i = 0; i < book.authors().size(); i++) {
                String bookAuthorSQL = String.format(
                    SQL.ADD_BOOK_AUTHORS, book.id(), book.authors().get(i));
                try {
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
        }
        LOGGER.log(Level.INFO, "{0}Added books, books authors and books subfields{1}\n",
                new Object[]{Ansi.GREEN, Ansi.RESET});
    }

    private static void addAuthors() {
        LOGGER.log(Level.INFO, "{0}Adding authors{1}\n", new Object[]{Ansi.CYAN, Ansi.RESET});
        List<Author> authors = Data.createAuthorList();
        for (Author author : authors) {
            String authorSQL = String.format(
                    SQL.ADD_AUTHOR,
                    author.id(),
                    author.firstName(),
                    author.lastName()
            );
            try {
                statement.execute(authorSQL);
            }
            catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error while executing SQL to add authors!");
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "{0}Added authors{1}\n", new Object[]{Ansi.GREEN, Ansi.RESET});
    }

    private static void addSubfields() {
        LOGGER.log(Level.INFO, "{0}Adding subfields{1}\n", new Object[]{Ansi.CYAN, Ansi.RESET});
        List<Subfield> subfields = Data.createSubfieldList();
        for (Subfield subfield : subfields) {
            String subfieldSQL = String.format(
                    SQL.ADD_SUBFIELD,
                    subfield.id(),
                    subfield.name()
            );
            try {
                statement.execute(subfieldSQL);
            }
            catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database#addSubfields(): Error while executing SQL to add subfields!");
                e.printStackTrace();
            }
        }
        LOGGER.log(Level.INFO, "{0}Added subfields{1}\n", new Object[]{Ansi.GREEN, Ansi.RESET});
    }

    private static void addForeignKeys() {
        LOGGER.log(Level.INFO, "{0}Adding foreign keys{1}\n", new Object[]{Ansi.CYAN, Ansi.RESET});
        try {
            statement.execute(SQL.ADD_FK_BOOK_AUTHORS);
            statement.execute(SQL.ADD_FK_BOOK_SUBFIELDS);
            LOGGER.log(Level.INFO, "{0}Added foreign keys{1}\n", new Object[]{Ansi.GREEN, Ansi.RESET});
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database#addForeignKeys(): Error while executing SQL to add foreign keys!");
            e.printStackTrace();
        }
    }

    private static void dropAll() {
        String delBook = "DROP TABLE IF EXISTS `Book`";
        String delSub = "DROP TABLE IF EXISTS `Subfield`";
        String delAuth = "DROP TABLE IF EXISTS `Author`";

        String delBookSub = "DROP TABLE IF EXISTS `BookSubfields`";
        String delBookAuth = "DROP TABLE IF EXISTS `BookAuthors`";

        try {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0;");
            statement.execute(delBook);
            statement.execute(delSub);
            statement.execute(delAuth);
            statement.execute(delBookSub);
            statement.execute(delBookAuth);
            statement.execute("SET FOREIGN_KEY_CHECKS = 1;");
        }
        catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "{0}Exception while executing SQL to drop all tables!{1}",
                    new Object[]{Ansi.RED, Ansi.RESET});
            e.printStackTrace();
        }
    }
}