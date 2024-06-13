package ua.bibusukraine.fitlifebot.model;

import java.util.Objects;

public class WhrData {

    private Double waist; // in centimeters (cm)
    private Double hips; // in centimeters (cm)

    public WhrData() {
    }

    public WhrData(Double waist, Double hips) {
        this.waist = waist;
        this.hips = hips;
    }

    public Double getWaist() {
        return waist;
    }

    public void setWaist(Double waist) {
        this.waist = waist;
    }

    public Double getHips() {
        return hips;
    }

    public void setHips(Double hips) {
        this.hips = hips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhrData whrData = (WhrData) o;
        return Objects.equals(waist, whrData.waist) &&
                Objects.equals(hips, whrData.hips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(waist, hips);
    }

    @Override
    public String toString() {
        return "WHRData{" +
                "waist=" + waist +
                ", hips=" + hips +
                '}';
    }
}
