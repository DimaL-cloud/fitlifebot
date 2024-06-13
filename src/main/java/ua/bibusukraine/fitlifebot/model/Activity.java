package ua.bibusukraine.fitlifebot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ua.bibusukraine.fitlifebot.annotation.ReportInclude;

@Entity
@Table(name = "activity")
@ReportInclude(title = "Report")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @ReportInclude(columnName = "Id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "name", nullable = false)
    @ReportInclude(columnName = "Name")
    private String name;

    @Column(name = "burned_calories")
    @ReportInclude(columnName = "Burned Calories")
    private Double burnedCalories;

    @Column(name = "spent_time_in_minutes")
    @ReportInclude(columnName = "Spent time (minutes)")
    private Integer spentTimeInMinutes;

    @Column(name = "notes")
    @ReportInclude(columnName = "Notes")
    private String notes;

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
