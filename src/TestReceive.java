import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TestReceive {

    public static void main(final String[] args) {

        try {
            new Socket().connect(new InetSocketAddress("127.0.0.1", 666), 5000);
            System.out.println("\033[0;32m" + "[v]" + "\033[0m" + " Port 666 is open!");
        } catch (final IOException ignored) {
            System.out.println("\033[0;31m" + "[x]" + "\033[0m" + " Port 666 is closed.");
        }
    }
}
