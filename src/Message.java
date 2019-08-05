public class Message {
    String name;
    int port;
    String message;
    int System;
    Message(String clientname, int port){
        name = clientname;
        this.port = port;
    }
    Message(String clientname, int port, String message){
        name = clientname;
        this.port = port;
        this.message = message;
    }
}
