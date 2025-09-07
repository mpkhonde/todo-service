package se.moise.todoservice.dto;

import jakarta.validation.constraints.NotBlank;

/** DTO för JSON-body vid skapande av todo. */
public record CreateTodoRequest(
        @NotBlank String title
) {}
