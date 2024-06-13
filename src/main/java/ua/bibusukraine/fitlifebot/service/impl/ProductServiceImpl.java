package ua.bibusukraine.fitlifebot.service.impl;

import org.springframework.stereotype.Service;
import ua.bibusukraine.fitlifebot.client.fatsecret.AccessTokenHolder;
import ua.bibusukraine.fitlifebot.client.fatsecret.OauthClient;
import ua.bibusukraine.fitlifebot.client.fatsecret.ProductClient;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.AccessTokenResponse;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.ProductResponse;
import ua.bibusukraine.fitlifebot.model.Product;
import ua.bibusukraine.fitlifebot.repository.ProductRepository;
import ua.bibusukraine.fitlifebot.service.ProductService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
  private static final String grantType = "client_credentials";
  private static final String scope = "basic";
  private final OauthClient oauthClient;
  private final ProductClient productClient;
  private final ProductRepository productRepository;

  public ProductServiceImpl(OauthClient oauthClient, ProductClient productClient, ProductRepository productRepository) {
    this.oauthClient = oauthClient;
    this.productClient = productClient;
    this.productRepository = productRepository;
  }

  @Override
  public void fillProductComplexFields(Product product) {
    if (AccessTokenHolder.isAccessTokenExpired()) {
      AccessTokenHolder.setAccessToken(getAccessToken());
    }
    List<ProductResponse> products = Objects.requireNonNull(productClient.getProductsBySearch(product.getName())).getBody().getFoods().getFood();
    Long productId = Objects.requireNonNull(products).stream()
        .findFirst()
        .map(ProductResponse::getId)
        .orElseThrow(() -> new RuntimeException("Product has empty id field"));
    ProductResponse detailedProductInfo = Objects.requireNonNull(productClient.getProductById(productId).getBody()).getFood();
    ProductResponse.ServingResponse servingInfo = detailedProductInfo.getServingsResponse().getServings().stream()
        .filter(serving -> serving != null &&
            serving.getMetricUnit() != null &&
            serving.getMetricUnit().equals("g"))
        .findFirst()
        .orElse(null);
    product.setName(detailedProductInfo.getName());
    conductCalculations(product, servingInfo, product.getWeight());
  }

  @Override
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public List<Product> findProductsByChatId(Long chatId) {
    return productRepository.findByChatId(chatId);
  }

  @Override
  public void removeById(Long id) {
    productRepository.removeById(id);
  }

  @Override
  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  private AccessTokenResponse getAccessToken() {
    return this.oauthClient.getAccessToken(grantType, scope).getBody();
  }

  private void conductCalculations(Product product, ProductResponse.ServingResponse serving, Double consumedWeight) {
    if (Objects.nonNull(serving)) {
      product.setCalories(round((consumedWeight / serving.getAmount()) * serving.getCalories(), 3));
      product.setFat(round((consumedWeight / serving.getAmount()) * serving.getFat(), 3));
      product.setProtein(round((consumedWeight / serving.getAmount()) * serving.getProtein(), 3));
      product.setCarbonates(round((consumedWeight / serving.getAmount()) * serving.getCarbonates(), 3));
    }
  }

  private Double round(Double number, int desiredPrecision){
    Double precisionMultiplier = Math.pow(10, desiredPrecision);
    return Math.round(number * precisionMultiplier) / precisionMultiplier;
  }
}
