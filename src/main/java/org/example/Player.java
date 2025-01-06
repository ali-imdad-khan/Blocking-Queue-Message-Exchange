package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Player implements Runnable {

    private int sentMessageCount = 0; // Number of messages sent by the player
    private int receivedMessageCount = 0; // Number of messages received by the player

    private final String name; // Player name
    private final BlockingQueue<String> outputQueue; // Queue for sending responses
    private final BlockingQueue<String> inputQueue; // Queue for receiving messages
    private boolean running = true; // Boolean to control simulation

    // Setting up the logger
    private static final Logger log = Logger.getLogger(Player.class.getName());

    public Player(String name, BlockingQueue<String> inputQueue, BlockingQueue<String> outputQueue) {
        /**
         * Initializing an instance Player of class Player with:
         *
         * @param name Identifier of player
         * @param inputQueue Queue for holding for processing of input messages
         * @param outputQueue Queue for holding of messages to be sent as responses
         */
        log.info(String.format("INITIALIZING PLAYER %s ...", name));
        this.name = name;
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    public String getName() {
        return name;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                String incomingMessage = inputQueue.take();

                // Check for termination signal
                if ("TERMINATE".equals(incomingMessage)) {
                    log.info(String.format("%s - TERMINATION SIGNAL RECEIVED. STOPPING...", name));
                    break;
                }

                // Process message
                receivedMessageCount++;
                log.info(String.format("%s - RECEIVED MESSAGE: %s (Total Received: %d)", name, incomingMessage, receivedMessageCount));

                // Respond with acknowledgment (no concatenation here)
                String response = String.format("RESPONSE #%d FROM %s", receivedMessageCount, name);
                sentMessageCount++;
                log.info(String.format("%s - SENDING RESPONSE: %s (Total Sent: %d)", name, response, sentMessageCount));
                outputQueue.put(response);
            }
        } catch (InterruptedException e) {
            log.warning(String.format("INTERRUPTION IS STOPPING %s GRACEFULLY...", name));
            Thread.currentThread().interrupt();
        } finally {
            log.info(String.format("%s - FINAL SENT: %d, FINAL RECEIVED: %d", name, sentMessageCount, receivedMessageCount));
        }
    }
}
