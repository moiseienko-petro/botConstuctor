package bot.rest;

import bot.messageSender.MessageSender;
import bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sendMessage")
public class InformationController {

    @Autowired
    private MessageSender messageSender;

    @PostMapping("/{userId}")
    public void sendMessage(@PathVariable Long userId, @RequestBody Message message) {
        messageSender.sendMessageToUser(userId, message);
    }

    @PostMapping
    public void sendMessageToAll(@RequestBody Message message) {
        messageSender.sendMessageToAll(message);
    }
}
