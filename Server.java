//Author: Nadiya Amanda Rizkania

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public static void main (String[] args) {
        try {
            //Get Date & Time
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            //Initialize Port & Increment Message
            int serverPort = 7896;
            int angka = 1;

            //Create Server Socket
            ServerSocket serverSocket = new ServerSocket(serverPort);

            //Start ON Server
            System.out.println("Server ON (" + formatter.format(date) + ")");
            while(!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                new Connection(clientSocket, angka);
                angka++;
            }
        } catch (IOException e) {
            System.out.println("Server: " + e.getMessage());
        }
    }
}