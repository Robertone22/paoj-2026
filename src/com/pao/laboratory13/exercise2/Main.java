package com.pao.laboratory13.exercise2;

import com.pao.laboratory13.exercise1.ProtocolEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 9000;
    private static final int CLIENT_COUNT = 2;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch clientsDone = new CountDownLatch(CLIENT_COUNT);

        Thread serverThread = new Thread(() -> runServer(serverReady, clientsDone));
        serverThread.start();

        serverReady.await();

        Thread client1 = new Thread(() -> runClient(
                "CLIENT-1",
                List.of(
                        "AUTH alice",
                        "OPEN",
                        "SEND hello from alice",
                        "HISTORY",
                        "CLOSE"
                )
        ));

        Thread client2 = new Thread(() -> runClient(
                "CLIENT-2",
                List.of(
                        "AUTH bob",
                        "OPEN",
                        "BROADCAST message from bob",
                        "SEND private message",
                        "HISTORY",
                        "CLOSE"
                )
        ));

        client1.start();
        client2.start();

        client1.join();
        client2.join();

        clientsDone.await();
        serverThread.join();

        System.out.println("[MAIN] Demo finished.");
    }

    private static void runServer(CountDownLatch serverReady, CountDownLatch clientsDone) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Listening on port " + PORT);
            serverReady.countDown();

            for (int i = 1; i <= CLIENT_COUNT; i++) {
                Socket clientSocket = serverSocket.accept();
                int clientNumber = i;

                System.out.println("[SERVER] Client " + clientNumber + " connected.");

                executorService.submit(() ->
                        handleClient(clientSocket, "SESSION-" + clientNumber, clientsDone)
                );
            }

            clientsDone.await();

            executorService.shutdown();
            System.out.println("[SERVER] All clients done. Shutting down.");

        } catch (IOException e) {
            System.out.println("[SERVER] IO error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[SERVER] Interrupted.");
        }
    }

    private static void handleClient(Socket socket, String sessionName, CountDownLatch clientsDone) {
        ProtocolEngine protocolEngine = new ProtocolEngine();

        try (
                Socket clientSocket = socket;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                String response = protocolEngine.process(line);

                if (response != null) {
                    writer.println(response);
                    System.out.println("[" + sessionName + "] " + line + " => " + response);
                }
            }

            System.out.println("[" + sessionName + "] Disconnected.");

        } catch (IOException e) {
            System.out.println("[" + sessionName + "] Error: " + e.getMessage());
        } finally {
            clientsDone.countDown();
        }
    }

    private static void runClient(String clientName, List<String> commands) {
        try (
                Socket socket = new Socket("localhost", PORT);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("[" + clientName + "] Connected.");

            for (String command : commands) {
                writer.println(command);

                String response = reader.readLine();
                System.out.println("[" + clientName + "] >> " + command + " => " + response);

                Thread.sleep(100);
            }

            System.out.println("[" + clientName + "] Disconnected.");

        } catch (IOException e) {
            System.out.println("[" + clientName + "] IO error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + clientName + "] Interrupted.");
        }
    }
}