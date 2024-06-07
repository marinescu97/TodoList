package com.marinescumonica.todolist;

import com.marinescumonica.todolist.datamodel.TodoData;
import com.marinescumonica.todolist.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class represents the controller of the mainWindow.fxml file.
 */
public class MainController {
    /**
     * The list view of the items.
     * */
    @FXML
    private ListView<TodoItem> todoListView;

    /**
     * The {@link TextArea} with the item's details.
     */
    @FXML
    private TextArea detailsTextArea;

    /**
     * The {@link Label} for the item's deadline.
     */
    @FXML
    private Label deadLineLabel;

    /**
     * The main container.
     */
    @FXML
    private BorderPane mainBorderPane;

    /**
     * The {@link ContextMenu} of the window.
     */
    @FXML
    private ContextMenu listContextMenu;

    /**
     * The {@link Button} for adding a new item.
     */
    @FXML
    private Button fileBtn;

    /**
     * A {@link ToggleButton} to filter the items.
     */
    @FXML
    private ToggleButton filterToggleButton;

    /**
     * A {@link FilteredList} of items.
     */

    private FilteredList<TodoItem> filteredList;

    /**
     * A {@link Predicate} for showing all items.
     */
    private Predicate<TodoItem> wantAllItems;

    /**
     * A {@link Predicate} for showing today's items.
     */
    private Predicate<TodoItem> wantTodaysItems;

    /**
     * Initializes the main window.
     */
    public void initialize(){
        Image img = new Image(new File("src/main/resources/media/newFile.png").toURI().toString());
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(30);
        imgView.setPreserveRatio(true);
        fileBtn.setGraphic(imgView);
        fileBtn.setTooltip(new Tooltip("Add a new todo item"));

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(actionEvent -> {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        });
        listContextMenu.getItems().addAll(deleteMenuItem);
        todoListView.getSelectionModel().selectedItemProperty().addListener((observableValue, todoItem, t1) -> {
            if (t1!=null){
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                detailsTextArea.setText(item.getDetail());
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                deadLineLabel.setText(df.format(item.getDeadLine()));
            }
        });

        wantAllItems = item -> true;

        wantTodaysItems = item -> (item.getDeadLine().equals(LocalDate.now()));
        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), wantAllItems);

        SortedList<TodoItem> sortedList = new SortedList<>(filteredList, Comparator.comparing(TodoItem::getDeadLine));

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean b) {
                        super.updateItem(todoItem, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText(todoItem.getShortDescription());
                            if (todoItem.getDeadLine().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if (todoItem.getDeadLine().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }

    /**
     * Shows the dialog for adding a new item.
     */
    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new todo item");
        dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            DialogController controller = fxmlLoader.getController();
            TodoItem newItem = controller.processResult();
            todoListView.getSelectionModel().select(newItem);
        }
    }

    /**
     * Deletes an item from the list.
     * @param item the item to be deleted
     */
    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete todo item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

    /**
     * Deletes a selected item from the list when the “Delete” key is pressed.
     * @param keyEvent The key event to be pressed when the item is selected from the list.
     */
    @FXML
    public void handleDeleteKey(KeyEvent keyEvent){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        if (selectedItem != null){
            if (keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
    }

    /**
     * Handles the functionality of the "Today's Items" button.
     * When the button is clicked once, it will be displayed the today's items.
     * When the button is clicked again, it will be displayed all items.
     */
    @FXML
    public void handleFilterButton(){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItems);
            if (filteredList.isEmpty()){
                detailsTextArea.clear();
                deadLineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(wantAllItems);
            if (filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        }
    }

    /**
     * This method handles the functionality of the “Exit” button.
     * It stops the application.
     */
    @FXML
    public void handleExit(){
        Platform.exit();
    }
}