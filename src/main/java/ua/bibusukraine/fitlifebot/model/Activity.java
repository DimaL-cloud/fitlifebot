package ua.bibusukraine.fitlifebot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "burned_calories")
    private Double burnedCalories;

    @Column(name = "spent_time_in_minutes")
    private Integer spentTimeInMinutes;

    @Column(name = "notes")
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBurnedCalories() {
        return burnedCalories;
    }

    public void setBurnedCalories(Double burnedCalories) {
        this.burnedCalories = burnedCalories;
    }

    public Integer getSpentTimeInMinutes() {
        return spentTimeInMinutes;
    }

    public void setSpentTimeInMinutes(Integer spentTimeInMinutes) {
        this.spentTimeInMinutes = spentTimeInMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity activity)) return false;

        return id != null && id.equals(activity.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
