package ua.bibusukraine.fitlifebot.client.fatsecret;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.FoodResponse;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.SingleFoodResponse;

@FeignClient(name = "fatsecretProductClient",
    url = "https://platform.fatsecret.com",
    path = "/rest/server.api",
    configuration = {ProductClient.RequestInterceptorImpl.class, ProductClient.LoggingConfig.class})
public interface ProductClient {
  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<SingleFoodResponse> getProductById(@NonNull @RequestParam("food_id") Long foodId);

  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<FoodResponse> getProductsBySearch(@NonNull @RequestParam("search_expression") String searchExpression);

  @Component
  class RequestInterceptorImpl implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
      template.query("format", "json");
      if (template.queries().containsKey("search_expression")) {
        template.header("Authorization", "Bearer " + AccessTokenHolder.getAccessToken());
        template.query("method", "foods.search");
        template.header("Host", "platform.fatsecret.com");
      } else if (template.queries().containsKey("food_id")) {
        template.header("Authorization", "Bearer " + AccessTokenHolder.getAccessToken());
        template.query("method", "food.get.v4");
        template.header("Host", "platform.fatsecret.com");
      }
    }
  }

  @Configuration
  class LoggingConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
      return Logger.Level.FULL;
    }
  }
}
