package bot.data_layer.repository;

import bot.data_layer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByTelegramId(Integer telegramId);
}
