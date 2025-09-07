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
    public TodoController(TodoService service) { this.service = service; }

    // ---------------------------------------------------
    // GET
    // ---------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Todo>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return service.find(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------
    // POST
    // ---------------------------------------------------
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Todo> createJson(@Valid @RequestBody CreateTodoRequest req) {
        Todo saved = service.create(req.title());
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(saved);
    }

    @PostMapping(params = "title")
    public ResponseEntity<Todo> createQuery(@RequestParam @NotBlank String title) {
        Todo saved = service.create(title);
        return ResponseEntity.created(URI.create("/api/todos/" + saved.getId())).body(saved);
    }

    // ---------------------------------------------------
    // DELETE (en)
    // ---------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ---------------------------------------------------
    // DELETE (många)
    // ---------------------------------------------------
    // A) Query-param: DELETE /api/todos?ids=3,4,5
    @DeleteMapping(params = "ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BulkDeleteResponse> deleteManyQuery(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.deleteMany(ids));
    }

    // B) Path: DELETE /api/todos/bulk/3,4,5
    @DeleteMapping("/bulk/{ids}")
    public ResponseEntity<BulkDeleteResponse> deleteManyPath(@PathVariable List<Long> ids) {
        return ResponseEntity.ok(service.deleteMany(ids));
    }

    // ---------------------------------------------------
    // DELETE ALL: DELETE /api/todos
    // ---------------------------------------------------
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
