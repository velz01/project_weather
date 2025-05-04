package ru.velz.project_weather.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 16, message = "Length from 4 to 16 letters")

    private String login;

    @Size(min = 4, message = "Length from 4 letters")
    private String password;

    @Transient
    @OneToMany(mappedBy = "user")
    private List<Location> locations;

    @Transient
    @OneToMany(mappedBy = "user")
    private List<Session> sessions;


}
