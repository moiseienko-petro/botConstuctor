package bot.data_layer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ActionItem {

    @Id
    @GeneratedValue
    private Long id;
    private String action;
    private String method;
    private String message;


    public ActionItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionItem that = (ActionItem) o;
        return Objects.equals(id, that.id) &&
                action.equals(that.action) &&
                method.equals(that.method) &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, method, message);
    }
}
