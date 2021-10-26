//Author: Nadiya Amanda Rizkania

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main (String[] args) {
        Socket socket = null;

        try {
            //Initialize Port & Connect to Server Socket
            int serverPort = 7896;
            socket = new Socket(args[1], serverPort);

            //Initialize write var for send ip to server
            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //Get & Send IP To Server
            InetAddress ia = InetAddress.getLocalHost();
            String ip = ia.getHostAddress();
            write.write(ip);
            write.newLine();
            write.flush();

            //Initialize out var for send message to server
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Send Message To Server
            out.writeUTF(args[0]);

            //Initialize read var for get received message from server
            BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Received Message From Server
            String msg = read.readLine();
            System.out.println(">> " + msg);

            //Close Socket
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("UH: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null) try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}