package utils;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.dataformat.xml.*;
import data.Coordinates;
import data.Dragon;
import data.DragonHead;
import exceptions.EmptyFieldException;
import exceptions.InvalidAgeInputException;
import exceptions.InvalidArgumentsOfCoordinatesException;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private String envVariable;
    XmlMapper xmlMapper = new XmlMapper();
    public Parser() {
    }

    public ArrayDeque<Dragon> fromXmlToCollectionManager(String xml) throws IOException {
        List<Dragon> res = new ArrayList<Dragon>() {};
        try{
            res =  xmlMapper.readValue(xml, new TypeReference<List<Dragon>>() {});
        }catch (InvalidFormatException e){
            System.out.println("Type mismatch");
            res.clear();
        }
        ArrayDeque<Dragon> ad = new ArrayDeque<>();
        for (Dragon d: res){
            try {
                Dragon dragonCheck = new Dragon();
                Coordinates coord = new Coordinates();
                DragonHead head = new DragonHead();
                dragonCheck.setName(d.getName());
                dragonCheck.setAge(d.getAge());
                coord.setX(d.getCoordinates().getX());
                coord.setY(d.getCoordinates().getY());
                head.setToothCount(d.getHead().getToothCount());
                head.setSize(d.getHead().getSize());
                head.setEyesCount(d.getHead().getEyesCount());
                dragonCheck.setColor(d.getColor());
                dragonCheck.setType(d.getType());
                dragonCheck.setDescription(d.getDescription());
                dragonCheck.setCoordinates(coord);
                dragonCheck.setHead(head);
                dragonCheck.setId(d.getId());
                dragonCheck.setDate(d.retDate());
                ad.add(dragonCheck);
            } catch(EmptyFieldException e){
                System.out.println("Name, coordinates and description must not empty. This dragon with id " + d.getId() + " will not be added to a collection.");
            } catch(InvalidAgeInputException e){
                System.out.println("Age must be greater than 0. This dragon with id " + d.getId() + " will not be added to a collection.");
            } catch(InvalidArgumentsOfCoordinatesException e){
                System.out.println("X coordinate must be greater than -417, y coordinate must be greater than -225. This dragon with id " + d.getId() + " will not be added to a collection.");
            }

        }


        if(!ad.isEmpty()){
            return ad;
        }else{
            System.out.println("The file contents isn't valid. The collection will be empty.");
            return new ArrayDeque<>();
        }

    }

    public String fromXmlToString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        File f = new File(path);
        Path fn = Paths.get(path);
        Scanner scanner = new Scanner(fn);

        scanner.useDelimiter(System.getProperty("line.separator"));
        while(scanner.hasNext()){
            sb.append(scanner.next());

        }
        scanner.close();

        return sb.toString();
    }

    public String toXmlString(ArrayDeque<Dragon> d) throws JsonProcessingException {


        Dragon dragon = new Dragon();
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String rootName = Dragon.class.getAnnotation(JsonRootName.class).value();

        return xmlMapper.writerWithDefaultPrettyPrinter().withRootName(rootName).writeValueAsString(d);
    }
    public void writeStringToFile(String filename, ArrayDeque<Dragon> dragons) throws IOException{

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
        writer.write("");
        writer.write(toXmlString(dragons));
        writer.close();
    }



}

