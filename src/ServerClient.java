import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ServerClient implements Runnable {

    Thread t;
    public static int clientport = 9000;
    ServerSocket clientserversocket;
    Socket client;
    DataInputStream in;
    DataOutputStream out;
    String message;
    Message tempMessage;
    public static int maxconnections = 50;
    int conlimit=0;

    // responces will be saved in this list
    public static Queue<Message> Responds;

    ServerClient(){
        Responds = new LinkedList<>();
        t = new Thread(this,"Server client thread");
        try {
            clientserversocket = new ServerSocket(clientport, maxconnections);
            System.out.println("Server Client started ...");
        }catch (Exception e){
            System.out.println("Could not start Client Server ...");
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                System.out.println(t.getName());
                client = clientserversocket.accept();
                ServerClientProcess process = new ServerClientProcess(client);
                process.t.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
