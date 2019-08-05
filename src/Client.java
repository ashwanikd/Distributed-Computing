import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket client;
    static DataInputStream in;
    static DataOutputStream out;
    static String command;
    static String respond;
    public static void main(String args[]){
        String address = "";
        Scanner scan  = new Scanner(System.in);
        try {
            client = new Socket("172.16.2.70", 9000);
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("handshake");
            //in.wait();
            respond = in.readUTF();
            System.out.println(respond);

            while (true){
                System.out.print(">>>");
                command = scan.nextLine();
                out.writeUTF(command);
                respond = in.readUTF();
                System.out.println(respond);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
