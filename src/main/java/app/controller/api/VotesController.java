package app.controller.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.model.Option;
import app.model.Poll;
import app.model.Vote;
import app.service.PollHandler;

@RestController
@RequestMapping("/api/polls/{pollId}")
public class VotesController {

    private PollHandler handler;

    @Autowired
    public VotesController(PollHandler handler) {
        this.handler = handler;
    }

    @GetMapping("/vote")
	@ResponseStatus(HttpStatus.CREATED)
    public Vote get(@PathVariable UUID pollId) {
        Poll poll = handler.getPoll(pollId);
        Vote vote = handler.getUserVote(poll);
        return vote;
    }

    @PostMapping("/vote/{optionId}")
    public Vote vote(@PathVariable UUID pollId, @PathVariable UUID optionId) {
        Poll poll = handler.getPoll(pollId);
        Option option = handler.getPollOption(poll, optionId);
        Vote vote = handler.setUserVote(poll, option);

        return vote;
    }
}
