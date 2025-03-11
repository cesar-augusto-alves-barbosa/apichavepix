package io.github.cesar_augusto_alves_barbosa.apichavepix.service;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveCriacaoDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveDTO;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoConta;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.ChavePixInvalidaException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.ChavePixJaCadastradaException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.LimiteChavesPixAtingidoException;
import io.github.cesar_augusto_alves_barbosa.apichavepix.repository.PixChaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PixChaveServiceTest {

    @Mock
    private PixChaveRepository pixChaveRepository;

    @InjectMocks
    private PixChaveService pixChaveService;

    private PixChaveCriacaoDTO dto;

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
    }

    @Test
    void deveCadastrarChaveComSucesso() {
        PixChave chaveCriada = PixChave.builder()
                .id(UUID.randomUUID())
                .tipoChave(dto.tipoChave())
                .valorChave(dto.valorChave())
                .tipoConta(dto.tipoConta())
                .numeroAgencia(dto.numeroAgencia())
                .numeroConta(dto.numeroConta())
                .nomeCorrentista(dto.nomeCorrentista())
                .sobrenomeCorrentista(dto.sobrenomeCorrentista())
                .dataCriacao(LocalDateTime.now())
                .status(StatusChave.ATIVA)
                .build();

        when(pixChaveRepository.findByValorChave(dto.valorChave())).thenReturn(Optional.empty());
        when(pixChaveRepository.save(any(PixChave.class))).thenReturn(chaveCriada);

        PixChaveDTO chaveSalva = pixChaveService.cadastrarChave(dto);

        assertNotNull(chaveSalva);
        assertEquals(dto.valorChave(), chaveSalva.valorChave());
        assertEquals(dto.tipoChave(), chaveSalva.tipoChave());
        assertEquals(StatusChave.ATIVA, chaveSalva.status());

        verify(pixChaveRepository, times(1)).save(any(PixChave.class));
    }

    @Test
    void naoDeveCadastrarChaveDuplicada() {
        when(pixChaveRepository.findByValorChave(dto.valorChave())).thenReturn(Optional.of(new PixChave()));

        Exception exception = assertThrows(ChavePixJaCadastradaException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("Chave PIX já cadastrada para outro correntista.", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }

    @Test
    void naoDeveCadastrarChaveSeLimiteExcedido() {
        when(pixChaveRepository.findByValorChave(dto.valorChave())).thenReturn(Optional.empty());
        when(pixChaveRepository.countByNumeroAgenciaAndNumeroConta(dto.numeroAgencia(), dto.numeroConta()))
                .thenReturn(5L);

        Exception exception = assertThrows(LimiteChavesPixAtingidoException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("Limite máximo de chaves PIX atingido para essa conta.", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }

    @Test
    void deveRejeitarCpfInvalido() {
        dto = new PixChaveCriacaoDTO(
                TipoChave.CPF,
                "12345ABC901", // CPF inválido (contém letras)
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira"
        );

        Exception exception = assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("CPF inválido. Deve conter exatamente 11 dígitos numéricos.", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        dto = new PixChaveCriacaoDTO(
                TipoChave.EMAIL,
                "email_invalido.com", // Falta "@"
                TipoConta.CORRENTE,
                1234,
                56789012,
                "Maria Souza",
                ""
        );

        Exception exception = assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("E-mail inválido. Deve conter '@' e ter no máximo 77 caracteres.", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }

    @Test
    void deveRejeitarCelularSemCodigoPais() {
        dto = new PixChaveCriacaoDTO(
                TipoChave.CELULAR,
                "11987654321", // Falta o código do país (+55)
                TipoConta.CORRENTE,
                1234,
                56789012,
                "Carlos Pereira",
                ""
        );

        Exception exception = assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("Número de celular inválido. O formato correto é +[código do país][DDD][número com 9 dígitos].", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }

    @Test
    void deveRejeitarContaComTipoInvalido() {
        dto = new PixChaveCriacaoDTO(
                TipoChave.EMAIL,
                "teste@email.com",
                null, // Tipo de conta inválido
                1234,
                56789012,
                "Fernanda Lima",
                ""
        );

        Exception exception = assertThrows(ChavePixInvalidaException.class, () -> pixChaveService.cadastrarChave(dto));

        assertEquals("O tipo da conta é obrigatório.", exception.getMessage());
        verify(pixChaveRepository, never()).save(any(PixChave.class));
    }
}
