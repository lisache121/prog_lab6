package utils;

import commands.*;
import data.Dragon;
import exceptions.CommandExecutingException;
import exceptions.WrongAmountOfElementsException;
import interaction.CommandRequest;
import interaction.ResponseMessage;
import interaction.Status;
import log.Log;
import server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    private Map<String, AbstractCommand> commands = new HashMap<>();
    private CollectionManager collectionManager;
    private Parser p= new Parser();
    private Scanner scanner;
    private Server server;

    public CommandManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void init(Map<String, AbstractCommand> commands){
        commands.put("add", new AddCommand(collectionManager));
        commands.put("add_if_min", new AddIfMinCommand(collectionManager));
        commands.put("add_if_max", new AddIfMaxCommand(collectionManager));
        commands.put("filter_less_than_age", new FilterLessThanAgeCommand(collectionManager));
        commands.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commands.put("update", new UpdateByIdCommand(collectionManager));
        commands.put("remove_head", new RemoveHeadCommand(collectionManager));
        commands.put("max_by_description", new MaxByDescriptionCommand(collectionManager));
        commands.put("print_ascending", new PrintAscendingCommand(collectionManager));
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("show", new ShowCommand(collectionManager));
        commands.put("info", new InfoCommand(collectionManager));
        commands.put("save", new SaveCommand(collectionManager, p));
        commands.put("help", new HelpCommand(commands));
        commands.put("exit", new ExitCommand());
        commands.put("clear", new ClearCommand(collectionManager));
    }

    public ResponseMessage execute(CommandRequest request) throws WrongAmountOfElementsException {
        ResponseMessage res = new ResponseMessage();
        String name = request.getCommandName();
        if (commands.containsKey(name)) {
            try {
                AbstractCommand cmd = commands.get(name);
                cmd.setArgs(request);
                res.info(cmd.execute());
            }catch (CommandExecutingException e){
                res = new ResponseMessage(e.getMessage());
                res.setStatus(Status.ERROR);
            }
        } else {
            res.info("Your input doesn't match any command");
        }
        if (!name.equals("save") && !name.equals("exit")) {
            return res;
        }
        return null;
    }

    public void run(Server s) {
        init(commands);
        boolean processStatus = true;
        scanner = new Scanner(System.in);
        Runnable userInput = () -> {
            try {
                while (true) {
                    String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                    if (!userCommand[0].equals("save") && !userCommand[0].equals("exit")) {
                        Log.logger.error("There is no such command on server");
                        continue;
                    }
                    CommandRequest request = new CommandRequest(userCommand[0], userCommand[1], null);
                    AbstractCommand cmd = commands.get(userCommand[0]);
                    cmd.setArgs(request);
                    Log.logger.info(cmd.execute());
                }
            } catch (Exception e) {
                System.out.println("You have entered the end of file symbol. Program will be terminate.");
                System.exit(0);
            }
        };
        Thread thread = new Thread(userInput);
        thread.start();
        while (processStatus) {
            processStatus = processingClientRequest(s);
        }
    }

    private boolean processingClientRequest(Server s){
        CommandRequest request = null;
        try {
            while(true){
                request = s.receiveCommandRequest();
                s.sendResponse(execute(request));
            }
        } catch (WrongAmountOfElementsException e) {
            Log.logger.error("Error while working with client");
        }
        return true;
    }
}
