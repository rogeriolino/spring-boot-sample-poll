package app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import app.model.Poll;
import app.model.User;

public interface PollRepository extends PagingAndSortingRepository<Poll, UUID> {
    Page<Poll> findByOwner(User owner, Pageable pageable);
    Optional<Poll> findBySlug(String slug);
}