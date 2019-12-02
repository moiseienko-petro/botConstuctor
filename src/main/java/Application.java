import bot.configs.BotConfig;
import bot.configs.MenuConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication(scanBasePackages = {"bot", "bot.data_layer", "bot.rest"})
@EnableJpaRepositories({"bot.data_layer.repository"})
@EntityScan({"bot.data_layer.model"})
public class Application {

    public static void main(String[] args) {
        ApiContextInitializer.init();

        ApplicationContext context = SpringApplication.run(Application.class, args);
        MenuConfig menuConfig = context.getBean(MenuConfig.class);
        BotConfig botConfig = context.getBean(BotConfig.class);
    }

}
