package io.github.cesar_augusto_alves_barbosa.apichavepix.controller;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.repository.PixChaveRepository;
import io.github.cesar_augusto_alves_barbosa.apichavepix.service.PixChaveService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PixChaveController.class)
class PixChaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PixChaveService pixChaveService;

    private PixChaveCriacaoDTO dto;
    private PixChaveAlteracaoDTO alteracaoDTO;
    private UUID chaveId;

    @BeforeEach
    void setup() {

        chaveId = UUID.randomUUID();
        alteracaoDTO = new PixChaveAlteracaoDTO(
                chaveId, TipoConta.POUPANCA,
                4321,
                87654321,
                "Carlos Silva",
                "Santos"
        );

        dto = new PixChaveCriacaoDTO(
                TipoChave.CPF,
                "12345678901",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira",
                TipoTitular.PF
        );

        PixChave chaveInativa = PixChave.builder()
                .id(chaveId)
                .tipoChave(TipoChave.CPF)
                .valorChave("12345678901")
                .tipoConta(TipoConta.CORRENTE)
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Carlos Silva")
                .sobrenomeCorrentista("Santos")
                .status(StatusChave.INATIVA)
                .dataCriacao(LocalDateTime.now())
                .dataInativacao(LocalDateTime.now())
                .build();
    }

    @Test
    void deveRetornar422QuandoChaveJaExiste() throws Exception {
        when(pixChaveService.cadastrarChave(any(PixChaveCriacaoDTO.class)))
                .thenThrow(new ChavePixJaCadastradaException("Chave PIX já cadastrada para outro correntista."));

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity());
    }



    @Test
    void deveRetornar404QuandoChaveNaoEncontrada() throws Exception {
        when(pixChaveService.consultarPorId(any())).thenThrow(new ChavePixNaoEncontradaException("Chave não encontrada"));

        mockMvc.perform(get("/api/pix/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar422QuandoAlterarChaveInativa() throws Exception {
        doThrow(new ChavePixInativadaException("Não é permitido alterar chaves inativadas."))
                .when(pixChaveService).alterarChave(any(PixChaveAlteracaoDTO.class));

        mockMvc.perform(put("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(alteracaoDTO)))
                .andExpect(status().isUnprocessableEntity());
    }



    @Test
    void deveRetornar422QuandoChaveJaInativa() throws Exception {
        when(pixChaveService.inativarChave(any()))
                .thenThrow(new ChavePixInvalidaException("A chave PIX já foi desativada."));

        mockMvc.perform(delete("/api/pix/" + chaveId))
                .andExpect(status().isUnprocessableEntity());
    }
}
