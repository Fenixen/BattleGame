package cz.kalab.battlegame.battle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Battle history entry. Fighter names are stored as a snapshot,
 * so the history stays readable even after a fighter is renamed or deleted.
 */
@Entity
@Table(name = "battles")
public class BattleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fighterA;

    @Column(nullable = false)
    private String fighterB;

    /** {@code null} means a draw */
    private String winner;

    private int rounds;

    @Column(nullable = false)
    private Instant foughtAt;

    protected BattleRecord() {
        // required by JPA
    }

    public BattleRecord(String fighterA, String fighterB, String winner, int rounds, Instant foughtAt) {
        this.fighterA = fighterA;
        this.fighterB = fighterB;
        this.winner = winner;
        this.rounds = rounds;
        this.foughtAt = foughtAt;
    }

    public Long getId() {
        return id;
    }

    public String getFighterA() {
        return fighterA;
    }

    public String getFighterB() {
        return fighterB;
    }

    public String getWinner() {
        return winner;
    }

    public int getRounds() {
        return rounds;
    }

    public Instant getFoughtAt() {
        return foughtAt;
    }
}
