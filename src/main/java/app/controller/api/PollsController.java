package app.controller.api;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.error.NotAuthenticatedException;
import app.error.PermissionDeniedException;
import app.error.PollAlreadyOwnedException;
import app.model.Option;
import app.model.Poll;
import app.model.User;
import app.repository.PollRepository;
import app.repository.UserRepository;
import app.service.PollHandler;
import app.service.SecurityService;

@RestController
@RequestMapping("/api/polls")
public class PollsController {

    public static final int MAX_PAGE_SIZE = 100;

    private PollHandler handler;
    private SecurityService security;
    private PollRepository repository;
    private UserRepository userRepo;

    @Autowired
    public PollsController(
        PollHandler handler,
        SecurityService security,
        PollRepository repository,
        UserRepository userRepo
    ) {
        this.handler = handler;
        this.security = security;
        this.repository = repository;
        this.userRepo = userRepo;
    }

    @GetMapping("")
    public Iterable<Poll> findAll(
        @RequestParam(defaultValue="0") Integer pageNo, 
        @RequestParam(defaultValue="20") Integer pageSize,
        @RequestParam(defaultValue="id") String sortBy,
        @RequestParam(defaultValue="ASC") String order,
        @RequestParam(required=false, name="owner") UUID ownerId
    ) {
        Sort.Direction direction;
        if (order.toUpperCase().equals("DESC")) {
            direction = Sort.Direction.DESC;
        } else {
            direction = Sort.Direction.ASC;
        }
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
        if (ownerId != null) {
            try {
                User owner = userRepo.findById(ownerId).get();
                return repository.findByOwner(owner, paging);
            } catch (Throwable ex) {
            }
        }
        return repository.findAll(paging);
    }

    @GetMapping("/{id}")
    public Poll findOne(@PathVariable UUID id) {
        return handler.getPoll(id);
    }

    @GetMapping("/{slug}/slug")
    public Poll findOne(@PathVariable String slug) {
        return handler.getPoll(slug);
    }

    @PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
    public Poll add(@Valid @RequestBody Poll poll) {
        return handler.save(poll);
    }

    @PutMapping("/{id}")
    public Poll update(@Valid @RequestBody Poll poll, @PathVariable UUID id) {
        if (poll.getOwner() != null) {
            User user = security.getUser();
            if (!poll.getOwner().equals(user)) {
                throw new PermissionDeniedException();
            }
        }
        return handler.save(poll);
    }

    @PostMapping("/{id}/claim")
    public Poll claim(@PathVariable UUID id) {
        User user = security.getUser();
        if (user == null) {
            throw new NotAuthenticatedException();
        }
        Poll poll = handler.getPoll(id);
        return handler.claim(poll, user);
    }
}
