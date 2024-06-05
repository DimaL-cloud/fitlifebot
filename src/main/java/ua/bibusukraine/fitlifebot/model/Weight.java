package ua.bibusukraine.fitlifebot.model;

import jakarta.persistence.*;
import ua.bibusukraine.fitlifebot.annotation.ReportInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "weight")
@ReportInclude(title = "Weight report")
public class Weight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @ReportInclude(columnName = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "weight", nullable = false)
    @ReportInclude(columnName = "current weight")
    private BigDecimal weight;

    @Column(name = "created_at", nullable = false)
    @ReportInclude(columnName = "creation date")
    private LocalDateTime createdAt;

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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weight)) return false;
        return id != null && Objects.equals(id, ((Weight) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
