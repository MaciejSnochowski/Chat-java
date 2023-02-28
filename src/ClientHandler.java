import DatabaseConnection.Communication;
import DatabaseConnection.Database;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable{

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    static String []pytania=new String[]{"Które medium transmisyjne należy zastosować, aby połączyć dwa punkty dystrybucyjne odległe od siebie o 600 m?"
            ,"Która usługa serwerowa zapewnia automatyczną konfigurację parametrów sieciowych stacji roboczych?",""};
    String  odpowiedz= "";//array list
    String[]  odpowiedzi= new String[]{"A. skrętkę STP  B. przewód koncentryczny C. światłowód D. skrętkę UTP","A. skrętkę STP  B. przewód koncentryczny C. światłowód D. skrętkę UTP","KONIEC"};//array list
    ArrayList<String >arrayList= new ArrayList<String>();
    Database database;
    int counter=0;

    public ClientHandler(Socket socket, Database database){
      this.database=database;

        this.clientSocket=socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);//wysyłanie
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//czytanie

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public ArrayList<String> call() {
        ArrayList<String> clientMsg=new ArrayList<>();
        System.out.println("new student appear");



        try {
            while(true) {
                if(odpowiedzi[counter]=="KONIEC"){
                    System.out.println("User ended");
                    clientMsg.add("KONIEC");
                    break;
                }
                out.println(pytania[counter]+"\n "+odpowiedzi[counter++]);


                String msg= in.readLine();
                clientMsg.add(msg);
                System.out.println("I've got a message");
            }





        }catch (IOException | SQLException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Koniec calla ");
            out.close();
            try {
                in.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  clientMsg;
    }
}
