package de.medieninformatik.server;

import de.medieninformatik.server.database.Database;

public class Startup {
    public static void main(String[] args) {
        Database.start();
    }
}
