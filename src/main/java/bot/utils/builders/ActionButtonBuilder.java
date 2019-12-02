package bot.utils.builders;

import bot.data_layer.model.ActionButton;
import bot.data_layer.model.ActionItem;

public class ActionButtonBuilder {

    private ActionButton actionButton;

    public ActionButton build() {
        return actionButton;
    }

    public ActionButtonBuilder() {
        actionButton = new ActionButton();
    }

    public ActionButtonBuilder setRequest(ActionItem actionItem) {
        actionButton.setRequest(actionItem);
        return this;
    }

    public ActionButtonBuilder setOppositeRequest(ActionItem actionItem) {
        actionButton.setOppositeRequest(actionItem);
        return this;
    }

    public ActionButtonBuilder setSuccessMessage(String message) {
        actionButton.setSuccessMessage(message);
        return this;
    }

    public ActionButtonBuilder setErrorMessage(String message) {
        actionButton.setErrorMessage(message);
        return this;
    }
}
