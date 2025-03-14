package io.github.cesar_augusto_alves_barbosa.apichavepix.service;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.factory.PixChaveFactory;
import io.github.cesar_augusto_alves_barbosa.apichavepix.repository.PixChaveRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PixChaveService {

    private final PixChaveRepository pixChaveRepository;

    public PixChaveService(PixChaveRepository pixChaveRepository) {
        this.pixChaveRepository = pixChaveRepository;
    }

    public PixChaveConsultaRespostaDTO consultarPorId(UUID id) {
        return pixChaveRepository.findById(id)
                .map(PixChaveFactory::toConsultaDTO)
                .orElseThrow(() -> new ChavePixNaoEncontradaException("Chave PIX não encontrada."));
    }


    public List<PixChaveConsultaRespostaDTO> consultarPorFiltros(PixChaveConsultaDTO filtroDTO) {
        PixChave filtro = PixChaveFactory.consultaToEntity(filtroDTO);
        Example<PixChave> example = Example.of(filtro, ExampleMatcher.matchingAll()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase()
                        .withIgnoreNullValues());

        return pixChaveRepository.findAll(example)
                .stream()
                .map(PixChaveFactory::toConsultaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID cadastrarChave(PixChaveCriacaoDTO dto) {
        if (pixChaveRepository.findByValorChave(dto.valorChave()).isPresent()) {
            throw new ChavePixJaCadastradaException("Chave PIX já cadastrada para outro correntista.");
        }

        long quantidadeChaves = pixChaveRepository.countByNumeroConta(dto.numeroConta());

        if ((dto.tipoTitular() == TipoTitular.PF && quantidadeChaves >= 5) ||
                (dto.tipoTitular() == TipoTitular.PJ && quantidadeChaves >= 20)) {
            throw new LimiteChavesPixAtingidoException("Limite máximo de chaves PIX atingido para essa conta.");
        }

        validarRegrasDeCadastro(dto);

        PixChave novaChave = PixChaveFactory.criarChavePixToEntity(dto);
        PixChave chaveSalva = pixChaveRepository.save(novaChave);

        return chaveSalva.getId();
    }

    protected void validarRegrasDeCadastro(PixChaveCriacaoDTO dto) {
        if (dto.tipoChave() == null) {
            throw new ChavePixInvalidaException("O tipo da chave é obrigatório.");
        }

        if (dto.tipoConta() == null) {
            throw new ChavePixInvalidaException("O tipo da conta é obrigatório.");
        }

        if (dto.valorChave() == null || dto.valorChave().isBlank()) {
            throw new ChavePixInvalidaException("O valor da chave não pode ser nulo ou vazio.");
        }

        if (dto.numeroConta() == null || dto.numeroConta().toString().length() > 8) {
            throw new ChavePixInvalidaException("Número da conta inválido. Deve ter no máximo 8 dígitos.");
        }

        if (dto.numeroAgencia() == null || dto.numeroAgencia().toString().length() > 4) {
            throw new ChavePixInvalidaException("Número da agência inválido. Deve ter no máximo 4 dígitos.");
        }

        if (!EnumSet.of(TipoChave.CPF, TipoChave.CNPJ, TipoChave.EMAIL, TipoChave.CELULAR, TipoChave.ALEATORIO).contains(dto.tipoChave())) {
            throw new ChavePixInvalidaException("Tipo de chave inválido.");
        }


        switch (dto.tipoChave()) {
            case CELULAR -> validarCelular(dto.valorChave());
            case EMAIL -> validarEmail(dto.valorChave());
            case CPF -> validarCpf(dto.valorChave());
            case CNPJ -> validarCnpj(dto.valorChave());
            case ALEATORIO -> validarChaveAleatoria(dto.valorChave());
            default -> throw new ChavePixInvalidaException("Tipo de chave inválido.");
        }
    }


    protected void validarCelular(String celular) {
        if (celular == null || celular.isBlank()) {
            throw new ChavePixInvalidaException("Número de celular inválido. O valor não pode ser nulo ou vazio.");
        }
        if (!Pattern.matches("^\\+\\d{1,2}\\d{1,3}\\d{9}$", celular)) {
            throw new ChavePixInvalidaException("Número de celular inválido. O formato correto é +[código do país][DDD][número com 9 dígitos].");
        }
    }


    protected void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ChavePixInvalidaException("E-mail inválido. O valor não pode ser nulo ou vazio.");
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new ChavePixInvalidaException("E-mail inválido. O formato correto deve conter '@' e um domínio válido.");
        }

        if (email.length() > 77) {
            throw new ChavePixInvalidaException("E-mail inválido. Deve conter no máximo 77 caracteres.");
        }
    }



    protected void validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new ChavePixInvalidaException("CPF inválido. O valor não pode ser nulo ou vazio.");
        }
        if (!cpf.matches("^\\d{11}$")) {
            throw new ChavePixInvalidaException("CPF inválido. Deve conter exatamente 11 dígitos numéricos.");
        }

        CPFValidator validator = new CPFValidator();
        validator.initialize(null);
        if (!validator.isValid(cpf, null)) {
            throw new ChavePixInvalidaException("CPF inválido. O número informado não é válido.");
        }
    }

    protected void validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new ChavePixInvalidaException("CNPJ inválido. O valor não pode ser nulo ou vazio.");
        }
        if (!cnpj.matches("^\\d{14}$")) {
            throw new ChavePixInvalidaException("CNPJ inválido. Deve conter exatamente 14 dígitos numéricos.");
        }

        CNPJValidator validator = new CNPJValidator();
        validator.initialize(null);
        if (!validator.isValid(cnpj, null)) {
            throw new ChavePixInvalidaException("CNPJ inválido. O número informado não é válido.");
        }
    }


    protected void validarChaveAleatoria(String chave) {
        if (chave == null || chave.isBlank()) {
            throw new ChavePixInvalidaException("Chave aleatória inválida. O valor não pode ser nulo ou vazio.");
        }
        if (!chave.matches("^[a-zA-Z0-9\\-]+$")) { // Apenas caracteres alfanuméricos e "-"
            throw new ChavePixInvalidaException("Chave aleatória inválida. Deve conter apenas letras, números e '-'.");
        }
        if (chave.length() > 36) {
            throw new ChavePixInvalidaException("Chave aleatória inválida. Deve conter no máximo 36 caracteres.");
        }
    }



    @Transactional
    public PixChaveDTO alterarChave(PixChaveAlteracaoDTO dto) {
        PixChave chave = pixChaveRepository.findById(dto.id())
                .orElseThrow(() -> new ChavePixNaoEncontradaException("Chave PIX não encontrada com id " + dto.id()));
        chave.setTipoConta(dto.tipoConta());
        chave.setNumeroAgencia(dto.numeroAgencia());
        chave.setNumeroConta(dto.numeroConta());
        chave.setNomeCorrentista(dto.nomeCorrentista());
        chave.setSobrenomeCorrentista(dto.sobrenomeCorrentista());

        PixChave chaveSalva = pixChaveRepository.save(chave);

            return PixChaveFactory.toDTO(chaveSalva);
    }

    @Transactional
    public PixChaveDTO inativarChave(UUID id) {
        PixChave chave = pixChaveRepository.findById(id)
                .orElseThrow(() -> new ChavePixNaoEncontradaException("Chave PIX não encontrada com ID: " + id));

        if (chave.getStatus() == StatusChave.INATIVA) {
            throw new ChavePixInvalidaException("A chave PIX já foi desativada.");
        }

        chave.setStatus(StatusChave.INATIVA);
        chave.setDataInativacao(LocalDateTime.now());

        PixChave chaveSalva = pixChaveRepository.save(chave);
        return PixChaveFactory.toDTO(chaveSalva);
    }

}
