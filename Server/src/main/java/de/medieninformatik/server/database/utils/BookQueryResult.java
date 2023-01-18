package de.medieninformatik.server.database.utils;

public record BookQueryResult(
        int id,
        String title,
        String publisher,
        int publicationdate,
        int pages,
        String isbn,
        double rating
){}
