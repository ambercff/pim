package com.easypark.pim.repositories;

import com.easypark.pim.entities.EntradaSaida;
import com.easypark.pim.entities.Vaga;
import com.easypark.pim.entities.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface EntradaSaidaRepository extends JpaRepository<EntradaSaida, Long> {
    EntradaSaida findFirstByVagaNumeroVagaAndHoraSaidaIsNullOrderByHoraEntradaDesc(int numeroVaga);

    EntradaSaida findByVaga(Vaga byNumeroVaga);

    EntradaSaida findByVeiculoAndHoraEntrada(Veiculo veiculo, LocalDateTime horaEntrada);

    Page<EntradaSaida> findAll(Pageable pageable);

}
