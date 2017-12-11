package org.inlm5.client.controller;


import android.os.Build;
import android.support.annotation.RequiresApi;

import org.inlm5.client.net.Client;
import org.inlm5.client.net.OutputHandler;
import org.inlm5.client.start.MainActivity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private Client client;
    private boolean connected = false;
    private boolean isGameOngoing = false;
    private MainActivity view;

    public Controller(MainActivity view) {
        this.view = view;
    }

    public boolean isConnected() {
        return connected;
    }

    public synchronized void appendText(String text) {
        if(isGameOngoing) {
            view.runOnUiThread(() -> {
                view.appendText(text);
            });
        }
    }

    public synchronized void appendNewConnection(String text) {
        view.runOnUiThread(() -> {
            view.appendText(text);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void connect(String ip, int port, OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            disconnect();
            client = new Client(ip, port, outputHandler);
            try {
                client.connect();
                connected = true;
            } catch (IOException ie) {
                appendText("Failed to establish connection.");
                try {
                    client.disconnect();
                    connected = false;
                } catch (Exception e) {
                    System.out.println("Cleanup failed.");
                }
            }
        }).thenRun(() -> outputHandler.handleNewConnection("Connected to " + ip + ":" + port));
    }

    public void disconnect() {
        CompletableFuture.runAsync(() -> {
            if(connected) {
                try {
                    client.disconnect();
                    connected = false;
                    appendText("Disconnected.");
                } catch (IOException e) {
                    view.runOnUiThread(() -> {
                        view.showToast("Failed to disconnect.");
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendGuessMessage(String guess) {
        CompletableFuture.runAsync(() -> {
            try {
                if(isGameOngoing) {
                    appendText("Guess: " + guess);
                    client.sendGuessMessage(guess);
                } else {
                    view.runOnUiThread(() -> {
                        view.showToast("Start a new game.");
                    });
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendNewGameMessage() {
        CompletableFuture.runAsync(() -> {
            try {
                if(!isGameOngoing) {
                    client.sendNewGameMessage();
                    isGameOngoing = true;
                } else {
                    view.runOnUiThread(() -> {
                        view.showToast("Game already ongoing.");
                    });
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void connect(String ip, int port) {
        connect(ip,port,new ViewOutput());
    }

    private class ViewOutput implements OutputHandler {

        @Override
        public void handleNewConnection(String message) {
            appendNewConnection(message);
        }

        @Override
        public void handleMessage(String message) {
            appendText(message);
        }

        @Override
        public void handleGameOver() {
            appendText("Game over, start a new game to play again.");
            isGameOngoing = false;
        }
    }

}
