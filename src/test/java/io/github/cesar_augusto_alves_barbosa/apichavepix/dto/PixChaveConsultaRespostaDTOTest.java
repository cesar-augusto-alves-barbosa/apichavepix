package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PixChaveConsultaRespostaDTOTest {

    private PixChaveConsultaRespostaDTO dto1;
    private PixChaveConsultaRespostaDTO dto2;
    private PixChaveConsultaRespostaDTO dtoEmail;
    private PixChaveConsultaRespostaDTO dtoDiferente;
    private PixChaveConsultaRespostaDTO dto;

    @BeforeEach
    void setUp() {
        dto = PixChaveConsultaRespostaDTO.builder()
                .id(UUID.randomUUID())
                .tipoChave("EMAIL")
                .valorChave("example@example.com")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Carlos")
                .sobrenomeCorrentista("Silva")
                .status("ATIVA")
                .dataCriacao(LocalDateTime.now().toString())
                .dataInativacao(null)
                .build();
        dto1 = PixChaveConsultaRespostaDTO.builder()
                .id(UUID.randomUUID())
                .tipoChave("CPF")
                .valorChave("12345678909")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Carlos Silva")
                .sobrenomeCorrentista("Oliveira")
                .status("ATIVA")
                .dataCriacao("2021-01-01T10:00:00")
                .dataInativacao(null)
                .build();

        dto2 = PixChaveConsultaRespostaDTO.builder()
                .id(dto1.getId())
                .tipoChave("CPF")
                .valorChave("12345678909")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Carlos Silva")
                .sobrenomeCorrentista("Oliveira")
                .status("ATIVA")
                .dataCriacao("2021-01-01T10:00:00")
                .dataInativacao(null)
                .build();

        dtoEmail = PixChaveConsultaRespostaDTO.builder()
                .id(UUID.randomUUID())
                .tipoChave("EMAIL")
                .valorChave("teste@email.com")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("João Silva")
                .sobrenomeCorrentista("Oliveira")
                .status("ATIVA")
                .dataCriacao("2025-03-10")
                .dataInativacao(null)
                .build();

        dtoDiferente = PixChaveConsultaRespostaDTO.builder()
                .id(UUID.randomUUID())
                .tipoChave("CNPJ")
                .valorChave("98765432100")
                .tipoConta("POUPANCA")
                .numeroAgencia(4321)
                .numeroConta(98765432)
                .nomeCorrentista("Carlos Souza")
                .sobrenomeCorrentista("Santos")
                .status("INATIVA")
                .dataCriacao("2021-02-01T10:00:00")
                .dataInativacao("2021-02-01T12:00:00")
                .build();
    }

    @Test
    void deveTestarEqualsComObjetosIguais() {
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosDiferentes() {
        assertNotEquals(dto1, dtoDiferente);
        assertNotEquals(dto1.hashCode(), dtoDiferente.hashCode());
    }

    @Test
    void deveTestarEqualsComObjetosNulos() {
        PixChaveConsultaRespostaDTO dtoNull = null;
        assertNotEquals(dto1, null);
    }

    @Test
    void deveTestarToStringNaoRetornaNulo() {
        assertNotNull(dto1.toString());
    }

    @Test
    void deveAlterarValoresComSetters() {
        dto1.setTipoChave("CNPJ");
        dto1.setValorChave("98765432100");
        dto1.setTipoConta("POUPANCA");
        dto1.setNumeroAgencia(4321);
        dto1.setNumeroConta(98765432);
        dto1.setNomeCorrentista("Carlos Souza");
        dto1.setSobrenomeCorrentista("Santos");
        dto1.setStatus("INATIVA");
        dto1.setDataCriacao("2021-02-01T10:00:00");
        dto1.setDataInativacao("2021-02-01T12:00:00");

        assertEquals("CNPJ", dto1.getTipoChave());
        assertEquals("98765432100", dto1.getValorChave());
        assertEquals("POUPANCA", dto1.getTipoConta());
        assertEquals(4321, dto1.getNumeroAgencia());
        assertEquals(98765432, dto1.getNumeroConta());
        assertEquals("Carlos Souza", dto1.getNomeCorrentista());
        assertEquals("Santos", dto1.getSobrenomeCorrentista());
        assertEquals("INATIVA", dto1.getStatus());
        assertEquals("2021-02-01T10:00:00", dto1.getDataCriacao());
        assertEquals("2021-02-01T12:00:00", dto1.getDataInativacao());
    }

    @Test
    void deveTestarBuilderComSucesso() {
        PixChaveConsultaRespostaDTO dtoBuilder = PixChaveConsultaRespostaDTO.builder()
                .id(UUID.randomUUID())
                .tipoChave("CPF")
                .valorChave("12345678909")
                .tipoConta("CORRENTE")
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("João Silva")
                .sobrenomeCorrentista("Oliveira")
                .status("ATIVA")
                .dataCriacao("2021-01-01T10:00:00")
                .dataInativacao(null)
                .build();

        assertNotNull(dtoBuilder);
        assertEquals("CPF", dtoBuilder.getTipoChave());
        assertEquals("12345678909", dtoBuilder.getValorChave());
        assertEquals("CORRENTE", dtoBuilder.getTipoConta());
        assertEquals(1234, dtoBuilder.getNumeroAgencia());
        assertEquals(56789012, dtoBuilder.getNumeroConta());
        assertEquals("João Silva", dtoBuilder.getNomeCorrentista());
        assertEquals("Oliveira", dtoBuilder.getSobrenomeCorrentista());
        assertEquals("ATIVA", dtoBuilder.getStatus());
        assertEquals("2021-01-01T10:00:00", dtoBuilder.getDataCriacao());
        assertNull(dtoBuilder.getDataInativacao());
    }

    @Test
    void deveCriarPixChaveConsultaRespostaDTOUsandoBuilder() {
        UUID id = UUID.randomUUID();
        String tipoChave = "EMAIL";
        String valorChave = "teste@email.com";
        String tipoConta = "CORRENTE";
        Integer numeroAgencia = 1234;
        Integer numeroConta = 56789012;
        String nomeCorrentista = "Carlos";
        String sobrenomeCorrentista = "Silva";
        String status = "ATIVA";
        String dataCriacao = "2025-03-13";

        PixChaveConsultaRespostaDTO dto = PixChaveConsultaRespostaDTO.builder()
                .id(id)
                .tipoChave(tipoChave)
                .valorChave(valorChave)
                .tipoConta(tipoConta)
                .numeroAgencia(numeroAgencia)
                .numeroConta(numeroConta)
                .nomeCorrentista(nomeCorrentista)
                .sobrenomeCorrentista(sobrenomeCorrentista)
                .status(status)
                .dataCriacao(dataCriacao)
                .dataInativacao(null)
                .build();

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(tipoChave, dto.getTipoChave());
        assertEquals(valorChave, dto.getValorChave());
        assertEquals(tipoConta, dto.getTipoConta());
        assertEquals(numeroAgencia, dto.getNumeroAgencia());
        assertEquals(numeroConta, dto.getNumeroConta());
        assertEquals(nomeCorrentista, dto.getNomeCorrentista());
        assertEquals(sobrenomeCorrentista, dto.getSobrenomeCorrentista());
        assertEquals(status, dto.getStatus());
        assertEquals(dataCriacao, dto.getDataCriacao());
        assertNull(dto.getDataInativacao());
    }

    @Test
    void deveCriarPixChaveConsultaRespostaDTOComSucesso() {
        assertNotNull(dtoEmail);
        assertEquals("EMAIL", dtoEmail.getTipoChave());
        assertEquals("teste@email.com", dtoEmail.getValorChave());
        assertEquals("CORRENTE", dtoEmail.getTipoConta());
        assertEquals(1234, dtoEmail.getNumeroAgencia());
        assertEquals(56789012, dtoEmail.getNumeroConta());
        assertEquals("João Silva", dtoEmail.getNomeCorrentista());
        assertEquals("Oliveira", dtoEmail.getSobrenomeCorrentista());
        assertEquals("ATIVA", dtoEmail.getStatus());
        assertEquals("2025-03-10", dtoEmail.getDataCriacao());
    }

    @Test
    void deveTestarToStringComSucesso() {
        String resultadoToString = dto.toString();

        assertNotNull(resultadoToString);

        assertTrue(resultadoToString.contains("id=" + dto.getId()));
        assertTrue(resultadoToString.contains("tipoChave=" + dto.getTipoChave()));
        assertTrue(resultadoToString.contains("valorChave=" + dto.getValorChave()));
        assertTrue(resultadoToString.contains("tipoConta=" + dto.getTipoConta()));
        assertTrue(resultadoToString.contains("numeroAgencia=" + dto.getNumeroAgencia()));
        assertTrue(resultadoToString.contains("numeroConta=" + dto.getNumeroConta()));
        assertTrue(resultadoToString.contains("nomeCorrentista=" + dto.getNomeCorrentista()));
        assertTrue(resultadoToString.contains("sobrenomeCorrentista=" + dto.getSobrenomeCorrentista()));
        assertTrue(resultadoToString.contains("status=" + dto.getStatus()));
        assertTrue(resultadoToString.contains("dataCriacao=" + dto.getDataCriacao()));
    }

}
