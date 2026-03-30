package com.quynhproject.globalstocktracker.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quynhproject.globalstocktracker.constant.AuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email", unique = true)
    @Email(message = "Email không đúng định dạng")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 20)
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WatchLists> watchLists;

    @PrePersist
    public void handleBeforeCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
