package commands;

import exceptions.EmptyCollectionException;
import utils.CollectionManager;

/**
 * class for command 'max_by_description' to print element with max description
 */
public class MaxByDescriptionCommand extends AbstractCommand{
    private final CollectionManager collectionManager;

    public MaxByDescriptionCommand(CollectionManager collectionManager) {
        super("max_by_description", "вывести любой объект из коллекции, значение поля description которого является максимальным");
        this.collectionManager = collectionManager;

    }


    @Override
    public String execute(){
        try{
            if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
            return collectionManager.maxByDescription();
        } catch (EmptyCollectionException e) {
            return e.getMessage();
        }
    }
}
