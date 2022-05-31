package commands;


import utils.CollectionManager;

/**
 * class for command 'add' to add elements to collection
 */
public class AddCommand extends AbstractCommand{
    private final CollectionManager collectionManager;


    public AddCommand(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;

    }

    /**
     *
     * @return if command successfully executed
     *      * @throws WrongAmountOfElementsException if number of arguments is not as expected
     */
    @Override
    public String execute() {
        collectionManager.addToCollection(getArgs().getDragon());
        return "Element was successfully added to collection " + getArgs().getDragon().toConsole();
    }


}
