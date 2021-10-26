//Author: Nadiya Amanda Rizkania

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    DataInputStream in;
    Socket clientSocket;
    int increment;
    public Connection(Socket clientSockets, int x) {
        try {
            //Initialize Client, in for received message from client & increment number from server
            clientSocket = clientSockets;
            in = new DataInputStream(clientSocket.getInputStream());
            increment = x;
            this.start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    public void run() {
        try {
            //Initialize read for read ip from client, write to send message back to client
            BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            //Get IP & Message From Client
            String ip = read.readLine();
            String data = in.readUTF();
            System.out.println("Koneksi #" + increment + "(" + ip + "): " + data);

            //Send Back Message To CLient
            write.write(increment + "_" + data);
            write.newLine();
            write.flush();
        } catch (UnknownHostException e) {
            System.out.println("UH: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}