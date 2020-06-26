package com.akosszabo.demo.mancala;

import com.akosszabo.demo.mancala.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = MancalaServiceApplication.class)
@AutoConfigureMockMvc
public class MancalaServiceIntTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testMakeRegularMoveOnMatch1() throws Exception {
        final String id = "1";

        MvcResult result = mvc.perform(get("/api/game/fetch").param("matchId", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String expectedContent = TestUtil.readJSONFile("match" + id);
        assertEquals(expectedContent, responseContent);


        result = mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"" + id + "\",\n" +
                        "\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        responseContent = result.getResponse().getContentAsString();
        expectedContent = TestUtil.readJSONFile("match" + id + "_step1");
        assertEquals(expectedContent, responseContent);

        result = mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"" + id + "\",\n" +
                        "\"player\" : \"PLAYER2\",\n" +
                        "\"pitNumber\" : 9}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        responseContent = result.getResponse().getContentAsString();
        expectedContent = TestUtil.readJSONFile("match" + id + "_step2");
        assertEquals(expectedContent, responseContent);
    }

    @Test
    public void testCaptureMoveOnMatch2() throws Exception {
        final String id = "2";

        MvcResult result = mvc.perform(get("/api/game/fetch").param("matchId", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String expectedContent = TestUtil.readJSONFile("match" + id);
        assertEquals(expectedContent, responseContent);

        result = mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"" + id + "\",\n" +
                        "\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 2}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        responseContent = result.getResponse().getContentAsString();
        expectedContent = TestUtil.readJSONFile("match" + id + "_step1");
        assertEquals(expectedContent, responseContent);
    }

    @Test
    public void testDoubleTurnMoveOnMatch3() throws Exception {
        final String id = "3";

        MvcResult result = mvc.perform(get("/api/game/fetch").param("matchId", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String expectedContent = TestUtil.readJSONFile("match" + id);
        assertEquals(expectedContent, responseContent);

        result = mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"" + id + "\",\n" +
                        "\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 5}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        responseContent = result.getResponse().getContentAsString();
        expectedContent = TestUtil.readJSONFile("match" + id + "_step1");
        assertEquals(expectedContent, responseContent);
    }

    @Test
    public void testPlayer2WinningMatch4() throws Exception {
        final String id = "4";

        MvcResult result = mvc.perform(get("/api/game/fetch").param("matchId", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        String expectedContent = TestUtil.readJSONFile("match" + id);
        assertEquals(expectedContent, responseContent);

        result = mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"" + id + "\",\n" +
                        "\"player\" : \"PLAYER2\",\n" +
                        "\"pitNumber\" : 12}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        responseContent = result.getResponse().getContentAsString();
        expectedContent = TestUtil.readJSONFile("match" + id + "_step1");
        assertEquals(expectedContent, responseContent);
    }

    @Test
    public void testFetchingMatch5_ResultsInError() throws Exception {
        final String id = "5";

        mvc.perform(get("/api/game/fetch").param("matchId", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn();

        MvcResult result = mvc.perform(get("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final String responseContent = result.getResponse().getContentAsString();
        final String expectedContent = TestUtil.readJSONFile("match" + id + "_new");
        assertEquals(expectedContent, responseContent);
    }


}
