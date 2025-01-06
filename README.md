# Message Exchange Simulation

A simple Java project simulating communication between two players (`INITIATOR` and `RECEIVER`) using threads and blocking queues.

This project demonstrates a clean and dynamic design for inter-thread communication where:
- Messages are sent from an initiator to a receiver.
- Responses are concatenated and logged by the initiator.
- The simulation terminates gracefully after a predefined number of exchanges.

---

## Features

- Two dynamically named players (`INITIATOR` and `RECEIVER`).
- Inter-thread communication using `BlockingQueue`.
- Concatenated message exchange logged for the initiator.
- Graceful termination using a special "TERMINATE" signal.
- Minimal dependencies: Pure Java with no external libraries.

---

## How It Works

1. The **initiator** sends messages to the **receiver**.
2. The **receiver** processes each message and sends a response back to the **initiator**.
3. Each response is appended to a concatenated message log maintained by the **initiator**.
4. After 10 message exchanges, the initiator sends a termination signal to stop the receiver.
5. Both threads terminate gracefully.

---

## Installation and Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/MessageExchangeSimulation.git
   cd MessageExchangeSimulation
