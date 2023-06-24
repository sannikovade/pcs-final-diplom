import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    protected static final int PORT = 8989;
    protected static final String LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(LOCALHOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("Type in the word");
            String word = scanner.nextLine();
            out.println(word);
            System.out.println(in.readLine());
        } catch (IOException e) {
            System.out.println("Client failed");
            e.printStackTrace();
        }
    }
}
