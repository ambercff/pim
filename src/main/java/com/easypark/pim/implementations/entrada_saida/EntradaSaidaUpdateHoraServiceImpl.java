package com.easypark.pim.implementations.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import com.easypark.pim.dtos.entrada_saida.EntradaSaidaUpdateDTO;
import com.easypark.pim.dtos.vagas.VagaDTO;
import com.easypark.pim.dtos.vagas.VagaUpdateDTO;
import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.entities.Vaga;
import com.easypark.pim.entities.enums.StatusVaga;
import com.easypark.pim.exceptions.GenericsNotFoundException;
import com.easypark.pim.repositories.EntradaSaidaRepository;
import com.easypark.pim.repositories.VagaRespository;
import com.easypark.pim.services.entrada_saida.EntradaSaidaUpdateHoraService;
import com.easypark.pim.services.vagas.VagaUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntradaSaidaUpdateHoraServiceImpl implements EntradaSaidaUpdateHoraService {
    @Autowired
    private EntradaSaidaRepository entradaSaidaRepository;

    @Autowired
    private VagaRespository vagaRespository;

    @Autowired
    private VagaUpdateService vagaUpdateService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public EntradaSaidaDTO updateHoraSaida(EntradaSaidaUpdateDTO data, int numeroVaga) {
        // Buscar o registro mais recente para a vaga com hora de saída nula
        EntradaSaida entradaSaida = entradaSaidaRepository
                .findFirstByVagaNumeroVagaAndHoraSaidaIsNullOrderByHoraEntradaDesc(numeroVaga);

        entradaSaida.setHoraSaida(data.horaSaida());

        entradaSaidaRepository.save(entradaSaida);

        vagaUpdateService.update(new VagaUpdateDTO(StatusVaga.STATUS_LIVRE), entradaSaida.getVaga().getNumeroVaga());

        List<VagaDTO> listaAtualizada = vagaRespository.findAll().stream()
                .map(VagaDTO::new)
                .toList();
        messagingTemplate.convertAndSend("/topic/vagas", listaAtualizada);

        return new EntradaSaidaDTO(entradaSaida);
    }
}
