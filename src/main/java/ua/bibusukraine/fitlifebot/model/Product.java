package ua.bibusukraine.fitlifebot.model;


import jakarta.persistence.*;
import ua.bibusukraine.fitlifebot.annotation.ReportInclude;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "products")
@ReportInclude(title = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ReportInclude(columnName = "Айді")
  private Long id;
  @Column(name = "chat_id")
  private Long chatId;
  @Column(name = "calories")
  @ReportInclude(columnName = "Калорійність")
  private Double calories;
  @Column(name = "name")
  @ReportInclude(columnName = "Назва")
  private String name;
  @Column(name = "notes")
  @ReportInclude(columnName = "Нотатки")
  private String notes;
  @Column(name = "consumption_date_time")
  @ReportInclude(columnName = "Час та дата вжитку")
  private LocalDateTime consumptionDateTime;
  @Column(name = "fat")
  private Double fat;
  @Column(name = "carbonates")
  private Double carbonates;
  @Column(name = "protein")
  private Double protein;
  @Column(name = "weight")
  private Double weight;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getChatId() {
    return chatId;
  }

  public void setChatId(Long chatId) {
    this.chatId = chatId;
  }

  public Double getCalories() {
    return calories;
  }

  public void setCalories(Double calories) {
    this.calories = calories;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Double getFat() {
    return fat;
  }

  public Double getCarbonates() {
    return carbonates;
  }

  public Double getProtein() {
    return protein;
  }

  public Double getWeight() {
    return weight;
  }

  public void setFat(Double fat) {
    this.fat = fat;
  }

  public void setCarbonates(Double carbonates) {
    this.carbonates = carbonates;
  }

  public void setProtein(Double protein) {
    this.protein = protein;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public LocalDateTime getConsumptionDateTime() {
    return consumptionDateTime;
  }

  public void setConsumptionDateTime(LocalDateTime consumptionDateTime) {
    this.consumptionDateTime = consumptionDateTime;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;

    Product product = (Product) object;

    return Objects.equals(id, product.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  public static class Builder {
    private Long id;
    private Long chatId;
    private Double calories;
    private String name;
    private String notes;
    private LocalDateTime consumptionDateTime;
    private Double fat;
    private Double carbonates;
    private Double protein;
    private Double weight;

    public Builder setId(Long id) {
      this.id = id;
      return this;
    }

    public Builder setChatId(Long chatId) {
      this.chatId = chatId;
      return this;
    }

    public Builder setCalories(Double calories) {
      this.calories = calories;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setNotes(String notes) {
      this.notes = notes;
      return this;
    }

    public Builder setConsumptionDateTime(LocalDateTime consumptionDateTime) {
      this.consumptionDateTime = consumptionDateTime;
      return this;
    }

    public Builder setFat(Double fat) {
      this.fat = fat;
      return this;
    }

    public Builder setCarbonates(Double carbonates) {
      this.carbonates = carbonates;
      return this;
    }

    public Builder setProtein(Double protein) {
      this.protein = protein;
      return this;
    }

    public Builder setWeight(Double weight) {
      this.weight = weight;
      return this;
    }

    public Product build() {
      Product product = new Product();
      product.id = this.id;
      product.chatId = this.chatId;
      product.calories = this.calories;
      product.name = this.name;
      product.notes = this.notes;
      product.consumptionDateTime = this.consumptionDateTime;
      product.fat = this.fat;
      product.carbonates = this.carbonates;
      product.protein = this.protein;
      product.weight = this.weight;
      return product;
    }
  }
}
