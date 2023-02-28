import DatabaseConnection.Database;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Server
{
    static final int PORT=9999;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private BufferedReader keyboard;
    private  static ArrayList<ArrayList<String>> odpowiedzi=new ArrayList<>();
    private  static List<Future<ArrayList<String>>> futureTaskArrayList=new ArrayList<>();
    private  ExecutorService executorService = Executors.newFixedThreadPool(250);

    public static Database dataBase= new Database();

    Server() throws IOException {
        System.out.println("Server started");
        keyboard= new BufferedReader(new InputStreamReader(System.in));

        serverSocket= new ServerSocket(PORT);
        while(true){
            String srv=keyboard.readLine();
            if(srv.equals("end")){
                break;
            }
            clientSocket = serverSocket.accept();
            System.out.println("Server got connection");

            futureTaskArrayList.add(executorService.submit(new ClientHandler(clientSocket,dataBase)));

            List<Thread> threads = new ArrayList<>();
            for (Future<ArrayList<String>> f: futureTaskArrayList) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (f.isDone()!=true){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println("Otrzymano wszystkie odpowiedzi");
                        try {
                            System.out.println(f.get());
                            odpowiedzi.add(f.get());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            }

        }
        stop();
        for (ArrayList<String> odp:odpowiedzi
        ) {
            System.out.println("tablica odpowiedzi: "+odp.get(0)+"\t"+odp.get(1)+"\t"+odp.get(2)+"\n");

        }




    }


    public void stop() throws IOException {
        keyboard.close();

        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Server server= new Server();

    }


}