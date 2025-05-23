package client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class GameClient extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Consumer<String> messageHandler;

    public GameClient(String serverIp, int serverPort, Consumer<String> messageHandler) throws IOException {
        this.socket = new Socket(serverIp, serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.messageHandler = messageHandler;
    }

    public void send(String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                messageHandler.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
