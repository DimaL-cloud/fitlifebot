package ua.bibusukraine.fitlifebot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.bibusukraine.fitlifebot.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query(value = "SELECT p FROM Product p WHERE p.chatId=:chatId")
  List<Product> findByChatId(@Param("chatId") Long chatId);

  @Transactional(readOnly = false)
  @Modifying
  @Query(value = "DELETE Product p WHERE p.id=:id")
  void removeById(@Param("id") Long id);
}
