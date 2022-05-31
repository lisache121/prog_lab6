package main;
import client.Client;
import utils.ClientRequestManager;

public class Main {
    public static void main(String[] args) {
        //args = new String[]{"100"};
        int port = 0;
        int TIMEOUT = 0;
        Client cl;
        if (args.length != 2) {
            cl = new Client(3008, 1000);
        }
        try {
            port = Integer.parseInt(args[0]);
            TIMEOUT = Integer.parseInt(args[1]);
            cl = new Client(port, TIMEOUT);
        } catch (NumberFormatException e){
            cl = new Client(3008, 1000);
        }
        //cl = new Client(3007);
        cl.run();
        ClientRequestManager manager = new ClientRequestManager(cl);
        manager.interactiveMod("");
    }
}
