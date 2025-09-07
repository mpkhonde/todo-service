package se.moise.todoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.moise.todoservice.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {}
