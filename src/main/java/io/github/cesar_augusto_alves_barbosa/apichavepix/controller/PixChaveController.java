package io.github.cesar_augusto_alves_barbosa.apichavepix.controller;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.ConsultaInvalidaException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.service.PixChaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class PixChaveController {

    private final PixChaveService pixChaveService;

    public PixChaveController(PixChaveService pixChaveService) {
        this.pixChaveService = pixChaveService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixChaveConsultaRespostaDTO> consultarPorId(
            @PathVariable UUID id,
            @RequestParam(required = false) Map<String, String> allRequestParams) {

        if (!allRequestParams.isEmpty()) {
            throw new ConsultaInvalidaException("Se o ID for informado, nenhum outro filtro pode ser utilizado.");
        }

        return ResponseEntity.ok(pixChaveService.consultarPorId(id));
    }


    @GetMapping
    public ResponseEntity<List<PixChaveConsultaRespostaDTO>> consultarPorFiltros(PixChaveConsultaDTO filtroDTO) {
        List<PixChaveConsultaRespostaDTO> chaves = pixChaveService.consultarPorFiltros(filtroDTO);
        return chaves.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(chaves);
    }

    @PostMapping
    public ResponseEntity<PixChaveCriacaoRespostaDTO> cadastrar(@Valid @RequestBody PixChaveCriacaoDTO dto) {
        UUID chaveId = pixChaveService.cadastrarChave(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new PixChaveCriacaoRespostaDTO(chaveId));
    }

    @PutMapping
    public ResponseEntity<PixChaveDTO> alterar(@Valid @RequestBody PixChaveAlteracaoDTO dto) {
        PixChaveDTO chaveAlterada = pixChaveService.alterarChave(dto);
        return ResponseEntity.ok(chaveAlterada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PixChaveDTO> inativarChave(@PathVariable UUID id) {
        PixChaveDTO chaveInativada = pixChaveService.inativarChave(id);
        return ResponseEntity.ok(chaveInativada);
    }

}
