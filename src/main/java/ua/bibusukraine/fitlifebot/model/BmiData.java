package ua.bibusukraine.fitlifebot.model;

import java.util.Objects;

public class BmiData {

    private Double weight;
    private Double height;

    public BmiData() {
    }

    public BmiData(Double weight, Double height) {
        this.weight = weight;
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BmiData bmiData = (BmiData) o;

        if (!Objects.equals(weight, bmiData.weight)) return false;
        return Objects.equals(height, bmiData.height);
    }

    @Override
    public int hashCode() {
        int result = weight != null ? weight.hashCode() : 0;
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

}
