package app.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.github.slugify.Slugify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.error.InvalidOptionException;
import app.error.NotAuthenticatedException;
import app.error.OptionNotFoundException;
import app.error.PollAlreadyOwnedException;
import app.error.PollNotFoundException;
import app.error.UserVoteNotFoundException;
import app.model.Option;
import app.model.Poll;
import app.model.User;
import app.model.Vote;
import app.repository.OptionRepository;
import app.repository.PollRepository;
import app.repository.VoteRepository;

@Component
public class PollHandler {

    private PollRepository pollRepo;
    private OptionRepository optionRepo;
    private VoteRepository voteRepo;
    private SecurityService security;
    private HttpServletRequest request;

    @Autowired
    public PollHandler(
        PollRepository pollRepo,
        OptionRepository optionRepo,
        VoteRepository voteRepo,
        SecurityService security,
        HttpServletRequest request
    ) {
        this.pollRepo = pollRepo;
        this.optionRepo = optionRepo;
        this.voteRepo = voteRepo;
        this.security = security;
        this.request = request;
    }

    public Poll getPoll(UUID id) {
        return pollRepo
            .findById(id)
            .orElseThrow(() -> new PollNotFoundException(id));
    }

    public Poll getPoll(String slug) {
        return pollRepo
            .findBySlug(slug)
            .orElseThrow(() -> new PollNotFoundException(slug));
    }

    public Option getPollOption(Poll poll, UUID id) {
        Option option = optionRepo
            .findById(id)
            .orElseThrow(() -> new OptionNotFoundException(id));

        if (!option.getPoll().equals(poll)) {
            throw new InvalidOptionException();
        }

        return option;
    }

    public Vote getUserVote(Poll poll) {
        User user = security.getUser();
        return getUserVote(poll, user);
    }

    public Vote getUserVote(Poll poll, User user) {
        Vote vote = null;
        if (user == null) {
            // if request is available, check saved vote in the client session
            if (request != null) {
                String value = (String) request.getSession().getAttribute("vote-" + poll.getId().toString());
                if (value != null) {
                    UUID voteId = UUID.fromString(value);
                    if (voteId != null) {
                        vote = voteRepo.findById(voteId).get();
                    }
                }
            }
        } else {
            try {
                vote = voteRepo.findByUserAndPoll(user, poll).get();
            } catch (Throwable ex) {
            }
        }
        if (vote == null) {
            throw new UserVoteNotFoundException(poll);
        }
        return vote;
    }

    public Vote setUserVote(Poll poll, Option option) {
        User user = security.getUser();
        return setUserVote(poll, option, user);
    }

    public Vote setUserVote(Poll poll, Option option, User user) {
        Vote vote = null;

        if (poll.isRestricted() && user == null) {
            throw new NotAuthenticatedException();
        }

        try {
            vote = this.getUserVote(poll, user);
        } catch (UserVoteNotFoundException ex) {
            // new vote
        }

        if (vote == null) {
            vote = new Vote();
            vote.setPoll(poll);
            vote.setOption(option);
            vote.setUser(user);

            // increment poll votes (improve it!)
            poll.setVotes(poll.getVotes() + 1);
            pollRepo.save(poll);

            // increment option votes (improve it!)
            option.setVotes(option.getVotes() + 1);
            optionRepo.save(option);

            vote = voteRepo.save(vote);

            // if request is available, save in the client session
            if (request != null) {
                request.getSession().setAttribute("vote-" + poll.getId().toString(), vote.getId().toString());
            }
        } else {
            if (!vote.getOption().getId().equals(option.getId())) {
                // decrement previous option votes (improve it!)
                vote.getOption().setVotes(vote.getOption().getVotes() - 1);
                optionRepo.save(vote.getOption());
                // increment new option votes (improve it!)
                option.setVotes(option.getVotes() + 1);
                optionRepo.save(option);
            }

            vote.setOption(option);
            vote.setUser(user);

            vote = voteRepo.save(vote);
        }

        return vote;
    }

    public Poll save(Poll poll) {
        handlePreSave(poll);
        return pollRepo.save(poll);
    }

    public Poll claim(Poll poll, User user) {
        if (poll.getOwner() != null && !poll.getOwner().equals(user)) {
            throw new PollAlreadyOwnedException(poll);
        }
        poll.setOwner(user);
        return pollRepo.save(poll);
    }

    private void handlePreSave(Poll poll) {
        boolean exits = false;
        int attempts = 1;
        String slugBase = new Slugify().slugify(poll.getTitle());
        do {
            String slug = slugBase;
            if (attempts > 1) {
                slug += "-" + attempts;
            }
            poll.setSlug(slug);
            try {
                Poll current = pollRepo.findBySlug(slug).get();
                exits = current != null && !current.getId().equals(poll.getId());
            } catch (RuntimeException ex) {
                exits = false;
            }
            attempts++;
        } while (exits);

        int i = 0;
        int votes = 0;
        for (Option option : poll.getOptions()) {
            option.setPosition(i);
            option.setPoll(poll);
            votes += option.getVotes();
            i++;
        }
        
        poll.setVotes(votes);

        if (poll.getOwner() == null) {
            poll.setOwner(security.getUser());
        }
    }
}