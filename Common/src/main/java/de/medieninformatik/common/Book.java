package de.medieninformatik.server;

import java.io.Serializable;
import java.util.List;

public record Book(
        Integer id,
        String title,
        String publisher,
        Integer publicationDate,
        Integer pages,
        String isbn,
        List<Integer> authors,
        List<Integer> subfields,
        Double rating
) implements Serializable {}
