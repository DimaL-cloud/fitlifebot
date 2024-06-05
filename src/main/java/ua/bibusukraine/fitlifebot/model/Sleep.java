package ua.bibusukraine.fitlifebot.model;

import jakarta.persistence.*;
import ua.bibusukraine.fitlifebot.annotation.ReportInclude;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sleep")
@ReportInclude(title = "Sleep report")
public class Sleep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @ReportInclude(columnName = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "went_to_bed", nullable = false)
    @ReportInclude(columnName = "bed time")
    private LocalDateTime wentToBed;

    @Column(name = "woke_up", nullable = false)
    @ReportInclude(columnName = "wake up")
    private LocalDateTime wokeUp;

    @Column(name = "sleep_time", nullable = false)
    @ReportInclude(columnName = "sleep time")
    private Long sleepTimeInMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getWentToBed() {
        return wentToBed;
    }

    public void setWentToBed(LocalDateTime wentToBad) {
        this.wentToBed = wentToBad;
    }

    public LocalDateTime getWokeUp() {
        return wokeUp;
    }

    public void setWokeUp(LocalDateTime wokeUp) {
        this.wokeUp = wokeUp;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSleepTimeInMinutes() {
        return sleepTimeInMinutes;
    }

    public void setSleepTimeInMinutes(Long sleepTimeInMinutes) {
        this.sleepTimeInMinutes = sleepTimeInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sleep)) return false;
        return id != null && Objects.equals(id, ((Sleep) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
