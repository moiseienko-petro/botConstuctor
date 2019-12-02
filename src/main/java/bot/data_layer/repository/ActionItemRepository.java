package bot.data_layer.repository;

import bot.data_layer.model.ActionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {
}
