import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader read;
    private BufferedWriter write;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = read.readLine();
            clientHandlers.add(this);
            broadcastMessage("Nadya(Server): " + clientUsername + " has entered the conversation");
        } catch (IOException e) {
            closeEverything(socket, read, write);
        }
    }

    @Override
    public void run() {
        String msgFromClient;

        while(socket.isConnected()) {
            try {
                msgFromClient = read.readLine();
                System.out.println(msgFromClient);
                broadcastMessage(msgFromClient);
            } catch (IOException e) {
                closeEverything(socket, read, write);
                break;
            }
        }
    }

    public void broadcastMessage(String msgToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.write.write(msgToSend);
                    clientHandler.write.newLine();
                    clientHandler.write.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, read, write);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("Nadya(Server): " + clientUsername + " has left the conversation");
    }

    public void closeEverything(Socket socket, BufferedReader read, BufferedWriter write) {
        removeClientHandler();
        try {
            if (read != null) read.close();
            if (write != null) write.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
