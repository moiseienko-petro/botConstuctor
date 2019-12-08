package bot.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuItem {
    private String name;
    private String path;
    private String message;
    private List<MenuItem> menuItems;
    private String action;
    private String actionMessage;
    private String text;
    private String successMessage;
    private String errorMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(name, menuItem.name) &&
                Objects.equals(path, menuItem.path) &&
                Objects.equals(message, menuItem.message) &&
                Objects.equals(menuItems, menuItem.menuItems) &&
                Objects.equals(action, menuItem.action) &&
                Objects.equals(actionMessage, menuItem.actionMessage) &&
                Objects.equals(text, menuItem.text) &&
                Objects.equals(successMessage, menuItem.successMessage) &&
                Objects.equals(errorMessage, menuItem.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, message, menuItems, action, actionMessage, text, successMessage, errorMessage);
    }
}
