package io.github.cesar_augusto_alves_barbosa.apichavepix.service;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.PixChaveAlteracaoDTO;
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
                "87468543801",
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira"
        );
    }

    @Test
    void deveCadastrarChaveComSucesso() {
        UUID chaveId = UUID.randomUUID();
        PixChave chaveCriada = PixChave.builder()
                .id(chaveId)
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

        UUID chaveIdRetornada = pixChaveService.cadastrarChave(dto);

        assertNotNull(chaveIdRetornada);
        assertEquals(chaveCriada.getId(), chaveIdRetornada);

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

        assertEquals("CPF inválido. Deve conter exatamente 11 dígitos numéricos e ser um CPF válido.", exception.getMessage());
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

        assertEquals("E-mail inválido. O formato correto deve conter '@' e seguir as regras de e-mail.", exception.getMessage());
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
    void deveAlterarChaveComSucesso() {
        UUID id = UUID.randomUUID();
        PixChave chaveExistente = new PixChave();
        chaveExistente.setId(id);
        chaveExistente.setStatus(StatusChave.ATIVA);

        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                id,
                TipoConta.CORRENTE,
                1234,
                12345678,
                "João",
                "Silva"
        );

        when(pixChaveRepository.findById(id)).thenReturn(Optional.of(chaveExistente));
        when(pixChaveRepository.save(any(PixChave.class))).thenReturn(chaveExistente);

        PixChaveDTO resultado = pixChaveService.alterarChave(dto);

        assertNotNull(resultado);
        assertEquals(dto.tipoConta(), resultado.tipoConta());
        verify(pixChaveRepository, times(1)).save(any(PixChave.class));
    }

    @Test
    void naoDevePermitirAlterarChaveInativa() {
        PixChave chaveInativa = new PixChave();
        chaveInativa.setStatus(StatusChave.INATIVA);

        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                UUID.randomUUID(),
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira"
        );

        when(pixChaveRepository.findById(dto.id())).thenReturn(Optional.of(chaveInativa));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pixChaveService.alterarChave(dto));
        assertEquals("Não é permitido alterar chaves inativadas.", exception.getMessage());
    }

    @Test
    void deveRetornarErroSeChaveNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        PixChaveAlteracaoDTO dto = new PixChaveAlteracaoDTO(
                idInexistente,
                TipoConta.CORRENTE,
                1234,
                56789012,
                "João Silva",
                "Oliveira"
        );

        when(pixChaveRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> pixChaveService.alterarChave(dto));
        assertEquals("Chave PIX não encontrada", exception.getMessage());
    }


}
