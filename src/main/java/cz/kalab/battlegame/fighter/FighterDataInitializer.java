package cz.kalab.battlegame.fighter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the in-memory database with a few fighters, so the game can be played right after startup.
 */
@Component
public class FighterDataInitializer implements ApplicationRunner {

    private final FighterRepository repository;

    public FighterDataInitializer(FighterRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }
        repository.save(new Fighter("Zalgoren", FighterType.WARRIOR, 150, 40, 15, null, null));
        repository.save(new Fighter("Gandalf", FighterType.MAGE, 100, 30, 10, 25, 60));
        repository.save(new Fighter("Brienne", FighterType.WARRIOR, 130, 35, 20, null, null));
    }
}
