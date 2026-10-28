package cz.kalab.battlegame.fighter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FighterRepository extends JpaRepository<Fighter, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
