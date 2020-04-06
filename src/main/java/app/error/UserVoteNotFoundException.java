package app.error;

import app.model.Poll;

public class UserVoteNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public UserVoteNotFoundException(Poll poll) {
        super("Vote not found for poll: " + poll.getTitle());
    }

}