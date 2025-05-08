package ru.velz.project_weather.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

    @Size(min = 4, max = 16, message = "Length from 4 to 16 letters")
    @NotEmpty(message = "Login should not be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Login must contain only english letters, numbers, and underscores")
    private String login;

    @NotEmpty(message = "Password should not be empty.")
    @Size(min = 4, message = "Length from 4 letters")
    private String password;

    @NotEmpty(message = "Password should not be empty.")
    @Size(min = 4, message = "Length from 4 letters")
    private String repeatedPassword;

}
