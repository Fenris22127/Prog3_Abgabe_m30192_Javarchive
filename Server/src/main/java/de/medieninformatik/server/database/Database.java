package de.medieninformatik.server.database;

import de.medieninformatik.common.Ansi;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Subfield;

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
            LOGGER.log(Level.INFO, "{0}Connection to database established!{1}\n", new Object[]{Ansi.GREEN, Ansi.RESET});
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
        String createBookTable = """
                CREATE TABLE IF NOT EXISTS `Book` (
                ID int NOT NULL,
                Title varchar(255) NOT NULL,
                Publisher varchar(255),
                PublicationDate int(4),
                Pages int,
                ISBN varchar(13) NOT NULL,
                Rating DOUBLE PRECISION(3, 2),
                PRIMARY KEY (ID)
                );
                """;
        String createBookSubfieldsTable = """
                CREATE TABLE IF NOT EXISTS `BookSubfields` (
                    BookID int NOT NULL,
                    SubfieldID int NOT NULL
                );
                """;
        String createSubfieldsTable = """
                CREATE TABLE IF NOT EXISTS `Subfield` (
                ID int NOT NULL,
                Name varchar(255) NOT NULL,
                PRIMARY KEY (ID)
                );
                """;
        String createBookAuthorsTable = """
                CREATE TABLE IF NOT EXISTS `BookAuthors` (
                    BookID int NOT NULL,
                    AuthorID int NOT NULL
                );
                """;
        String createAuthorsTable = """
                CREATE TABLE IF NOT EXISTS `Author` (
                ID int NOT NULL,
                FirstName varchar(255),
                LastName varchar(255) NOT NULL,
                PRIMARY KEY (ID)
                );
                """;

        try {
            statement.execute(createBookTable);
            statement.execute(createBookSubfieldsTable);
            statement.execute(createSubfieldsTable);
            statement.execute(createBookAuthorsTable);
            statement.execute(createAuthorsTable);
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
            String bookSQL = String.format("""
                    INSERT INTO
                    book(id, title, publisher, publicationdate, pages, isbn, rating)
                    VALUES
                    (%d, '%s', '%s', %d, %d, '%s', %f);
                    """,
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
                String bookAuthorSQL = String.format("""
                    INSERT INTO
                    bookauthors(bookid, authorid)
                    VALUES
                    (%d, %d);
                    """, book.id(), book.authors().get(i));
                try {
                    statement.execute(bookAuthorSQL);
                }
                catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error while executing SQL to add bookAuthors!");
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < book.subfields().size(); i++) {
                String bookSubsSQL = String.format("""
                    INSERT INTO
                    booksubfields(bookid, subfieldid)
                    VALUES
                    (%d, %d);
                    """, book.id(), book.subfields().get(i));
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
            String authorSQL = String.format("""
                    INSERT INTO author(id, firstname, lastname)
                    VALUES
                    (%d, '%s', '%s')
                    """,
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
            String subfieldSQL = String.format("""
                    INSERT INTO subfield(id, name)
                    VALUES
                    (%d, '%s')
                    """,
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
        String addFKBookAuthor = """
                ALTER TABLE `bookauthors`
                    ADD CONSTRAINT `AuthorFK`
                        FOREIGN KEY (`AuthorID`)
                            REFERENCES `author`(`ID`)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                    ADD CONSTRAINT `BookAuthFK`
                        FOREIGN KEY (`BookID`)
                            REFERENCES `book`(`ID`)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE;
                """;
        String addFKBookSubfield = """
                ALTER TABLE `booksubfields`
                    ADD CONSTRAINT `SubfieldFK`
                        FOREIGN KEY (`SubfieldID`)
                            REFERENCES `subfield`(`ID`)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE,
                    ADD CONSTRAINT `BookSubFK`
                        FOREIGN KEY (`BookID`)
                            REFERENCES `book`(`ID`)
                            ON DELETE CASCADE
                            ON UPDATE CASCADE;
                """;

        try {
            statement.execute(addFKBookAuthor);
            statement.execute(addFKBookSubfield);
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