package commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import data.Dragon;
import utils.CollectionManager;
import utils.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * class for command 'save' to save
 */
public class SaveCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final Parser parser;

    public SaveCommand(CollectionManager collectionManager, Parser parser) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;

        this.parser =parser;
    }


    @Override
    public String execute() {
        try{

            ArrayDeque<Dragon> dragons = collectionManager.getCollection();
            String filename = collectionManager.getFilepath();
            File f = new File(filename);
            boolean flag = f.canWrite();
            if (!flag){
                File newFile = new File(System.getProperty("user.dir") + File.separator + "NewXml.xml");
                try
                {
                    boolean created = newFile.createNewFile();
                    if(created)
                        System.out.println("New file for saving collection has been created");
                    parser.writeStringToFile(newFile.getPath(), dragons);
                    return ("Collection was successfully saved to file " + newFile);
                }
                catch(IOException ex){

                    return ("File already exists");
                }
            } else{
                parser.writeStringToFile(filename, dragons);
                return ("Collection was successfully saved to file " + filename);
            }

        }catch (JsonProcessingException e) {
            System.out.println("Saving process failed.");
        } catch (IOException e) {
            System.out.println("Collection cannot be saved to file");
        }
        return null;
    }
}
