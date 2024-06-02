package ua.bibusukraine.fitlifebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.bibusukraine.fitlifebot.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
