package bot.helper;

import bot.configs.BotConfig;
import bot.configs.MenuConfig;
import bot.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class MenuHelperTest {

    @Mock
    private BotConfig botConfig;

    @Mock
    private MenuConfig menuConfig;

    @Autowired
    @InjectMocks
    private MenuHelper menuHelper;

    @BeforeEach()
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetMenuItem() {
        
    }

    @Test
    void testGetMenuItemByName() {
        mockMenuConfig();
        MenuConfig menu = getMenuConfig();
        MenuItem actual = menuHelper.getMenuItemByName("Layer3_1", menu.getMenuItems());
        assertEquals("Layer3_1", actual.getName());

        actual = menuHelper.getMenuItemByName("Layer6_1", menu.getMenuItems());
        assertNull(actual);
    }

    @Test
    void testGetParentLevel() {
        mockMenuConfig();
        MenuItem actual = menuHelper.getParentLevel("Layer3_1");
        assertEquals("Layer2_1", actual.getName());

        actual = menuHelper.getParentLevel("Layer6_1");
        assertNull(actual);
    }

    @Test
    void testGetRootMenuItem() {
        mockBotConfig();
        mockMenuConfig();
        MenuConfig menuConfig = getMenuConfig();
        MenuItem actual = menuHelper.getRootMenuItem(true);
        MenuItem expected = new MenuItem();
        expected.setMenuItems(menuConfig.getMenuItems());
        expected.setMessage("User exists");
        assertEquals(expected, actual);

        actual = menuHelper.getRootMenuItem(false);
        expected.setMessage(menuConfig.getMessage());
        assertEquals(expected, actual);

    }

    private void mockMenuConfig() {
        MenuConfig menu = getMenuConfig();
        when(menuConfig.getMenuItems()).thenReturn(menu.getMenuItems());
        when(menuConfig.getMessage()).thenReturn(menu.getMessage());
    }

    private void mockBotConfig() {
        when(botConfig.getUserExists()).thenReturn("User exists");
    }

    private MenuConfig getMenuConfig() {
        MenuConfig menuConfig = new MenuConfig();
        List<MenuItem> thirdLayer = new ArrayList<>();
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setName("Layer3_1");
        thirdLayer.add(menuItem1);

        List<MenuItem> secondLayer = new ArrayList<>();
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("Layer2_1");
        MenuItem menuItem3 = new MenuItem();
        menuItem3.setName("Layer2_2");
        menuItem2.setMenuItems(thirdLayer);
        secondLayer.add(menuItem2);
        secondLayer.add(menuItem3);

        List<MenuItem> secondLayer1 = new ArrayList<>();
        MenuItem menuItem7 = new MenuItem();
        menuItem7.setName("Layer2_1");
        MenuItem menuItem8 = new MenuItem();
        menuItem8.setName("Layer2_2");
        secondLayer1.add(menuItem7);
        secondLayer1.add(menuItem8);

        List<MenuItem> firstLayer = new ArrayList<>();
        MenuItem menuItem4 = new MenuItem();
        menuItem4.setName("Layer1_1");
        MenuItem menuItem5 = new MenuItem();
        menuItem5.setName("Layer1_2");
        MenuItem menuItem6 = new MenuItem();
        menuItem6.setName("Layer1_3");
        menuItem5.setMenuItems(secondLayer);
        menuItem6.setMenuItems(secondLayer1);
        firstLayer.add(menuItem4);
        firstLayer.add(menuItem5);

        menuConfig.setMenuItems(firstLayer);
        menuConfig.setMessage("Welcome");
        return menuConfig;
    }
}