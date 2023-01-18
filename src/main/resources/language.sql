CREATE TABLE IF NOT EXISTS `BookLanguages` (
    BookID int NOT NULL,
    LanguageID int NOT NULL
                                           );

CREATE TABLE IF NOT EXISTS `Language` (
    ID int NOT NULL,
    Name varchar(255) NOT NULL,
    PRIMARY KEY (ID)
                                      );

ALTER TABLE `booklanguages`
    ADD CONSTRAINT `LanguageFK`
        FOREIGN KEY (`LanguageID`)
            REFERENCES `language`(`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT `BookLangFK`
        FOREIGN KEY (`BookID`)
            REFERENCES `book`(`ID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

DROP TABLE IF EXISTS `Language`;

DROP TABLE IF EXISTS `BookLanguages`;