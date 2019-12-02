package bot.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:botConfig.yml")
public class BotConfig {

    private String pathToEnv;
    private String userApiPath;
    private String userNotFoundMessage;
    private String botUsername;
    private String botToken;
    private String userExists;

    public String getBotUsername() {
        return botUsername;
    }

    public String getUserExists() {
        return userExists;
    }

    public void setUserExists(String userExists) {
        this.userExists = userExists;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getUserNotFoundMessage() {
        return userNotFoundMessage;
    }

    public void setUserNotFoundMessage(String userNotFoundMessage) {
        this.userNotFoundMessage = userNotFoundMessage;
    }

    public String getPathToEnv() {
        return pathToEnv;
    }

    public void setPathToEnv(String pathToEnv) {
        this.pathToEnv = pathToEnv;
    }

    public String getUserApiPath() {
        return userApiPath;
    }

    public void setUserApiPath(String userApiPath) {
        this.userApiPath = userApiPath;
    }
}
