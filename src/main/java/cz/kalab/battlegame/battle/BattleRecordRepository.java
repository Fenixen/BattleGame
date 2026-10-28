package cz.kalab.battlegame.battle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleRecordRepository extends JpaRepository<BattleRecord, Long> {

    List<BattleRecord> findAllByOrderByFoughtAtDesc();
}
