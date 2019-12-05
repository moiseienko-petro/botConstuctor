package bot.helper;

import bot.configs.BotConfig;
import bot.configs.MenuConfig;
import bot.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuHelper {

    @Autowired
    private MenuConfig menuConfig;

    @Autowired
    private BotConfig botConfig;

    public MenuItem getMenuItem(String name, boolean isRoot, boolean isBack) {
        if (isRoot) {
            return getRootMenuItem(isBack);
        } else {
            return getMenuItemByName(name, menuConfig.getMenuItems());
        }
    }

    private MenuItem getMenuItemByName(String name, List<MenuItem> items) {
        MenuItem result = null;
        if (items == null) {
            return null;
        }
        for (MenuItem menuItem : items) {
            if (menuItem.getName().equals(name)) {
                result = menuItem;
                break;
            } else {
                result = getMenuItemByName(name, menuItem.getMenuItems());
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    public MenuItem getParentLevel(String name) {
        List<MenuItem> menuItems = menuConfig.getMenuItems();
        return getParentItemByName(name, menuConfig.getMenuItems());
    }

    private MenuItem getParentItemByName(String name, List<MenuItem> items) {
        MenuItem result = null;
        if (items == null) {
            return null;
        }
        for (MenuItem item : items) {
            if (item.getMenuItems() == null) {
                continue;
            }
            for (MenuItem item1 : item.getMenuItems()) {
                if (item1.getName().equals(name)) {
                    result = item;
                    break;
                }
            }
            if (result == null) {
                result = getParentItemByName(name, item.getMenuItems());
            } else {
                break;
            }
        }
        return result;
    }

    private MenuItem getRootMenuItem(boolean isRoot) {
        MenuItem menuItem = new MenuItem();
        if (!isRoot) {
            menuItem.setMessage(botConfig.getUserExists());
        } else {
            menuItem.setMessage(menuConfig.getMessage());
        }
        menuItem.setMenuItems(menuConfig.getMenuItems());
        return menuItem;
    }
}
