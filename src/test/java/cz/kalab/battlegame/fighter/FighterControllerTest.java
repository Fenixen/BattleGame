package cz.kalab.battlegame.fighter;

import cz.kalab.battlegame.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web layer slice test - only the controller and MVC infrastructure are started,
 * the service is replaced by a Mockito mock.
 */
@WebMvcTest(FighterController.class)
class FighterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FighterService fighterService;

    private final FighterResponse gandalf =
            new FighterResponse(1L, "Gandalf", FighterType.MAGE, 100, 30, 10, 25, 60);

    @Test
    void listsFighters() throws Exception {
        when(fighterService.findAll()).thenReturn(List.of(gandalf));

        mockMvc.perform(get("/api/fighters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gandalf"))
                .andExpect(jsonPath("$[0].type").value("MAGE"));
    }

    @Test
    void returns404ForUnknownFighter() throws Exception {
        when(fighterService.findById(99L)).thenThrow(new NotFoundException("Fighter", 99L));

        mockMvc.perform(get("/api/fighters/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Fighter with id 99 was not found"));
    }

    @Test
    void createsFighter() throws Exception {
        when(fighterService.create(any())).thenReturn(gandalf);

        mockMvc.perform(post("/api/fighters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Gandalf", "type": "MAGE", "health": 100, "damage": 30,
                                 "defense": 10, "mana": 25, "magicDamage": 60}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/fighters/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void rejectsInvalidFighter() throws Exception {
        mockMvc.perform(post("/api/fighters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "", "type": "WARRIOR", "health": 0, "damage": 30, "defense": 10}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.health").exists());

        verify(fighterService, never()).create(any());
    }

    @Test
    void mageRequiresMana() throws Exception {
        mockMvc.perform(post("/api/fighters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Merlin", "type": "MAGE", "health": 80, "damage": 20, "defense": 10}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mageAttributesValid").value("mana and magicDamage are required for a MAGE"));
    }
}
