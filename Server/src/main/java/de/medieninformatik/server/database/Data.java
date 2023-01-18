package de.medieninformatik.server.database;

import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Subfield;

import java.util.List;

public class Data {
    protected static List<Book> createBookList() {
        return List.of(
                new Book(
                        1,
                        "Clean Code: A Handbook of Agile Software Craftsmanship",
                        "Pearson",
                        2007,
                        464,
                        "9780132350884",
                        List.of(12),
                        List.of(14, 4, 19, 17, 18, 11, 3, 6, 5, 15),
                        4.39
                ),
                new Book(
                        2,
                        "The Pragmatic Programmer: From Journeyman to Master",
                        "Addison-Wesley Professional",
                        1999,
                        321,
                        "9780201616224",
                        List.of(8, 19),
                        List.of(14, 4, 19, 11, 17, 18, 3, 5, 6, 16),
                        4.33
                ),
                new Book(
                        3,
                        "The Mythical Man-Month: Essays on Software Engineering",
                        "Addison-Wesley Professional",
                        1975,
                        322,
                        "9780201835953",
                        List.of(2),
                        List.of(14, 4, 11, 19, 9, 17, 2, 18, 5, 6),
                        4.01
                ),
                new Book(
                        4,
                        "The Information: A History, a Theory, a Flood",
                        "Knopf Doubleday Publishing Group",
                        2011,
                        527,
                        "9780375423727",
                        List.of(6),
                        List.of(16, 11, 7, 19, 12, 10, 4, 13, 2),
                        4.02
                ),
                new Book(
                        5,
                        "Design Patterns: Elements of Reusable Object-Oriented Software",
                        "Addison-Wesley Professional",
                        1994,
                        416,
                        "9780201633610",
                        List.of(5, 9, 20, 7),
                        List.of(14, 4, 19, 17, 18, 11, 15, 3, 5, 6),
                        4.19
                ),
                new Book(
                        6,
                        "Structure and Interpretation of Computer Programs",
                        "MIT Press",
                        1984,
                        657,
                        "9780262510875",
                        List.of(1, 17, 18),
                        List.of(14, 4, 19, 11, 17, 18, 5, 16, 3),
                        4.47
                ),
                new Book(
                        7,
                        "Introduction to Algorithms",
                        "MIT Press",
                        2001,
                        1180,
                        "9780262032933",
                        List.of(3, 11, 15, 16),
                        List.of(4, 14, 1, 19, 11, 18, 17, 16),
                        4.34
                ),
                new Book(
                        8,
                        "Code Complete",
                        "Microsoft Press",
                        1993,
                        914,
                        "9780735619678",
                        List.of(13),
                        List.of(14, 4, 19, 17, 18, 11, 3, 5, 15, 6),
                        4.3
                ),
                new Book(
                        9,
                        "Thinking in Java",
                        "Prentice Hall",
                        1998,
                        1401,
                        "9780131872486",
                        List.of(4),
                        List.of(14, 4, 19, 18, 11, 17, 15, 5, 3, 8),
                        4.15
                ),
                new Book(
                        10,
                        "The C Programming Language",
                        "Pearson",
                        1978,
                        288,
                        "9780131103627",
                        List.of(10, 14),
                        List.of(14, 4, 11, 15, 19, 18, 5, 17, 16),
                        4.43
                )
        );
    }

    protected static List<Author> createAuthorList() {
        return List.of(
                new Author(
                        1,
                        "Harold",
                        "Abelson"
                ),
                new Author(
                        2,
                        "Frederick P.",
                        "Brooks Jr."
                ),
                new Author(
                        3,
                        "Thomas H.",
                        "Cormen"
                ),
                new Author(
                        4,
                        "Bruce",
                        "Eckel"
                ),
                new Author(
                        5,
                        "Erich",
                        "Gamma"
                ),
                new Author(
                        6,
                        "James",
                        "Gleick"
                ),
                new Author(
                        7,
                        "Richard",
                        "Helm"
                ),
                new Author(
                        8,
                        "Andy",
                        "Hunt"
                ),
                new Author(
                        9,
                        "Ralph",
                        "Johnson"
                ),
                new Author(
                        10,
                        "Brian W.",
                        "Kernighan"
                ),
                new Author(
                        11,
                        "Charles E.",
                        "Leiserson"
                ),
                new Author(
                        12,
                        "Robert C.",
                        "Martin"
                ),
                new Author(
                        13,
                        "Steve",
                        "McConnell"
                ),
                new Author(
                        14,
                        "Dennis M.",
                        "Ritchie"
                ),
                new Author(
                        15,
                        "Ronald L.",
                        "Rivest"
                ),
                new Author(
                        16,
                        "Clifford",
                        "Stein"
                ),
                new Author(
                        17,
                        "Gerald Jay",
                        "Sussman"
                ),
                new Author(
                        18,
                        "Julie",
                        "Sussman"
                ),
                new Author(
                        19,
                        "Dave",
                        "Thomas"
                ),
                new Author(
                        20,
                        "John",
                        "Vlissides"
                )
        );
    }

    protected static List<Subfield> createSubfieldList() {
        return List.of(
                new Subfield(
                        1,
                        "Algorithms"
                ),
                new Subfield(
                        2,
                        "Business"
                ),
                new Subfield(
                        3,
                        "Coding"
                ),
                new Subfield(
                        4,
                        "Computer Science"
                ),
                new Subfield(
                        5,
                        "Computers"
                ),
                new Subfield(
                        6,
                        "Engineering"
                ),
                new Subfield(
                        7,
                        "History"
                ),
                new Subfield(
                        8,
                        "Informatics"
                ),
                new Subfield(
                        9,
                        "Management"
                ),
                new Subfield(
                        10,
                        "Mathematics"
                ),
                new Subfield(
                        11,
                        "Nonfiction"
                ),
                new Subfield(
                        12,
                        "Philosophy"
                ),
                new Subfield(
                        13,
                        "Popular Science"
                ),
                new Subfield(
                        14,
                        "Programming"
                ),
                new Subfield(
                        15,
                        "Reference"
                ),
                new Subfield(
                        16,
                        "Science"
                ),
                new Subfield(
                        17,
                        "Software"
                ),
                new Subfield(
                        18,
                        "Technical"
                ),
                new Subfield(
                        19,
                        "Technology"
                )
        );
    }
}
