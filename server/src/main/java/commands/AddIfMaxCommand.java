package commands;

import exceptions.CommandExecutingException;
import utils.CollectionManager;

/**
 * class for command 'add_if_max' add element if is greater than other elements in collection
 */
public class AddIfMaxCommand extends AbstractCommand{
    private final CollectionManager collectionManager;


    public AddIfMaxCommand(CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        if (collectionManager.addIfMax(getArgs().getDragon())){
            return "Element with id " + getArgs().getDragon().getId() + " was successfully added to collection";
        }
        else throw new CommandExecutingException("Element is not greater than maximum element in collection");
    }
}
