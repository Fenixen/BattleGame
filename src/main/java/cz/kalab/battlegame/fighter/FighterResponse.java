package cz.kalab.battlegame.fighter;

public record FighterResponse(Long id, String name, FighterType type, int health, int damage, int defense,
                              Integer mana, Integer magicDamage) {

    public static FighterResponse from(Fighter fighter) {
        return new FighterResponse(fighter.getId(), fighter.getName(), fighter.getType(), fighter.getHealth(),
                fighter.getDamage(), fighter.getDefense(), fighter.getMana(), fighter.getMagicDamage());
    }
}
