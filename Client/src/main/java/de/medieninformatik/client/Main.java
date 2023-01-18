package de.medieninformatik.client;

import de.medieninformatik.common.Book;

import java.util.List;
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
        final String baseUri = "http://localhost:3306/rest";
        userInput(baseUri);
    }

    private static void userInput(String baseUri) {
        BooksClient client = new BooksClient(baseUri);
        Scanner sc = new Scanner(System.in);
        Book book = new Book(
                0,
                "test",
                "test",
                3000,
                42,
                "test",
                List.of(0,0,0,0),
                List.of(0,0,0,0),
                4.04);

        while (true) {
            System.out.println("""
                    What would you like to do?
                    (1) See books
                    (2) Get a certain book
                    (3) Create a book
                    (4) Edit a book
                    (5) Delete a book
                    """);
            int ans = sc.nextInt();
            switch (ans) {
                case 1 -> client.getAllBooks();
                case 2 -> client.getBook(ans);
                case 3 -> {
                    System.out.println("Title?");
                    String name = sc.next();
                    client.createBook(book);
                }
                case 4 -> client.editBook(book);
                case 5 -> client.deleteBook(book);
                default -> System.out.printf(
                        "%d is not a valid input!%nValid answers: Numbers between and including 1-5.%n",
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
}
