package com.example.tdd_workshop.schedule.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        description = "スケジュール1つ分の情報"
)
public record Schedule(
        @NonNull @Schema(description = "ID", example = "1")
        Long id,
        @NonNull @Schema(description = "タイトル")
        String title,
        @NonNull @Schema(description = "説明", example = "8時開場、9時開式")
        String description,
        @NonNull @Schema(description = "開始日時", example = "2025-04-10T08:00:00")
        LocalDateTime startTime,
        @NonNull @Schema(description = "終了日時", example = "2025-04-10T09:00:00")
        LocalDateTime endTime
) {
}
