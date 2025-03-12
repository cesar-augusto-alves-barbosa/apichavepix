package io.github.cesar_augusto_alves_barbosa.apichavepix.controller;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.ChavePixInvalidaException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.ChavePixJaCadastradaException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.service.PixChaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
class PixChaveControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private PixChaveService pixChaveService;

    private PixChaveCriacaoDTO dto;
    private PixChaveDTO pixChaveDTO;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        dto = new PixChaveCriacaoDTO(
                TipoChave.CPF,
                "12345678901",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira"
        );

        pixChaveDTO = new PixChaveDTO(
                UUID.randomUUID(),
                dto.tipoChave(),
                dto.valorChave(),
                dto.tipoConta(),
                dto.numeroAgencia(),
                dto.numeroConta(),
                dto.nomeCorrentista(),
                dto.sobrenomeCorrentista(),
                StatusChave.ATIVA,
                LocalDateTime.now(),
                null
        );
    }

    @Test
    void deveRetornar201QuandoCadastrarComSucesso() throws Exception {
        when(pixChaveService.cadastrarChave(any(PixChaveCriacaoDTO.class))).thenReturn(pixChaveDTO.id());

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pixChaveDTO.id().toString()));
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
    void deveRetornar422QuandoChaveInvalida() throws Exception {
        when(pixChaveService.cadastrarChave(any(PixChaveCriacaoDTO.class)))
                .thenThrow(new ChavePixInvalidaException("Dados inválidos para cadastro da chave PIX."));

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity());
    }
}