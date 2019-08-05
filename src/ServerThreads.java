import java.net.*;
import java.util.LinkedList;

public class ServerThreads implements Runnable {

    String[] address;
    Socket[] machine;
    boolean check;
    boolean isconnected[];
    Thread t;
    int connectedNodes;

    // commands from server client thread will be added in the following list
    public static LinkedList<Message> Commands;
    Message currentMessage;
    String message,response;

    ServerThreads(){
        connectedNodes = 0;
        Commands = new LinkedList<>();
        t = new Thread(this,"Server Connections Thread");
        address = new String[Server.numofmachines];
        // server machines
        address[0] = "172.16.2.70";
        address[1] = "172.16.3.85";
        address[2] = "172.16.3.174";
        address[3] = "172.16.3.199";
        machine = new Socket[Server.numofmachines];
        check = false;
        isconnected = new boolean[Server.numofmachines];
    }

    @Override
    public void run() {
        while (!check) {
            // check if all servers are connected
            check = true;
            try {
                for (int i = 0; i < machine.length; i++) {
                    if (isconnected[i])
                        continue;
                    machine[i] = new Socket(address[i], Server.serverport);
                    if (machine[i].isConnected()) {
                        connectedNodes++;
                        System.out.println("connected to machine " + machine[i].getInetAddress());
                        isconnected[i] = true;
                    }
                }
            } catch (Exception e) {
                check = false;
            }
        }

        System.out.println("All machines are Connected ...");
        while(true) {
            synchronized (Commands) {
                if (ServerThreads.Commands.size() > 0) {
                    currentMessage = ServerThreads.Commands.pop();
                    message = currentMessage.message;
                    synchronized (ServerClient.Responds) {
                        if (message.equals("handshake")) {
                            currentMessage.message = "**********************************************************\n" +
                                    "*                                                        *\n" +
                                    "*          Welcome to Ashwani's Cluster Project          *\n" +
                                    "*                                                        *\n" +
                                    "**********************************************************\n";
                            currentMessage.message += "enter command \"nodesinfo\" for more information\n";
                            ServerClient.Responds.add(currentMessage);

                        } else if (message.equals("nodesinfo")) {
                            message = "Number of currently connect Nodes :" + connectedNodes + "\n\n";
                            for (int i = 0; i < machine.length; i++) {
                                if (isconnected[i]) {
                                    message = message + "machine " + i + " : " + address[i] + "\n";
                                }
                            }
                            currentMessage.message = message;
                            ServerClient.Responds.add(currentMessage);
                        }else {
                            currentMessage.message = "wrong inputs";
                            ServerClient.Responds.add(currentMessage);
                        }
                    }
                }
            }
        }
    }
}
