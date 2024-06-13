package ua.bibusukraine.fitlifebot.model;

import java.util.Objects;

public class BmrData {

    private Double weight;
    private Double height;
    private Integer age;
    private String gender;

    public BmrData() {
    }

    public BmrData(Double weight, Double height, Integer age, String gender) {
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BmrData bmrData = (BmrData) o;
        return Objects.equals(weight, bmrData.weight) &&
                Objects.equals(height, bmrData.height) &&
                Objects.equals(age, bmrData.age) &&
                Objects.equals(gender, bmrData.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, height, age, gender);
    }

    @Override
    public String toString() {
        return "BmrData{" +
                "weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
