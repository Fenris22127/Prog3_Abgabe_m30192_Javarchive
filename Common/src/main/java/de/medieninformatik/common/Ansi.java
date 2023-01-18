package de.medieninformatik.common;

/**
 * Beinhaltet einige Ansi-Codes, welche zur farblichen Hervorhebung von Logging Nachrichten dienen.
 *
 * @author Elisa Johanna Woelk (m30192)
 * @version 2.1
 * @since 17.0.5
 */
public class Ansi {
    /**
     * Setzt die Schriftfarbe der Konsole auf den Standard zurück.
     */
    public static final String RESET = "\u001B[0m";

    /**
     * Setzt die Schriftfarbe der Konsole auf Rot.
     */
    public static final String RED = "\u001B[31m";

    /**
     * Setzt die Schriftfarbe der Konsole auf Grün.
     */
    public static final String GREEN = "\u001B[32m";

    /**
     * Setzt die Schriftfarbe der Konsole auf Cyan.
     */
    public static final String CYAN = "\u001B[36m";
}
