import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {


    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader keyboard;
    private BufferedReader in;
    private String textClient;
    private String textServer;
    Client() throws IOException {
        serverSocket = new Socket("127.0.0.1", Server.PORT);//tworzy socket do połączenia z serwerem
        out = new PrintWriter(serverSocket.getOutputStream(), true); // odpowiedz od klienta
        keyboard= new BufferedReader(new InputStreamReader(System.in)); // wpisywanie z klawiatury
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())); //czytanie od serwera
        while(true){
            textServer=in.readLine()+in.readLine();
            if(textServer.equals("nullnull")){

                break;
            }
            System.out.println("O to pytanie:");
            System.out.println("[SERVER] "+textServer);
            System.out.print(">> ");
            textClient =keyboard.readLine();
            System.out.println("message send");

            if(textClient.equals("quit")){
                System.out.println("You have been disconnected...");
                break;
            }
            out.println(textClient);


        }
        stopConnection();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        serverSocket.close();
        System.out.println("Sayonara");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();

    }
}
