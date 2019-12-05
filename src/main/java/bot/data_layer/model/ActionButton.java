package bot.data_layer.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Transactional
public class ActionButton {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(cascade = CascadeType.REMOVE)
    private ActionItem request;
    @OneToOne(cascade = CascadeType.REMOVE)
    private ActionItem oppositeRequest;

    private String successMessage;
    private String errorMessage;
    private Integer telegramId;

    public ActionButton() {
    }

    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionItem getRequest() {
        return request;
    }

    public void setRequest(ActionItem request) {
        this.request = request;
    }

    public ActionItem getOppositeRequest() {
        return oppositeRequest;
    }

    public void setOppositeRequest(ActionItem oppositeRequest) {
        this.oppositeRequest = oppositeRequest;
    }
}
