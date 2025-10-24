package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "NAME_REGISTRY")
@Data
public class NameRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NAME_REGISTRY_SEQ")
    @SequenceGenerator(name = "NAME_REGISTRY_SEQ", sequenceName = "NAME_REGISTRY_SEQ", allocationSize = 1, initialValue = 1000)
    private Long id;

    private String firstName;

    private String lastName;

    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime thruDate;

    @Version
    private Timestamp version;
}
