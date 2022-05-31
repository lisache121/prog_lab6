package commands;


import exceptions.CommandExecutingException;
import utils.CollectionManager;

/**
 * command 'add_if_min' to add element if it will be minimum in collection
 */
public class AddIfMinCommand extends AbstractCommand{
    private final CollectionManager collectionManager;


    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min {element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }


    @Override
    public String execute(){
        if (collectionManager.addIfMin(getArgs().getDragon())){
            return "Element with id " + getArgs().getDragon().getId() + " was successfully added to collection";
        }
        else throw new CommandExecutingException("Element is not less than minimum element in collection");
    }
}
