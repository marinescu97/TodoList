package com.marinescumonica.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is a singleton class that handles the connection between the application,
 * and the TodoList.txt file, where the data is stored.
 */
public class TodoData {
    /**
     * The instance of the class.
     */
    private static final TodoData instance = new TodoData();

    /**
     * The filename where the data is stored.
     */
    private static final String filename = "TodoList.txt";

    /**
     * The list of to-do items.
     */
    private ObservableList<TodoItem> todoItems;

    /**
     * The date formatter.
     */
    private final DateTimeFormatter formatter;

    /**
     * Gets the instance of the class.
     * @return The instance of the class.
     */
    public static TodoData getInstance(){
        return instance;
    }

    /**
     * The constructor used to prevent instantiation outside the class.
     */
    private TodoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    /**
     * Gets the list of to-do items.
     * @return the list of to-do items
     */
    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    /**
     * Loads the list of to-do items from the file.
     * @throws IOException I/O exception that occurs when loading the list of to-do items from the file fails.
     */
    public void loadTodoItems() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);

                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                todoItems.add(todoItem);
            }
        }
    }

    /**
     * Stores the list of items in the file.
     * @throws IOException I/O exception that occurs when writing to the file fails.
     */
    public void storeTodoItems() throws IOException{
        Path path = Paths.get(filename);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (TodoItem item : todoItems) {
                bw.write(String.format("%s\t%s\t%s", item.getShortDescription(), item.getDetail().replace("\n", " "), item.getDeadLine().format(formatter)));
                bw.newLine();
            }
        }
    }

    /**
     * Adds a new to-do item to the list.
     * @param item the item to add
     */
    public void addTodoItem(TodoItem item){
        if (!item.getDeadLine().isBefore(LocalDate.now())){
            todoItems.add(item);
        }
    }

    /**
     * Deletes an item from the list.
     * @param item the item to be deleted
     */
    public void deleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }
}
