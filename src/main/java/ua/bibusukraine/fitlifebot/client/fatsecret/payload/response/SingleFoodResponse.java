package ua.bibusukraine.fitlifebot.client.fatsecret.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleFoodResponse {
  private final ProductResponse food;

  @JsonCreator
  public SingleFoodResponse(@JsonProperty("food") ProductResponse food) {
    this.food = food;
  }

  public ProductResponse getFood() {
    return food;
  }
}
