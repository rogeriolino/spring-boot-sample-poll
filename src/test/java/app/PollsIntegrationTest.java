package app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import app.model.Poll;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("application-integrationtest.properties")
public class PollsIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testPollOperations() throws Exception {
		String jsonData = "{ \"title\": \"poll title\", \"description\": \"poll desc\", \"options\": ["
			+ "{ \"name\": \"option1\" },"
			+ "{ \"name\": \"option2\" }"
			+ "] }";
			
		// first poll creation
		MvcResult result = mvc
			.perform(
				post("/api/polls")
				.content(jsonData)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.id").exists()
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.title").value("poll title")
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.slug").value("poll-title")
			)
			.andReturn();
			
		// second poll with same title (test slug suffix)
		mvc
			.perform(
				post("/api/polls")
				.content(jsonData)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.id").exists()
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.title").value("poll title")
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.slug").value("poll-title-2")
			);

		// test not found
		String id = "63c42470-2bb3-44a4-800e-dfbc48da3abb";
		mvc
			.perform(get("/api/polls/" + id))
			.andExpect(status().isNotFound());

		// casting first response to Poll object
		String body = result.getResponse().getContentAsString();
		Poll poll = objectMapper.readValue(body, Poll.class);
		
		// test api get by id
		mvc
			.perform(get("/api/polls/" + poll.getId()))
			.andExpect(status().isOk())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.title").value(poll.getTitle())
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.slug").value(poll.getSlug())
			);

		// test api get by slug
		mvc
			.perform(get("/api/polls/" + poll.getSlug() + "/slug"))
			.andExpect(status().isOk())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.title").value(poll.getTitle())
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.slug").value(poll.getSlug())
			);
	}
}