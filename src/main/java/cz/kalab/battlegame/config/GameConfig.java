package cz.kalab.battlegame.config;

import cz.kalab.battlegame.game.Dice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Registers plain domain objects as Spring beans,
 * so they can be injected (and replaced in tests).
 */
@Configuration
public class GameConfig {

    @Bean
    public Dice dice() {
        return new Dice();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
