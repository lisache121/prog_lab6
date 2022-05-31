package commands;


import data.Dragon;
import exceptions.EmptyCollectionException;
import utils.CollectionManager;

import java.util.List;

/**
 * class for command 'show' to show elements in collection
 */
public class ShowCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        try {
            List<Dragon> list = collectionManager.showCollection();
            return "Elements in collection "+ '\n' + list.stream().map(e -> e.toConsole()).reduce("", (a, b)->a + b + "\n");
        } catch (EmptyCollectionException e) {
            return e.getMessage();
        }
    }
}
