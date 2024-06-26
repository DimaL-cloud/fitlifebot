package ua.bibusukraine.fitlifebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.bibusukraine.fitlifebot.model.Sleep;

import java.util.List;

public interface SleepRepository extends JpaRepository<Sleep, Long> {

    List<Sleep> findSleepsByChatId(Long chatId);

}
