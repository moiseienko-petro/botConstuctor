package bot.configs;
import bot.model.MenuItem;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuConfig that = (MenuConfig) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(menuItems, that.menuItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, menuItems);
    }
}
