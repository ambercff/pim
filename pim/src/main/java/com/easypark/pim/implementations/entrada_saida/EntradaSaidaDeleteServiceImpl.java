package com.easypark.pim.implementations.entrada_saida;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDeleteDTO;
import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.entities.Veiculo;
import com.easypark.pim.repositories.EntradaSaidaRepository;
import com.easypark.pim.repositories.VeiculoRepository;
import com.easypark.pim.services.entrada_saida.EntradaSaidaDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EntradaSaidaDeleteServiceImpl implements EntradaSaidaDeleteService {
    @Autowired
    private EntradaSaidaRepository entradaSaidaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Override
    public String deleteEntradaSaida(EntradaSaidaDeleteDTO data) {
        Veiculo veiculo = veiculoRepository.findByPlaca(data.placa());
        EntradaSaida entradaSaida = entradaSaidaRepository.findByVeiculoAndHoraEntrada(veiculo, data.horaEntrada());
        entradaSaidaRepository.delete(entradaSaida);

        return "Registro de entrada/saída excluído com sucesso!";
    }
}
