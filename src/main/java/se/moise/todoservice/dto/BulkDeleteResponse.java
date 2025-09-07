package se.moise.todoservice.dto;

import java.util.List;

/** Resultat för bulk-radering. */
public record BulkDeleteResponse(
        List<Long> deleted,
        List<Long> notFound
) {}
