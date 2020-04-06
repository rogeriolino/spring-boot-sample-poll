package app;

import app.error.NotAuthenticatedException;
import app.error.PollAlreadyOwnedException;
import app.error.PollNotFoundException;
import app.model.Option;
import app.model.Poll;
import app.model.User;
import app.model.Vote;
import app.repository.OptionRepository;
import app.repository.PollRepository;
import app.repository.UserRepository;
import app.repository.VoteRepository;
import app.service.PollHandler;
import app.service.SecurityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import com.github.slugify.Slugify;

@DataJpaTest
@TestPropertySource("application-integrationtest.properties") 
public class PollHandlerUnitTest {

	@Autowired
    private PollRepository pollRepo;

	@Autowired
    private OptionRepository optionRepo;

	@Autowired
    private VoteRepository voteRepo;

	@Autowired
	private UserRepository userRepo;
		
	private PollHandler createHandler() {
		var security = new SecurityService(userRepo);
		var handler = new PollHandler(pollRepo, optionRepo, voteRepo, security, null);
		return handler;
	}

	public static Poll createPoll(String title) {
		return createPoll(title, "Poll description", 3);
	}

	public static Poll createPoll(String title, String description, int numOptions) {
		Poll poll = new Poll();
		poll.setTitle(title);
		poll.setDescription(description);
		for (int i = 1; i <= numOptions; i++) {
			Option option = new Option();
			option.setPoll(poll);
			option.setName("Option " + i);
			poll.getOptions().add(option);
		}
		return poll;
	}

	@Test
	public void testCreatePoll() throws Exception {
		String title = "My poll";
		String description = "poll description";
		int numOptions = 3;
		String slug = new Slugify().slugify(title);
		Poll poll = createPoll(title, description, numOptions);

		PollHandler handler = createHandler();
		Poll createdPoll = handler.save(poll);

		assertTrue(createdPoll.getId() instanceof UUID);
		assertEquals(title, createdPoll.getTitle());
		assertEquals(slug, createdPoll.getSlug());
		assertEquals(numOptions, createdPoll.getOptions().size());

		// testing slug suffix
		poll = createPoll(title, description, numOptions);
		Poll createdPoll2 = handler.save(poll);
		assertEquals(slug + "-2", createdPoll2.getSlug());
	}
	
	@Test
	public void testGetPoll() throws Exception {
		Poll poll = createPoll("Another poll");

		var handler = createHandler();
		poll = handler.save(poll);

		assertThrows(PollNotFoundException.class, () -> {
			UUID unknownId = UUID.fromString("8474a975-c480-4272-bf1e-102a91f28f9b");
			handler.getPoll(unknownId);
		});

		Poll loadedPoll = handler.getPoll(poll.getId());
		assertNotNull(poll);
		assertEquals(poll.getSlug(), loadedPoll.getSlug());
		assertEquals(poll.getTitle(), loadedPoll.getTitle());
	}
	
	@Test
	public void testClaimPoll() throws Exception {
		User user1 = new User();
		user1.setUsername("user1");
		User user2 = new User();
		user2.setUsername("user2");
		Poll poll = createPoll("Poll to claim poll");

		var handler = createHandler();
		handler.claim(poll, user1);

		assertEquals(user1, poll.getOwner());

		assertThrows(PollAlreadyOwnedException.class, () -> {
			handler.claim(poll, user2);
		});
	}
	
	@Test
	public void testUserVote() throws Exception {
		var handler = createHandler();

		// create new user
		User newUser = new User();
		newUser.setUsername("newuser");
		userRepo.save(newUser);

		// create new poll
		Poll poll = createPoll("Poll to vote");
		handler.save(poll);

		// choose a option to vote
		Option optionToVote = poll.getOptions().iterator().next();
		Vote vote = null;

		// make a vote without user (user = null)
		vote = handler.setUserVote(poll, optionToVote, null);
		assertNotNull(vote);
		assertEquals(optionToVote, vote.getOption());
		assertEquals(1, vote.getOption().getVotes());
		assertEquals(1, vote.getPoll().getVotes());

		// turn poll restricted to authenticated user
		poll.setRestricted(true);
		handler.save(poll);

		// make a vote without user (user = null)
		assertThrows(NotAuthenticatedException.class, () -> {
			handler.setUserVote(poll, optionToVote, null);
		});
		
		// test vote with new user
		vote = handler.setUserVote(poll, optionToVote, newUser);
		assertNotNull(vote);
		assertEquals(optionToVote, vote.getOption());
		assertEquals(2, vote.getOption().getVotes());
		assertEquals(2, vote.getPoll().getVotes());
	}
}