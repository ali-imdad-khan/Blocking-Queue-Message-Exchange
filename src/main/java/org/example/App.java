package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class App {

    // Setting up the logger
    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        log.info("CREATING BLOCKING QUEUES FOR COMMUNICATION SIMULATION");
        BlockingQueue<String> player1ToPlayer2 = new LinkedBlockingQueue<>(); // Queue from Player 1 to Player 2
        BlockingQueue<String> player2ToPlayer1 = new LinkedBlockingQueue<>(); // Queue from Player 2 to Player 1

        log.info("READY THE PLAYERS...");
        Player player1 = new Player("INITIATOR", player1ToPlayer2, player2ToPlayer1);
        Player player2 = new Player("RECEIVER", player2ToPlayer1, player1ToPlayer2);

        log.info(String.format("STARTING THREADS FOR %s and %s", player1.getName(), player2.getName()));
        Thread player1Thread = new Thread(player1);
        Thread player2Thread = new Thread(player2);
        player1Thread.start();
        player2Thread.start();

        try {
            log.info("BEGIN MESSAGING SIMULATION");
            String concatenatedMessage = ""; // Holds concatenated messages

            for (int i = 1; i <= 10; i++) {
                // Send message from INITIATOR
                String message = String.format("MESSAGE #%d FROM %s", i, player1.getName());
                concatenatedMessage = concatenatedMessage.isEmpty() ? message : concatenatedMessage + " -> " + message;
                log.info(String.format("MAIN - INITIATOR SENT: %s", concatenatedMessage));
                player1ToPlayer2.put(concatenatedMessage);

                // Wait for the response from RECEIVER
                String response = player2ToPlayer1.take();
                concatenatedMessage = concatenatedMessage + " | " + response; // Add the response to the concatenation
                log.info(String.format("MAIN - INITIATOR RECEIVED: %s", concatenatedMessage));
            }

            // Send termination signal to the RECEIVER
            log.info(String.format("MAIN - %s SENDING TERMINATION SIGNAL", player1.getName()));
            player1ToPlayer2.put("TERMINATE");

        } catch (InterruptedException e) {
            log.warning("MAIN PROCESS INTERRUPTED ABRUPTLY");
            Thread.currentThread().interrupt();
        } finally {
            log.info(String.format("STOPPING BOTH PLAYERS %s AND %s GRACEFULLY", player1.getName(), player2.getName()));
            player1.stop();
            player2.stop();
            try {
                player1Thread.join();
                player2Thread.join();
            } catch (InterruptedException e) {
                log.warning("FAILED TO TERMINATE THREADS CLEANLY.");
            }
        }
    }
}