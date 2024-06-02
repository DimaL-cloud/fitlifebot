package ua.bibusukraine.fitlifebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.bibusukraine.fitlifebot.model.Activity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findActivitiesByChatId(Long chatId);

}
