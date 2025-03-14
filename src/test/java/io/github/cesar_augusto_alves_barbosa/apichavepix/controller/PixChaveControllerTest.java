package io.github.cesar_augusto_alves_barbosa.apichavepix.controller;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.service.PixChaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PixChaveControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PixChaveService pixChaveService;

    @InjectMocks
    private PixChaveController pixChaveController;

    private UUID chaveId;
    private PixChaveCriacaoDTO pixChaveCriacaoDTO;
    private PixChaveAlteracaoDTO pixChaveAlteracaoDTO;
    private PixChaveConsultaRespostaDTO respostaDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(pixChaveController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        chaveId = UUID.randomUUID();

        pixChaveCriacaoDTO = new PixChaveCriacaoDTO(
                TipoChave.EMAIL, "teste@email.com", TipoConta.CORRENTE,
                1234, 56789012, "João Silva", "Oliveira", TipoTitular.PF
        );

        pixChaveAlteracaoDTO = new PixChaveAlteracaoDTO(
                chaveId, TipoConta.POUPANCA, 4321, 87654321, "Carlos Silva", "Santos"
        );

        respostaDTO = new PixChaveConsultaRespostaDTO(
                chaveId, TipoChave.EMAIL.name(), "teste@email.com", TipoConta.CORRENTE.name(),
                1234, 56789012, "João Silva", "Oliveira", StatusChave.ATIVA.name(), "10/03/2025", null
        );
    }

    @Test
    void deveRetornar422QuandoChaveJaExiste() throws Exception {
        when(pixChaveService.cadastrarChave(any(PixChaveCriacaoDTO.class)))
                .thenThrow(new ChavePixJaCadastradaException("Chave PIX já cadastrada para outro correntista."));

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pixChaveCriacaoDTO)))
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
                        .content(new ObjectMapper().writeValueAsString(pixChaveAlteracaoDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar422QuandoChaveJaInativa() throws Exception {
        when(pixChaveService.inativarChave(any()))
                .thenThrow(new ChavePixInvalidaException("A chave PIX já foi desativada."));

        mockMvc.perform(delete("/api/pix/" + chaveId))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornar200QuandoConsultarChavePorFiltrosComSucesso() throws Exception {
        PixChaveFiltroDTO filtroDTO = new PixChaveFiltroDTO(
                "EMAIL", null, null, null, null, "João Silva", null
        );

        when(pixChaveService.consultarPorFiltros(any())).thenReturn(List.of(respostaDTO));

        mockMvc.perform(get("/api/pix")
                        .param("tipoChave", "EMAIL")
                        .param("nomeCorrentista", "João Silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chaveId.toString()))
                .andExpect(jsonPath("$[0].tipoChave").value("EMAIL"));
    }

    @Test
    void deveRetornar200QuandoCadastrarChaveComSucesso() throws Exception {
        when(pixChaveService.cadastrarChave(any())).thenReturn(chaveId);

        mockMvc.perform(post("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pixChaveCriacaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chaveId.toString()));
    }

    @Test
    void deveRetornar200QuandoAlterarChaveComSucesso() throws Exception {
        PixChaveDTO chaveAlterada = new PixChaveDTO(
                chaveId, TipoChave.EMAIL, "teste@email.com", TipoConta.POUPANCA,
                4321, 87654321, "Carlos Silva", "Santos", StatusChave.ATIVA, null, null
        );

        when(pixChaveService.alterarChave(any())).thenReturn(chaveAlterada);

        mockMvc.perform(put("/api/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pixChaveAlteracaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chaveId.toString()))
                .andExpect(jsonPath("$.tipoConta").value("POUPANCA"));
    }

    @Test
    void deveRetornar200QuandoConsultarChavePorIdComSucesso() throws Exception {
        when(pixChaveService.consultarPorId(chaveId)).thenReturn(respostaDTO);

        mockMvc.perform(get("/api/pix/" + chaveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chaveId.toString()))
                .andExpect(jsonPath("$.tipoChave").value("EMAIL"))
                .andExpect(jsonPath("$.valorChave").value("teste@email.com"));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrarChavesComFiltros() throws Exception {
        PixChaveFiltroDTO filtroDTO = new PixChaveFiltroDTO(
                "EMAIL", null, null, null, null, "Carlos", null
        );

        when(pixChaveService.consultarPorFiltros(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/pix")
                        .param("tipoChave", "EMAIL")
                        .param("nomeCorrentista", "Carlos"))
                .andExpect(status().isNotFound());
    }



    @Test
    void deveRetornar200QuandoInativarChaveComSucesso() throws Exception {
        PixChaveDTO chaveDTO = new PixChaveDTO(
                chaveId,
                TipoChave.EMAIL,
                "teste@email.com",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira",
                StatusChave.INATIVA,
                LocalDateTime.parse("2025-03-10T00:00:00"),
                null
        );

        when(pixChaveService.inativarChave(any())).thenReturn(chaveDTO);

        mockMvc.perform(delete("/api/pix/" + chaveId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chaveId.toString()))
                .andExpect(jsonPath("$.status").value("INATIVA"));
    }


}
