package bot.data_layer.repository;

import bot.data_layer.model.ActionButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ActionButtonRepository extends JpaRepository<ActionButton, Long>{

    @Query("Select a from ActionButton a join fetch a.request join fetch a.oppositeRequest where a.id=:id")
    ActionButton getActionButton(@Param("id") Long id);
}
