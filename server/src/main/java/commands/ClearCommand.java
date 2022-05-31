package commands;


import utils.CollectionManager;

/**
 * class for command 'clear' to clear collection
 */
public class ClearCommand extends AbstractCommand{
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }


    @Override
    public String execute() {
        collectionManager.clearCollection();
        return "Collection was successfully cleared";
    }
}
