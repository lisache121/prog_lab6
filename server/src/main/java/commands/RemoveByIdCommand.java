package commands;


import data.Dragon;
import exceptions.CommandExecutingException;
import exceptions.EmptyCollectionException;
import utils.CollectionManager;

/**
 * class for command 'remove_by_id
 */
public class RemoveByIdCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private long id;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }

    /**
     *
     * @return if command successfully executed
     */
    @Override
    public String execute(){

        try {
            if (collectionManager.getCollection().size()==0) throw new EmptyCollectionException();
            Long id = Long.parseLong(getArgs().getArguments());
            Dragon d = collectionManager.getById(id);
            if (d == null) throw new CommandExecutingException("Element with id " + id + " does not exist");
            collectionManager.removeFromCollection(d);
            collectionManager.sort();
            return ("Element with id " + id + " was successfully removed.");
        } catch (EmptyCollectionException e) {
            return e.getMessage();
        }

    }
}
