package com.easypark.pim.dtos.entrada_saida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EntradaSaidaDeleteDTO(@NotBlank String placa,
                                    @NotNull LocalDateTime horaEntrada
                                    ) {
}
