package com.elisio.sensidia.DesafioSensidia.domain.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotNull(message = "O campo userId não pode ser null")
    private String userId;

    @NotNull(message = "O email userId não pode ser null")
    private String email;
}
