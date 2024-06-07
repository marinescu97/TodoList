package com.marinescumonica.todolist;

import com.marinescumonica.todolist.datamodel.TodoData;
import com.marinescumonica.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * This class represents the controller for the todoItemDialog.fxml file.
 * It adds a new item to the list.
 */
public class DialogController {
    /**
     * A {@link TextField} for entering the description of the item.
     */
    @FXML
    private TextField shortDescriptionField;

    /**
     * A {@link TextArea} for entering the item's details.
     */
    @FXML
    private TextArea detailsArea;

    /**
     * A {@link DatePicker} for selecting the deadline.
     */
    @FXML
    private DatePicker deadlinePicker;

    /**
     * Adds the new item to the list.
     * @return the new item
     */
    public TodoItem processResult(){
        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, details, deadline);
        TodoData.getInstance().addTodoItem(newItem);

        return newItem;
    }
}
