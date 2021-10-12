import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter write;
    private BufferedReader read;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.write = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, read, write);
        }
    }

    public void sendMessage() {
        try {
            write.write(username);
            write.newLine();
            write.flush();
            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected()) {
                System.out.print("Send: ");
                String msgToSend = scanner.nextLine();
                write.write(username + ": " + msgToSend);
                write.newLine();
                write.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, read, write);
        }
    }

    public void receivedMessage() {
        new Thread(() -> {
            String msgFromAll;

            while(socket.isConnected()) {
                try {
                    msgFromAll = read.readLine();
                    System.out.println("\n" + msgFromAll);
                    System.out.print("Send: ");
                } catch (IOException e) {
                    closeEverything(socket, read, write);
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader read, BufferedWriter write) {
        try {
            if (read != null) read.close();
            if (write != null) write.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String ip = ia.getHostAddress();
            String hn = ia.getHostName();

            Scanner scanner = new Scanner(System.in);

            Socket socket = new Socket("192.168.43.14", 7896);
            System.out.print("Enter your username for the conversation: ");
            String username = scanner.nextLine();
            username = username + "(" + ip + " - " + hn + ")";

            Client client = new Client(socket, username);
            System.out.println("Welcome to the conversation " + username);
            client.receivedMessage();
            client.sendMessage();
        } catch (UnknownHostException e) {
            System.out.println("Sock: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Failed to connect, because Server was down or wrong IP/Port");
            System.out.println("IO: " + e.getMessage());
        }
    }
}