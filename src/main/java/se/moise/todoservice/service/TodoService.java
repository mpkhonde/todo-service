package se.moise.todoservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.moise.todoservice.dto.BulkDeleteResponse;
import se.moise.todoservice.model.Todo;
import se.moise.todoservice.repository.TodoRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    // ---------------------------------------------------
    // LÄS
    // ---------------------------------------------------
    public List<Todo> list() { return repo.findAll(); }

    public Optional<Todo> find(Long id) { return repo.findById(id); }

    // ---------------------------------------------------
    // SKRIV
    // ---------------------------------------------------
    @Transactional
    public Todo create(String title) {
        return repo.save(new Todo(title));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    // ---------------------------------------------------
    // BULK-DELETE: radera flera id:n
    // ---------------------------------------------------
    @Transactional
    public BulkDeleteResponse deleteMany(List<Long> ids) {
        // unika, filtrera bort null
        var unique = ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // vilka finns?
        var existing = repo.findAllById(unique).stream()
                .map(Todo::getId)
                .toList();

        // radera de som finns
        repo.deleteAllById(existing);

        // kvar = not found
        var notFound = new ArrayList<>(unique);
        notFound.removeAll(existing);

        return new BulkDeleteResponse(existing, notFound);
    }

    // ---------------------------------------------------
    // DELETE ALL: radera allt
    // ---------------------------------------------------
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }
}
