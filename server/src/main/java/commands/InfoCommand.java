package commands;


import exceptions.EmptyCollectionException;
import utils.CollectionManager;

/**
 * class for command 'info' to print information about collection
 */
public class InfoCommand extends AbstractCommand{
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     *
     * @return if command successfully executed
     */
    @Override
    public String execute(){
        try{

            if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            return "Type of collection: " + collectionManager.getType() +'\n' + "Date of initialisation: " +
                    collectionManager.getTime() + '\n' + "Number of elements: " + collectionManager.getArraySize();

        } catch (EmptyCollectionException e) {
            return e.getMessage();
        }
    }
}
