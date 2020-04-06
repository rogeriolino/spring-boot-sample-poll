package app.error;

import app.model.Poll;

public class PollAlreadyOwnedException extends ForbiddenException {

    private static final long serialVersionUID = 1L;

    public PollAlreadyOwnedException(Poll poll) {
        super("This poll already have an owner: " + poll.getTitle());
    }

}