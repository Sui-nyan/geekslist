package hdm.stuttgart.geekslist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class TVControllerTest {
    @Autowired
    private MockMvc tvController;
    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(tvController).isNotNull();
    }

    @Test
    public void shouldDoSth() throws Exception {
        this.tvController.perform(get("/tv"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OKOK")));
    }

    @Test
    public void shouldReturnSeason() throws Exception {
        this.tvController.perform(get("/tv/420/seasons"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OKOK")));
    }
    
}