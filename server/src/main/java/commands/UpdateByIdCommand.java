package commands;

import data.Dragon;
import exceptions.CommandExecutingException;
import exceptions.EmptyCollectionException;
import utils.CollectionManager;

/**
 * class for command 'update' to update element in collection by id
 */
public class UpdateByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;


    public UpdateByIdCommand(CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    /**
     *
     *@return if command successfully executed
     */
    @Override
    public String execute() {
        Long id = Long.parseLong(getArgs().getArguments());
        try {
            if (collectionManager.getCollection().size()==0) throw new EmptyCollectionException();
            Dragon d = collectionManager.getById(id);
            if (d == null) throw new CommandExecutingException("there is no element with id " + id + " in collection");

            collectionManager.removeFromCollection(d);
            collectionManager.addToCollection(getArgs().getDragon());
            d.setId(id);
            return ("Dragon with id " + id + " successfully updated");


        } catch (EmptyCollectionException e) {
            return e.getMessage();
        }

    }
}
