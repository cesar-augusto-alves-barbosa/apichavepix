package io.github.cesar_augusto_alves_barbosa.apichavepix.repository;

import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import io.github.cesar_augusto_alves_barbosa.apichavepix.enums.TipoChave;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface PixChaveRepository extends JpaRepository<PixChave, UUID> {

    Optional<PixChave> findByValorChave(String valorChave);

    List<PixChave> findByTipoChave(TipoChave tipoChave);

    List<PixChave> findByNumeroAgenciaAndNumeroConta(Integer numeroAgencia, Integer numeroConta);

    List<PixChave> findByNomeCorrentistaContainingIgnoreCase(String nome);

    List<PixChave> findByDataCriacao(LocalDateTime dataCriacao);

    List<PixChave> findByDataInativacao(LocalDateTime dataInativacao);

    long countByNumeroConta(@NotNull(message = "O número da conta é obrigatório") @Digits(integer = 8, fraction = 0) Integer integer);
}
