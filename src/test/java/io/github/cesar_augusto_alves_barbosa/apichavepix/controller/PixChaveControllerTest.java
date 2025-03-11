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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PixChaveControllerTest {

    @Mock
    private PixChaveService pixChaveService;

    @InjectMocks
    private PixChaveController pixChaveController;

    private PixChaveCriacaoDTO dto;
    private PixChaveDTO pixChaveDTO;

    @BeforeEach
    void setup() {
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
    void deveRetornar201QuandoCadastrarComSucesso() {
        when(pixChaveService.cadastrarChave(dto)).thenReturn(pixChaveDTO);

        ResponseEntity<PixChaveDTO> resposta = pixChaveController.cadastrar(dto);

        assertEquals(201, resposta.getStatusCodeValue());
    }

    @Test
    void deveRetornar409QuandoChaveJaExiste() {
        when(pixChaveService.cadastrarChave(dto)).thenThrow(new ChavePixJaCadastradaException("Chave PIX já cadastrada"));

        ResponseEntity<String> resposta = pixChaveController.handleChaveJaCadastrada(
                new ChavePixJaCadastradaException("Chave PIX já cadastrada")
        );

        assertEquals(409, resposta.getStatusCodeValue());
    }

    @Test
    void deveRetornar422QuandoChaveInvalida() {
        when(pixChaveService.cadastrarChave(dto)).thenThrow(new ChavePixInvalidaException("CPF inválido"));

        ResponseEntity<String> resposta = pixChaveController.handleChaveInvalida(
                new ChavePixInvalidaException("CPF inválido")
        );

        assertEquals(422, resposta.getStatusCodeValue());
    }

    @Test
    void deveLancarExcecaoChavePixJaCadastrada() {
        when(pixChaveService.cadastrarChave(dto)).thenThrow(new ChavePixJaCadastradaException("Chave PIX já cadastrada"));

        Exception exception = assertThrows(ChavePixJaCadastradaException.class, () -> pixChaveController.cadastrar(dto));

        assertEquals("Chave PIX já cadastrada", exception.getMessage());
        verify(pixChaveService, times(1)).cadastrarChave(dto);
    }

    @Test
    void deveCadastrarChavePixComSucesso() {
        when(pixChaveService.cadastrarChave(dto)).thenReturn(pixChaveDTO);

        ResponseEntity<PixChaveDTO> resposta = pixChaveController.cadastrar(dto);

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCodeValue());
        assertNotNull(resposta.getBody());
        assertEquals(dto.valorChave(), resposta.getBody().valorChave());

        verify(pixChaveService, times(1)).cadastrarChave(dto);
    }
}
