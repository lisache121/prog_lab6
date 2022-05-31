package commands;


import data.Dragon;
import exceptions.EmptyCollectionException;
import utils.CollectionManager;

/**
 * class for command 'remove_head' to remove first element
 */
public class RemoveHeadCommand extends AbstractCommand{
    private final CollectionManager collectionManager;


    public RemoveHeadCommand(CollectionManager collectionManager) {
        super("remove_head", "вывести первый элемент коллекции и удалить его");
        this.collectionManager = collectionManager;
    }

    /**
     *
     * @return if command successfully executed
     */
    @Override
    public String execute(){
        try{
            if (collectionManager.getCollection().size()==0) throw new EmptyCollectionException();
            Dragon d = collectionManager.getCollection().getFirst();
            collectionManager.removeFromCollection(d);
            return ("Element removed successfully.");

        } catch (EmptyCollectionException e){
            return e.getMessage();
        }
    }
}
