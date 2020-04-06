package app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.model.Option;
import app.model.Poll;
import app.model.User;
import app.model.Vote;

public interface VoteRepository extends CrudRepository<Vote, UUID> {
    long countByPoll(Poll poll);
    long countByOption(Option poll);
    Optional<Vote> findByUserAndPoll(User user, Poll poll);
}