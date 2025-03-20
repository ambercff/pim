package com.easypark.pim.services.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EntradaSaidaGetAllService {
    Page<EntradaSaidaDTO> getAll(Pageable pageable);

    List<EntradaSaidaDTO> getAll();
}
