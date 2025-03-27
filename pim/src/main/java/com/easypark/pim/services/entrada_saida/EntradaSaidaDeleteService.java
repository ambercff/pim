package com.easypark.pim.services.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDeleteDTO;

import java.time.LocalDateTime;

public interface EntradaSaidaDeleteService {
    String deleteEntradaSaida(EntradaSaidaDeleteDTO data);
}
