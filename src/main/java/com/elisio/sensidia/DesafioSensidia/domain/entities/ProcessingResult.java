package com.elisio.sensidia.DesafioSensidia.domain.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingResult {


    @NotNull(message = "O qtdLinhas não pode ser null")
    private Long qtdLinhas;

    @NotNull(message = "O status não pode ser null")
    private Enum status;
}
