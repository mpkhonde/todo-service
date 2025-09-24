package se.moise.todoservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.moise.todoservice.dto.BulkDeleteResponse;
import se.moise.todoservice.dto.CreateTodoRequest;
import se.moise.todoservice.model.Todo;
import se.moise.todoservice.service.TodoService;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    // ---------------------------------------------------
    // GET ALL
    // ---------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Todo>> list() {
        return ResponseEntity.ok(service.list());
    }

    // ---------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return service.find(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------
    // POST (JSON Body)
    // ---------------------------------------------------
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Todo> createJson(@Valid @RequestBody CreateTodoRequest req) {
        Todo saved = service.create(req.title());
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(saved);
    }

    // ---------------------------------------------------
    // POST (Query-param: ?title=Text)
    // ---------------------------------------------------
    @PostMapping(params = "title")
    public ResponseEntity<Todo> createQuery(@RequestParam @NotBlank String title) {
        Todo saved = service.create(title);
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(saved);
    }

    // ---------------------------------------------------
    // PUT (Update todo by id)
    // ---------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody CreateTodoRequest req) {
        return service.update(id, req.title())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------
    // PATCH (Toggle done/undone på en todo)
    // ---------------------------------------------------
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggle(@PathVariable Long id) {
        return service.toggle(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------
    // DELETE (en todo)
    // ---------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ---------------------------------------------------
    // DELETE (många - query)
    // ---------------------------------------------------
    @DeleteMapping(params = "ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BulkDeleteResponse> deleteManyQuery(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.deleteMany(ids));
    }

    // ---------------------------------------------------
    // DELETE (många - path)
    // ---------------------------------------------------
    @DeleteMapping("/bulk/{ids}")
    public ResponseEntity<BulkDeleteResponse> deleteManyPath(@PathVariable List<Long> ids) {
        return ResponseEntity.ok(service.deleteMany(ids));
    }

    // ---------------------------------------------------
    // DELETE ALL
    // ---------------------------------------------------
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
