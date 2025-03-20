package com.easypark.pim.implementations.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.repositories.EntradaSaidaRepository;
import com.easypark.pim.services.entrada_saida.EntradaSaidaGetAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class EntradaSaidaGetAllServiceImpl implements EntradaSaidaGetAllService {
    @Autowired
    private EntradaSaidaRepository entradaSaidaRepository;

    @Override
    public List<EntradaSaidaDTO> getAll() {
        return entradaSaidaRepository.findAll().stream().map(EntradaSaidaDTO::new).toList();
    }

    @Override
    public Page<EntradaSaidaDTO> getAll(Pageable pageable) {
        Page<EntradaSaida> page = entradaSaidaRepository.findAll(pageable);
        return page.map(EntradaSaidaDTO::new);
    }
}
