package com.easypark.pim.implementations.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaCreateDTO;
import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import com.easypark.pim.dtos.vagas.VagaDTO;
import com.easypark.pim.dtos.vagas.VagaUpdateDTO;
import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.entities.Vaga;
import com.easypark.pim.entities.Veiculo;
import com.easypark.pim.entities.enums.StatusVaga;
import com.easypark.pim.exceptions.GenericsConflictVagaException;
import com.easypark.pim.exceptions.GenericsNotFoundException;
import com.easypark.pim.repositories.EntradaSaidaRepository;
import com.easypark.pim.repositories.VagaRespository;
import com.easypark.pim.repositories.VeiculoRepository;
import com.easypark.pim.services.entrada_saida.EntradaSaidaSaveService;
import com.easypark.pim.services.vagas.VagaUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntradaSaidaSaveServiceImpl implements EntradaSaidaSaveService {
    @Autowired
    private EntradaSaidaRepository entradaSaidaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VagaRespository vagaRespository;

    @Autowired
    private VagaUpdateService vagaUpdateService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public EntradaSaidaDTO save(EntradaSaidaCreateDTO data) {
        EntradaSaida entradaSaida = new EntradaSaida(data);

        Veiculo veiculo = veiculoRepository.findByPlaca(data.placa());

        if(veiculo != null){
            Vaga vaga = vagaRespository.findByNumeroVaga(data.numeroVaga());

            if(vaga.getStatusVaga() == StatusVaga.STATUS_OCUPADO) {
                throw new GenericsConflictVagaException("Vaga já está ocupada!");
            }

            if(vaga != null){
                entradaSaida.setVeiculo(veiculo);
                entradaSaida.setVaga(vaga);
                entradaSaidaRepository.save(entradaSaida);

                vagaUpdateService.update(new VagaUpdateDTO(StatusVaga.STATUS_OCUPADO), data.numeroVaga());

                List<VagaDTO> listaAtualizada = vagaRespository.findAll().stream()
                        .map(VagaDTO::new)
                        .toList();
                messagingTemplate.convertAndSend("/topic/vagas", listaAtualizada);

                return new EntradaSaidaDTO(entradaSaida);
            } else {
                throw new GenericsNotFoundException("Vaga não encontrada!");
            }
        }
        throw new GenericsNotFoundException("Veículo não encontrado!");
    }
}
