package com.easypark.pim.controllers;

import com.easypark.pim.dtos.entrada_saida.EntradaSaidaCreateDTO;
import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDTO;
import com.easypark.pim.dtos.entrada_saida.EntradaSaidaDeleteDTO;
import com.easypark.pim.dtos.entrada_saida.EntradaSaidaUpdateDTO;
import com.easypark.pim.services.entrada_saida.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/entradaSaida")
public class EntradaSaidaController {
    @Autowired
    EntradaSaidaSaveService entradaSaidaSaveService;

    @Autowired
    EntradaSaidaGetAllService entradaSaidaGetAllService;

    @Autowired
    EntradaSaidaUpdateHoraService entradaSaidaUpdateHoraService;

    @Autowired
    EntradaSaidaGetByVagaService entradaSaidaGetByVagaService;

    @Autowired
    EntradaSaidaDeleteService entradaSaidaDeleteService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @PostMapping
    @Operation(summary = "Registrar Entrada/Saída",
            description ="Registrar Entrada/Saída",
            tags = {"Registros Entrada/Saída"})
    public ResponseEntity<?> save(@RequestBody @Valid EntradaSaidaCreateDTO data, UriComponentsBuilder uriBuilder){
        EntradaSaidaDTO entradaSaidaDTO = entradaSaidaSaveService.save(data);

        List<EntradaSaidaDTO> listaAtualizada = entradaSaidaGetAllService.getAll();
        messagingTemplate.convertAndSend("/topic/entradasSaidas", listaAtualizada);

        return new ResponseEntity<>(entradaSaidaDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar Entradas/Saídas",
            description ="Registrar Entrada/Saída",
            tags = {"Registros Entrada/Saída"})
    public ResponseEntity<Page<EntradaSaidaDTO>> getAll(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(entradaSaidaGetAllService.getAll(pageable), HttpStatus.OK);
    }

    @PutMapping("/{numero_vaga}")
    @Operation(summary = "Registrar Hora de Saída",
            description ="Registrar Hora de Saída",
            tags = {"Registros Entrada/Saída"})
    @Transactional
    public ResponseEntity<EntradaSaidaDTO> updateHora(@PathVariable int numero_vaga, @RequestBody EntradaSaidaUpdateDTO data){
        EntradaSaidaDTO entradaSaidaDTO = entradaSaidaUpdateHoraService.updateHoraSaida(data, numero_vaga);

        List<EntradaSaidaDTO> listaAtualizada = entradaSaidaGetAllService.getAll();
        messagingTemplate.convertAndSend("/topic/entradasSaidas", listaAtualizada);
        return new ResponseEntity<>(entradaSaidaDTO, HttpStatus.OK);
    }

    @GetMapping("/{numero_vaga}")
    @Operation(summary = "Buscar por número da vaga",
            description ="Buscar por número da vaga",
            tags = {"Registros Entrada/Saída"})
    public ResponseEntity<EntradaSaidaDTO> getByNumVaga(@PathVariable int numero_vaga){
        return new ResponseEntity<>(entradaSaidaGetByVagaService.getByVaga(numero_vaga), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Excluir por veículo e hora de entrada",
            description ="Excluir por veículo e hora de entrada",
            tags = {"Registros Entrada/Saída"})
    public ResponseEntity<String> deleteEntradaSaida(@RequestBody EntradaSaidaDeleteDTO data){
        String response = entradaSaidaDeleteService.deleteEntradaSaida(data);

        List<EntradaSaidaDTO> listaAtualizada = entradaSaidaGetAllService.getAll();

        messagingTemplate.convertAndSend("/topic/entradasSaidas", listaAtualizada);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
