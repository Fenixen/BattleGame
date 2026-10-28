package cz.kalab.battlegame.fighter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Stored fighter definition (a "character sheet").
 * A new {@link cz.kalab.battlegame.game.Warrior} with full health is created from it for every battle.
 */
@Entity
@Table(name = "fighters")
public class Fighter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FighterType type;

    private int health;
    private int damage;
    private int defense;

    /** Only used by {@link FighterType#MAGE} */
    private Integer mana;
    /** Only used by {@link FighterType#MAGE} */
    private Integer magicDamage;

    protected Fighter() {
        // required by JPA
    }

    public Fighter(String name, FighterType type, int health, int damage, int defense,
                   Integer mana, Integer magicDamage) {
        update(name, type, health, damage, defense, mana, magicDamage);
    }

    public void update(String name, FighterType type, int health, int damage, int defense,
                       Integer mana, Integer magicDamage) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.damage = damage;
        this.defense = defense;
        this.mana = type == FighterType.MAGE ? mana : null;
        this.magicDamage = type == FighterType.MAGE ? magicDamage : null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FighterType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public Integer getMana() {
        return mana;
    }

    public Integer getMagicDamage() {
        return magicDamage;
    }
}
