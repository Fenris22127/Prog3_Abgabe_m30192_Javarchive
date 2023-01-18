package de.medieninformatik.client;

import java.util.List;

public record Filter(String textSearch, List<Integer> subfields) {
}
