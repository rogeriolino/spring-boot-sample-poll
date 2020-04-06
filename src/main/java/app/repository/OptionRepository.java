package app.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import app.model.Option;

public interface OptionRepository extends CrudRepository<Option, UUID> {
}