package com.damselbeing.jokesbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "usersDataTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long chatID;

    private String firstName;

    private Timestamp registeredAt;


}
