import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerClientProcess implements Runnable {

    int System;
    Thread t;
    Socket client;
    DataInputStream in;
    DataOutputStream out;
    String message;
    Message tempMessage;
    Message currentMessage;
    int conlimit;
    boolean responcesent;

    ServerClientProcess(Socket socket){
        System.out.println("Connection request: "+socket.getInetAddress().getHostName());
        t = new Thread(this,socket.getInetAddress().getHostName());
        client = socket;
        responcesent = false;
    }

    public void run(){
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            while(true) {
                if(!client.isConnected()){
                    break;
                }
                message = in.readUTF();

                if(message == "exit"){
                    break;
                }
                // sending request to server thread
                System.out.println("message : "+message);
                if (message != null && message != "") {
                    synchronized (ServerThreads.Commands) {
                        ServerThreads.Commands.add(new Message(client.getInetAddress().getHostName(), client.getPort(), message));
                    }
                    responcesent = false;

                    // sending back responce to client
                    while (!responcesent) {
                        t.join(15);
                        synchronized (ServerClient.Responds) {
                            if (ServerClient.Responds.size() > 0) {
                                if (ServerClient.Responds.peek().name == client.getInetAddress().getHostName()) {
                                    currentMessage = ServerClient.Responds.remove();
                                    out.writeUTF(currentMessage.message);
                                    responcesent = true;
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
