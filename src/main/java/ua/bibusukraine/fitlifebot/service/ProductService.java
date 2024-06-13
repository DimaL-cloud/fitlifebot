package ua.bibusukraine.fitlifebot.service;

import ua.bibusukraine.fitlifebot.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
  void fillProductComplexFields(Product product);
  Product saveProduct(Product product);
  List<Product> findProductsByChatId(Long chatId);
  void removeById(Long id);
  Optional<Product> findById(Long id);
}
