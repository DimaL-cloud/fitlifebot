package ua.bibusukraine.fitlifebot.client.fatsecret.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductResponse {
  private Long id;
  private String name;
  private ServingsResponse servingsResponse;

  @JsonCreator
  public ProductResponse(@JsonProperty(value = "food_id", required = true) Long id,
                         @JsonProperty(value = "food_name") String name,
                         @JsonProperty(value = "servings") ServingsResponse servingsResponse) {
    this.id = id;
    this.name = name;
    this.servingsResponse = servingsResponse;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ServingsResponse getServingsResponse() {
    return servingsResponse;
  }

  public static class ServingsResponse {
    private final List<ServingResponse> servings;

    @JsonCreator
    public ServingsResponse(@JsonProperty("serving") List<ServingResponse> servings) {
      this.servings = servings;
    }

    public List<ServingResponse> getServings() {
      return servings;
    }
  }

  public static class ServingResponse {
    private final Long id;
    private final Double amount;
    private final String metricUnit;
    private final Double calories;
    private final Double carbonates;
    private final Double protein;
    private final Double fat;

    @JsonCreator
    public ServingResponse(@JsonProperty(value = "serving_id", required = true) Long id,
                           @JsonProperty(value = "metric_serving_amount") Double amount,
                           @JsonProperty(value = "metric_serving_unit") String metricUnit,
                           @JsonProperty(value = "calories") Double calories,
                           @JsonProperty(value = "carbohydrate") Double carbonates,
                           @JsonProperty(value = "protein") Double protein,
                           @JsonProperty(value = "fat") Double fat) {
      this.id = id;
      this.amount = amount;
      this.metricUnit = metricUnit;
      this.carbonates = carbonates;
      this.protein = protein;
      this.fat = fat;
      this.calories = calories;
    }

    public Long getId() {
      return id;
    }

    public Double getAmount() {
      return amount;
    }

    public String getMetricUnit() {
      return metricUnit;
    }

    public Double getCalories() {
      return calories;
    }

    public Double getCarbonates() {
      return carbonates;
    }

    public Double getProtein() {
      return protein;
    }

    public Double getFat() {
      return fat;
    }
  }
}
