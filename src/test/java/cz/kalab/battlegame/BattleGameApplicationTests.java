package cz.kalab.battlegame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test - starts the whole application with the in-memory database.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BattleGameApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void seedsDefaultFighters() throws Exception {
        mockMvc.perform(get("/api/fighters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItems("Zalgoren", "Gandalf")));
    }

    @Test
    void fightsBattleAndStoresItInHistory() throws Exception {
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fighterAId": 1, "fighterBId": 2}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.events").isNotEmpty())
                .andExpect(jsonPath("$.events[0].fighters.length()").value(2));

        mockMvc.perform(get("/api/battles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].fighterA", hasItem("Zalgoren")));
    }

    @Test
    void rejectsBattleWithUnknownFighter() throws Exception {
        mockMvc.perform(post("/api/battles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fighterAId": 1, "fighterBId": 999}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsDuplicateFighterName() throws Exception {
        mockMvc.perform(post("/api/fighters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "gandalf", "type": "WARRIOR", "health": 100, "damage": 30, "defense": 10}
                                """))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/fighters"))
                .andExpect(jsonPath("$[*].name", not(hasItem("gandalf"))));
    }
}
