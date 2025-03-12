package io.github.cesar_augusto_alves_barbosa.apichavepix.controller;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveAlteracaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.service.PixChaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class PixChaveController {

    private final PixChaveService pixChaveService;

    public PixChaveController(PixChaveService pixChaveService) {
        this.pixChaveService = pixChaveService;
    }

    @PostMapping
    public ResponseEntity<UUID> cadastrar(@Valid @RequestBody PixChaveCriacaoDTO dto) {
        UUID chaveId = pixChaveService.cadastrarChave(dto);
        return ResponseEntity.status(HttpStatus.OK).body(chaveId);
    }


    @PutMapping
    public ResponseEntity<PixChaveDTO> alterar(@Valid @RequestBody PixChaveAlteracaoDTO dto) {
        PixChaveDTO chaveAlterada = pixChaveService.alterarChave(dto);
        return ResponseEntity.ok(chaveAlterada);
    }

}
