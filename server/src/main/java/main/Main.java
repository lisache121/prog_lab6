package main;

import exceptions.ConnectionException;
import log.Log;
import server.Server;
import utils.CollectionManager;
import utils.Parser;
import utils.CommandManager;

public class Main {
    public static void main(String[] args) {
        int port = 0;
//        args = new String[]{"100"};
        Server s = new Server();
        try{
            if (args.length != 1) {
                port = 3008;
            }
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                s.startConnection(3008);
            }
        }catch(ConnectionException e){
            Log.logger.error(e.getMessage());
        }

        s.startConnection(port);

        Parser p = new Parser();
        String path = System.getenv().get("COLLECTION");
        CollectionManager collectionManager = new CollectionManager(p, path);
        CommandManager manager = new CommandManager(collectionManager);
        manager.run(s);
    }
}
