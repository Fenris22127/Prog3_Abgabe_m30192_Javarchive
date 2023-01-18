CREATE TABLE IF NOT EXISTS `Book`
(
    ID              int          NOT NULL,
    Title           varchar(255) NOT NULL,
    Publisher       varchar(255),
    PublicationDate int(4),
    Pages           int,
    ISBN            varchar(13)  NOT NULL,
    Rating          DOUBLE PRECISION(3, 2),
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS `Subfield`
(
    ID   int          NOT NULL,
    Name varchar(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS `BookSubfields`
(
    BookID     int NOT NULL,
    SubfieldID int NOT NULL
);

CREATE TABLE IF NOT EXISTS `Author`
(
    ID        int          NOT NULL,
    FirstName varchar(255),
    LastName  varchar(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS `BookAuthors`
(
    BookID   int NOT NULL,
    AuthorID int NOT NULL
);

ALTER TABLE `bookauthors`
    ADD CONSTRAINT `AuthorFK`
        FOREIGN KEY (`AuthorID`)
            REFERENCES `author` (`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT `BookAuthFK`
        FOREIGN KEY (`BookID`)
            REFERENCES `book` (`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE `booksubfields`
    ADD CONSTRAINT `SubfieldFK`
        FOREIGN KEY (`SubfieldID`)
            REFERENCES `subfield` (`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT `BookSubFK`
        FOREIGN KEY (`BookID`)
            REFERENCES `book` (`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
