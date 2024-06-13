package ua.bibusukraine.fitlifebot.client.fatsecret.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ua.bibusukraine.fitlifebot.serialization.deserializer.SingleValueAsListDeserializer;

import java.util.List;

public class FoodResponse {
  private final Foods foods;

  @JsonCreator
  public FoodResponse(@JsonProperty("foods") Foods foods) {
    this.foods = foods;
  }

  public Foods getFoods() {
    return foods;
  }

  public static class Foods {
    private final List<ProductResponse> food;

    @JsonCreator
    public Foods(@JsonDeserialize(using = SingleValueAsListDeserializer.class) @JsonProperty("food") List<ProductResponse> food) {
      this.food = food;
    }

    public List<ProductResponse> getFood() {
      return food;
    }
  }
}