package ua.bibusukraine.fitlifebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.bibusukraine.fitlifebot.model.Weight;

import java.time.LocalDateTime;
import java.util.List;

public interface WeightRepository extends JpaRepository<Weight, Long> {

    List<Weight> findWeightsByChatId(Long chatId);
    List<Weight> findWeightsByChatIdAndCreatedAtBetween(Long chatId, LocalDateTime createdAt, LocalDateTime createdAt2);

}
