package io.github.cesar_augusto_alves_barbosa.apichavepix.service;

import io.github.cesar_augusto_alves_barbosa.apichavepix.dto.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.StatusChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoTitular;
import io.github.cesar_augusto_alves_barbosa.apichavepix.exception.*;
import io.github.cesar_augusto_alves_barbosa.apichavepix.mapper.PixChaveMapper;
import io.github.cesar_augusto_alves_barbosa.apichavepix.repository.PixChaveRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .map(PixChaveMapper::toConsultaDTO)
                .orElseThrow(() -> new IllegalArgumentException("Chave PIX não encontrada."));
    }


    public List<PixChaveConsultaRespostaDTO> consultarPorFiltros(PixChaveFiltroDTO filtroDTO) {
        PixChave filtro = PixChaveMapper.toEntity(filtroDTO);
        Example<PixChave> example = Example.of(filtro, ExampleMatcher.matchingAll()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase()
                        .withIgnoreNullValues());

        return pixChaveRepository.findAll(example)
                .stream()
                .map(PixChaveMapper::toConsultaDTO)
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

        PixChave novaChave = PixChaveMapper.criarChavePixToEntity(dto);
        PixChave chaveSalva = pixChaveRepository.save(novaChave);

        return chaveSalva.getId();
    }

    private void validarRegrasDeCadastro(PixChaveCriacaoDTO dto) {
        switch (dto.tipoChave()) {
            case CELULAR -> validarCelular(dto.valorChave());
            case EMAIL -> validarEmail(dto.valorChave());
            case CPF -> validarCpf(dto.valorChave());
            case CNPJ -> validarCnpj(dto.valorChave());
            case ALEATORIO -> validarChaveAleatoria(dto.valorChave());
            default -> throw new ChavePixInvalidaException("Tipo de chave inválido.");
        }
    }

    private void validarCelular(String celular) {
        if (!Pattern.matches("^\\+\\d{1,2}\\d{1,3}\\d{9}$", celular)) {
            throw new ChavePixInvalidaException("Número de celular inválido. O formato correto é +[código do país][DDD][número com 9 dígitos].");
        }
    }

    private void validarEmail(String email) {
        EmailValidator validator = new EmailValidator();
        if (!validator.isValid(email, null)) {
            throw new ChavePixInvalidaException("E-mail inválido. O formato correto deve conter '@' e seguir as regras de e-mail.");
        } else if (email.length() > 77) {
            throw new ChavePixInvalidaException("E-mail inválido. Deve conter no máximo 77 caracteres.");
        }
    }


    private void validarCpf(String cpf) {
        CPFValidator validator = new CPFValidator();
        validator.initialize(null);
        if (!validator.isValid(cpf, null)) {
            throw new ChavePixInvalidaException("CPF inválido. Deve conter exatamente 11 dígitos numéricos e ser um CPF válido.");
        }
    }

    private void validarCnpj(String cnpj) {
        CNPJValidator validator = new CNPJValidator();
        validator.initialize(null);
        if (!validator.isValid(cnpj, null)) {
            throw new ChavePixInvalidaException("CNPJ inválido. Deve conter exatamente 14 dígitos numéricos e ser um CNPJ válido.");
        }
    }

    private void validarChaveAleatoria(String chave) {
        if (chave.length() > 36) {
            throw new ChavePixInvalidaException("Chave aleatória inválida. Deve conter no máximo 36 caracteres.");
        }
    }


    @Transactional
    public PixChaveDTO alterarChave(PixChaveAlteracaoDTO dto) {
        System.out.println("🚀 Entrou no método alterarChave() com DTO: " + dto);

        PixChave chave = pixChaveRepository.findById(dto.id())
                .orElseThrow(() -> new ChavePixNaoEncontradaException("Chave PIX não encontrada com id " + dto.id()));

        System.out.println("🔍 Chave encontrada: " + chave);

        if (chave.getStatus() == StatusChave.INATIVA) {
            System.out.println("⚠️ Tentativa de alteração de chave inativa: " + chave.getId());
            throw new ChavePixInativadaException("Não é permitido alterar chaves inativadas.");
        }

        chave.setTipoConta(dto.tipoConta());
        chave.setNumeroAgencia(dto.numeroAgencia());
        chave.setNumeroConta(dto.numeroConta());
        chave.setNomeCorrentista(dto.nomeCorrentista());
        chave.setSobrenomeCorrentista(dto.sobrenomeCorrentista());

        PixChave chaveSalva = pixChaveRepository.save(chave);

            return PixChaveMapper.toDTO(chaveSalva);
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
        return PixChaveMapper.toDTO(chaveSalva);
    }
}
