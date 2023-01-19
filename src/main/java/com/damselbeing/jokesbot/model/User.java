package com.damselbeing.jokesbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "usersDataTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long chatID;

    private String firstName;

    @EqualsAndHashCode.Exclude
    private LocalDateTime registeredAt;


}
