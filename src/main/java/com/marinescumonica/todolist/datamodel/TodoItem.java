package com.marinescumonica.todolist.datamodel;

import java.time.LocalDate;

/**
 * This class represents an item inserted in the to-do list.
 */
public class TodoItem {
    /**
     * The title of the item.
     */
    private String shortDescription;
    /**
     * The item's details.
     */
    private String detail;
    /**
     * The item's deadline.
     */
    private LocalDate deadLine;

    /**
     * Constructs a new TodoItem with the given parameters.
     * @param shortDescription the item's title
     * @param detail the item's details
     * @param deadLine the item's deadline
     */
    public TodoItem(String shortDescription, String detail, LocalDate deadLine) {
        this.shortDescription = shortDescription;
        this.detail = detail;
        this.deadLine = deadLine;
    }

    /**
     * Gets the item's title.
     * @return the title
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the item's title with a given value.
     * @param shortDescription the new title
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Gets the item's details.
     * @return the details
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the item's details with a given value.
     * @param detail the new details
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Gets the item's deadline.
     * @return the item's deadline
     */
    public LocalDate getDeadLine() {
        return deadLine;
    }

    /**
     * Sets the item's deadline with a given value.
     * @param deadLine the new deadline
     */
    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }
}
