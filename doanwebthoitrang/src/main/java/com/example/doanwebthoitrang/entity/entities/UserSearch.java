package com.example.doanwebthoitrang.entity.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_searches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGINT maps to Long

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nullable
    private User user;

    @Column(name = "search_query", nullable = false, length = 255)
    private String searchQuery;

    @CreationTimestamp // Use creation timestamp for searched_at
    @Column(name = "searched_at", nullable = false, updatable = false)
    private LocalDateTime searchedAt;

    @Column(name = "session_id", length = 100)
    private String sessionId;
}
