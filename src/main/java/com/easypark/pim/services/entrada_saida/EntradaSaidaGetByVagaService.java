package com.easypark.pim.services.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;

public interface EntradaSaidaGetByVagaService {
    EntradaSaidaDTO getByVaga(int numVaga);
}
