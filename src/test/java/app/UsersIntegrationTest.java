package app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("application-integrationtest.properties") 
public class UsersIntegrationTest {
	
	@Autowired
    private MockMvc mvc;

	@Test
	public void testUsersOpertions() throws Exception {
		// test create new user
		String jsonData = "{ \"username\": \"user1\", \"password\": \"123456\" }";
		mvc
			.perform(
				post("/api/users")
				.content(jsonData)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.id").exists()
			)
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.username").value("user1")
			);

		// test user validation
		String jsonData2 = "{ \"username\": \"u\", \"password\": \"1\" }";
		mvc
			.perform(
				post("/api/users")
				.content(jsonData2)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest())
			.andExpect(
				MockMvcResultMatchers.jsonPath("$.errors.username[0]").value("Minimum 3 characteres")
			);
			
		// test duplicated username
		mvc
			.perform(
				post("/api/users")
				.content(jsonData)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isBadRequest());
		
		// test user not found
		String id = "63c42470-2bb3-44a4-800e-dfbc48da3abb";
		mvc
			.perform(get("/api/users/" + id))
			.andExpect(status().isNotFound());
	}
}