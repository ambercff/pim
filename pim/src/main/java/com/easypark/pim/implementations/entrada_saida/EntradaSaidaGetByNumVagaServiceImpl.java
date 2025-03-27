package com.easypark.pim.implementations.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.exceptions.GenericsNotFoundException;
import com.easypark.pim.repositories.EntradaSaidaRepository;
import com.easypark.pim.repositories.VagaRespository;
import com.easypark.pim.services.entrada_saida.EntradaSaidaGetByVagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntradaSaidaGetByNumVagaServiceImpl implements EntradaSaidaGetByVagaService {
    @Autowired
    private EntradaSaidaRepository entradaSaidaRepository;

    @Autowired
    VagaRespository vagaRespository;

    @Override
    public EntradaSaidaDTO getByVaga(int numVaga) {

        EntradaSaida entradaSaida = entradaSaidaRepository.findByVaga(vagaRespository.findByNumeroVaga(numVaga));
        if(entradaSaida != null){
            return new EntradaSaidaDTO(entradaSaida);
        } else {
            throw new GenericsNotFoundException("Entrada/Saída não encontrados!");
        }
    }
}
