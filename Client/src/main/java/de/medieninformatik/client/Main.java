package de.medieninformatik;

import java.util.Scanner;

/**
 * Führt Nutzereingaben aus.
 * @author Elisa Johanna Woelk (m30192)
 */
public class Main {

    /**
     * Legt die zu verwendende {@link java.net.URI URI} fest und ruft {@link #userInput(String) userInput()} auf.
     * @param args <-
     */
    public static void main(String[] args) {
        final String baseUri = "http://localhost:8080/rest";
        userInput(baseUri);
    }

    /**
     * Erlaubt es dem Nutzer, sich alle Sitze und Reservierungen anzeigen zu lassen, die Reservierung eines bestimmten
     * Sitzplatzes einzusehen, den Reservierungsstatus eines bestimmten Sitzplatzes zu prüfen, einen Sitzplatz zu
     * buchen oder die Reservierung eines Sitzplatzes zu löschen. <br>
     * Je nach Eingabe wird die entsprechende Methode über den {@link ReservationClient} ausgeführt und das Resultat
     * ausgegeben.
     * @param baseUri Ein {@link String}: Die zu verwendende {@link java.net.URI URI}
     */
    private static void userInput(String baseUri) {
        ReservationClient reservations = new ReservationClient(baseUri);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    What would you like to do?
                    (1) See all seats and their booking status
                    (2) Get the booking details for a certain seat
                    (3) Get the booking status for a certain seat
                    (4) Book a certain seat
                    (5) Get the booking details for all seats
                    (6) Delete the reservation for a certain seat
                    """);
            int ans = sc.nextInt();
            switch (ans) {
                case 1 -> reservations.getAllSeats(); //print all seats as either [ ] (free) or [X] (booked)
                case 2 -> {
                    int[] seatNr = getSeatNr(sc);
                    reservations.getReservation(seatNr[0], seatNr[1]); //get Reservation for seat in row x, column y
                }
                case 3 -> {
                    int[] seatNr = getSeatNr(sc);
                    reservations.hasReservation(seatNr[0], seatNr[1]); //get Reservation for seat in row x, column y
                }
                case 4 -> {
                    int[] seatNr = getSeatNr(sc);
                    System.out.printf("Under what name would you like to book seat %d-%d?%n", seatNr[0] + 1, seatNr[1] + 1);
                    String name = sc.next();
                    if (name.isBlank() || name.isEmpty()) {
                        System.out.println("Name must not be blank or empty!");
                        while (name.isBlank() || name.isEmpty()) {
                            System.out.printf("Under what name would you like to book seat %d-%d?",
                                    (seatNr[0] + 1), (seatNr[1] + 1));
                            name = sc.nextLine();
                        }
                    }
                    reservations.makeReservation(seatNr[0], seatNr[1], name); //get Reservation for seat in row x, column y
                }
                case 5 -> reservations.getAllReservations();
                case 6 -> {
                    int[] seatNr = getSeatNr(sc);
                    reservations.deleteReservation(seatNr[0], seatNr[1]);
                }
                default -> System.out.printf(
                        "%d is not a valid input!%nValid answers: Numbers between and including 1-6.%n",
                        ans);
            }
            System.out.printf("%n%nWould you like to do something else? (y/n)%n");
            String answer = sc.next().toLowerCase();
            if (!answer.equals("y")) {
                if (!answer.equals("n")) {
                    System.out.printf("Invalid input:%s%nValid input: 'y' or 'n'%nEnding programm.%n", answer);
                }
                break;
            }
        }
    }

    /**
     * Nimmt die Angaben des Nutzers zu der Reihe und Spalte des Sitzplatzes auf.
     * @param sc Ein {@link Scanner}: Der {@link Scanner} aus der {@link #userInput(String) userInput()}-Methode
     * @return Ein {@link Integer}-{@link java.lang.reflect.Array Array}: Die Reihe des Sitzes ist an der ersten
     * Stelle, die Spalte an der Zweiten
     */
    private static int[] getSeatNr(Scanner sc) {
        int[] seatNr = new int[2];
        System.out.println("In which row is the seat?");
        seatNr[0] = sc.nextInt() - 1;
        System.out.println("In which column is the seat?");
        seatNr[1] = sc.nextInt() - 1;
        return seatNr;
    }
}
