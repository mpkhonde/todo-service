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
    // SKAPA
    // ---------------------------------------------------
    @Transactional
    public Todo create(String title) {
        return repo.save(new Todo(title));
    }

    // ---------------------------------------------------
    // UPPDATERA
    // ---------------------------------------------------
    @Transactional
    public Optional<Todo> update(Long id, String newTitle) {
        return repo.findById(id).map(todo -> {
            todo.setTitle(newTitle);
            return repo.save(todo);
        });
    }

    // ---------------------------------------------------
    // DELETE (en)
    // ---------------------------------------------------
    @Transactional
    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    // ---------------------------------------------------
    // BULK-DELETE
    // ---------------------------------------------------
    @Transactional
    public BulkDeleteResponse deleteMany(List<Long> ids) {
        var unique = ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        var existing = repo.findAllById(unique).stream()
                .map(Todo::getId)
                .toList();

        repo.deleteAllById(existing);

        var notFound = new ArrayList<>(unique);
        notFound.removeAll(existing);

        return new BulkDeleteResponse(existing, notFound);
    }

    // ---------------------------------------------------
    // DELETE ALL
    // ---------------------------------------------------
    @Transactional
    public void deleteAll() {
        repo.deleteAll();
    }
}
