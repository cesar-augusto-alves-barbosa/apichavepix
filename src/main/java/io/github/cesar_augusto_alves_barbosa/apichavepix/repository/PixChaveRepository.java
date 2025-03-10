package io.github.cesar_augusto_alves_barbosa.apichavepix.repository;

import io.github.cesar_augusto_alves_barbosa.apichavepix.entity.PixChave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface PixChaveRepository extends JpaRepository<PixChave, UUID> {

    Optional<PixChave> findByValorChave(String valorChave);

    List<PixChave> findByNumeroAgenciaAndNumeroConta(Integer numeroAgencia, Integer numeroConta);

    long countByNumeroAgenciaAndNumeroConta(Integer numeroAgencia, Integer numeroConta);
}
