package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductHolder {
  private final Map<Long, Product> chatIdToProductMap = new ConcurrentHashMap<>();

  public Product getProductByChatId(Long chatId) {
    return this.chatIdToProductMap.get(chatId);
  }

  public void putProduct(Long chatId, Product product) {
    this.chatIdToProductMap.put(chatId, product);
  }

  public Product removeProductByChatId(Long chatId) {
    return this.chatIdToProductMap.remove(chatId);
  }
}
