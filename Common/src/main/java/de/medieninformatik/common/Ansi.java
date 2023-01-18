package de.medieninformatik.common;

public class Ansi {
    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf den Standard zurück.
     */
    public static final String RESET = "\u001B[0m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Rot.
     */
    public static final String RED = "\u001B[31m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Grün.
     */
    public static final String GREEN = "\u001B[32m";

    /**
     * Ein ANSI Escape Code: Setzt die Schriftfarbe der Konsole auf Cyan.
     */
    public static final String CYAN = "\u001B[36m";
}
