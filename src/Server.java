import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static int serverport = 9111;
    public static int numofmachines = 4;
    ServerSocket serverSocket;
    DataInputStream in;
    DataOutputStream out;
    public static LinkedList<Message> responds;
    Message currentRespond;
    String currentCommand;
    Process p;
    static ServerThreads serverThreads;
    static ServerClient serverClient;

    Server(){

        try {
            // starting server client thread
            serverClient = new ServerClient();
            serverClient.t.start();

            // starting server threads
            serverThreads = new ServerThreads();
            serverThreads.t.start();

            // initializing server
            serverSocket = new ServerSocket(serverport, numofmachines);
            System.out.println("Server started...");
            Socket socket;

            while(true){
                socket = serverSocket.accept();
                System.out.println(socket.getInetAddress());
                in = new DataInputStream(socket.getInputStream());
                currentCommand = in.readUTF();

                if(currentCommand != "" && currentCommand != null) {
                    p = new Process();
                    p.m = new Message(socket.getInetAddress().getHostName(), socket.getPort(), currentCommand);
                    p.t.start();
                }
                if(currentRespond != null){
                    if(currentRespond.name == socket.getInetAddress().getHostName()){
                        out = new DataOutputStream(socket.getOutputStream());
                        out.writeUTF(currentRespond.message);
                    }
                }else if(responds.size()>0){
                    currentRespond = responds.pop();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
