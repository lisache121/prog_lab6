package utils;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import data.Dragon;
import exceptions.EmptyCollectionException;

/**
 * class to work with collection's elements
 */
public class CollectionManager {
    private ArrayDeque<Dragon> dragons;
    private final Date time = new Date();
    private Comparator comparator = new Comparator();
    private Parser parser = new Parser();
    private String filepath;
    public CollectionManager(){
    }


    public CollectionManager (Parser parser, String filepath) {
        this.parser = parser;
        this.filepath = filepath;

        loadCollection();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setTime(Date time){
    }
    public void setMyArrayType(String arrayType){}
    public ArrayDeque<Dragon> getCollection() {
        return dragons;
    }



    public String getType(){
        return "ArrayDeque";
    }
    public int getArraySize(){
        return dragons.size();
    }
    public void addToCollection(Dragon dragon){
        dragon.setId(makeID());
        dragons.add(dragon);
    }

    public String getTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        return dateFormat.format(time);
    }


    public ArrayDeque<Dragon> getDragons() {
        return dragons;
    }

    public void setCollection(ArrayDeque<Dragon> dragons) {
        this.dragons = dragons;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     *
     * @return long value of created id
     */
    public long makeID(){
        if (dragons.isEmpty()) return 1L;
        return dragons.getLast().getId() + 1;
    }

    /**
     *
     * @return minimum age value of elements in collection
     */
    public boolean addIfMin(Dragon dragon){
        if (dragons.isEmpty()){ addToCollection(dragon);}
        if (dragons.stream()
                .min(Dragon::compareTo)
                .filter(w->w.compareTo(dragon)==-1)
                .isPresent())
        {
            return false;
        }
        addToCollection(dragon);
        return true;
    }

    public boolean addIfMax(Dragon dragon){
        if (dragons.stream()
                .max(Dragon::compareTo)
                .filter(w->w.compareTo(dragon)==1)
                .isPresent())
        {
            return false;
        }
        addToCollection(dragon);
        return true;
    }

    /**
     *
     * @param id of element
     * @return element of collection with this id
     */
    public Dragon getById(long id){
        return dragons.stream().filter(dragon -> dragon.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     *
     * @param d element of collection to remove
     */
    public void removeFromCollection(Dragon d){
        dragons.remove(d);
    }

    /**
     *
     * @return String representation of element with maximum description
     * @throws EmptyCollectionException if there is no elements to print
     */
    public String maxByDescription(){
        Dragon d = dragons
                .stream()
                .max(Comparator::compareByDescription).get();
        return d.toConsole();
    }

    /**
     *
     * @param age to filter by
     * @return List with elements with age less than this age
     */
    public List<Dragon> filterByAge(int age){
        List<Dragon> filteredList = dragons.stream().filter(w-> w.getAge()<age)
                .collect(Collectors.toList());;
        return filteredList;
    }

    /**
     *
     * @return ArrayList of sorted elements by age
     * @throws EmptyCollectionException if there is no elements in collection
     */
    public void sort() throws EmptyCollectionException {
        if (dragons.isEmpty()) throw new EmptyCollectionException();

        List<Dragon> list = dragons.stream().sorted(comparator).collect(Collectors.toList());
        clearCollection();
        dragons.addAll(list);
    }

    /**
     * prints elements in collection
     * @throws EmptyCollectionException if there is no elements in collection
     * @return
     */
    public List<Dragon> showCollection() throws EmptyCollectionException {
        if (dragons.isEmpty()) throw new EmptyCollectionException();
        sort();
        return dragons.stream().collect(Collectors.toList());
    }

    /**
     * clears collection
     */
    public void clearCollection(){
        dragons.clear();
    }


    private void loadCollection() {
        try {
            File f = new File(filepath);
            if(!f.isDirectory() && Files.isReadable(f.toPath())){
                dragons = parser.fromXmlToCollectionManager(parser.fromXmlToString(filepath));
                sort();
            }else{
                System.out.println("There is no rights for reading file. Change rights and run program again!");
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("File path is wrong. Run program again with correct filename.");
            System.exit(0);
        } catch(NoSuchElementException e){
            System.out.println("You have entered the end of file symbol. Program will be stopped.");
            System.exit(0);
        } catch (EmptyCollectionException e) {
            System.out.println("Collection is empty");
        }
    }

}
