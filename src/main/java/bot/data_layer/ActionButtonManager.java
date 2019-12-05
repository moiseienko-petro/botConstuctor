package bot.data_layer;

import bot.data_layer.model.ActionButton;
import bot.data_layer.model.ActionItem;
import bot.data_layer.repository.ActionButtonRepository;
import bot.data_layer.repository.ActionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActionButtonManager {

    @Autowired
    private ActionItemRepository actionItemRepository;

    @Autowired
    private ActionButtonRepository actionButtonRepository;

    @Transactional
    public Long saveActionButton(ActionButton actionButton){
         actionItemRepository.save(actionButton.getRequest());
         actionItemRepository.save(actionButton.getOppositeRequest());
         ActionButton saved = actionButtonRepository.save(actionButton);
         return saved.getId();
    }

    @Transactional
    public void deleteActionButtonsForUser(Integer telegramId){
        actionButtonRepository.deleteActionButtonByTelegramId(telegramId);
    }

    public ActionButton getActionButton(Long id) {
        return actionButtonRepository.getActionButton(id);
    }

    public void replaceRequestInButton(Long id) {
        ActionButton actionButton = actionButtonRepository.getActionButton(id);
        ActionItem request = actionButton.getRequest();
        ActionItem oppositeRequest = actionButton.getOppositeRequest();

        actionButton.setRequest(oppositeRequest);
        actionButton.setOppositeRequest(request);

        actionButtonRepository.save(actionButton);
    }
}
