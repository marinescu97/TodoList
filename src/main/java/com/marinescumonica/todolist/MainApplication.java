package com.marinescumonica.todolist;

import com.marinescumonica.todolist.datamodel.TodoData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the main class of the TodoList Application.
 * It extends the {@link Application} class.
 */
public class MainApplication extends Application {
    /**
     * Initializes the primary stage of the application.
     * @param stage The primary stage.
     * @throws IOException If the .fxml file is not found or cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Todo List");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method launches the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * This method stops the application and stores the items' list to the file.
     */
    @Override
    public void stop() {
        try {
            TodoData.getInstance().storeTodoItems();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method initializes the application and loads the data from the file.
     */
    @Override
    public void init() {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch (IOException e){
            System.out.println("Couldn't load todo items");
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }
}