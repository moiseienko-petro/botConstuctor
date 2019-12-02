package bot.configs;
import bot.model.MenuItem;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:botConfig.yml")
@ConfigurationProperties(prefix = "menu")
public class MenuConfig {
    private String message;
    private List<MenuItem> menuItems;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
