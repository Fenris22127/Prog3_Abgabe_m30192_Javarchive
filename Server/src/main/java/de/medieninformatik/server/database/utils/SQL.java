package de.medieninformatik.server.database.utils;

public class SQL {
    public static final String CREATE_BOOK_TABLE = """
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
    public static final String CREATE_BOOK_SUBFIELDS_TABLE = """
                CREATE TABLE IF NOT EXISTS `BookSubfields` (
                    BookID int NOT NULL,
                    SubfieldID int NOT NULL
                );
                """;
    public static final String CREATE_SUBFIELDS_TABLE = """
                CREATE TABLE IF NOT EXISTS `Subfield` (
                ID int NOT NULL,
                Name varchar(255) NOT NULL,
                PRIMARY KEY (ID)
                );
                """;
    public static final String CREATE_BOOK_AUTHORS_TABLE = """
                CREATE TABLE IF NOT EXISTS `BookAuthors` (
                    BookID int NOT NULL,
                    AuthorID int NOT NULL
                );
                """;
    public static final String CREATE_AUTHORS_TABLE = """
                CREATE TABLE IF NOT EXISTS `Author` (
                ID int NOT NULL,
                FirstName varchar(255),
                LastName varchar(255) NOT NULL,
                PRIMARY KEY (ID)
                );
                """;
    public static final String ADD_BOOK =
            """
                    INSERT INTO
                    book(id, title, publisher, publicationdate, pages, isbn, rating)
                    VALUES
                    (%d, '%s', '%s', %d, %d, '%s', %f);
                    """;
    public static final String ADD_AUTHOR =
            """
                    INSERT INTO author(id, firstname, lastname)
                    VALUES
                    (%d, '%s', '%s')
                    """;
    public static final String ADD_SUBFIELD =
            """
                    INSERT INTO subfield(id, name)
                    VALUES
                    (%d, '%s')
                    """;
    public static final String ADD_BOOK_AUTHORS =
            """
                    INSERT INTO
                    bookauthors(bookid, authorid)
                    VALUES
                    (%d, %d);
                    """;
    public static final String ADD_BOOK_SUBFIELDS =
            """
                    INSERT INTO
                    booksubfields(bookid, subfieldid)
                    VALUES
                    (%d, %d);
                    """;

    public static final String ADD_FK_BOOK_AUTHORS = """
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

    public static final String ADD_FK_BOOK_SUBFIELDS = """
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

}
