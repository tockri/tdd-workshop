package com.example.tdd_workshop.schedule.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        description = "スケジュール作成時のリクエストボディ",
        requiredProperties = {
                "title",
                "description",
                "startTime",
                "endTime"
        }
)
public record ScheduleCreateInput(
        @NonNull @Schema(description = "タイトル", example = "入学式")
        String title,
        @NonNull @Schema(description = "説明", example = "8時開場、9時開式")
        String description,
        @NonNull @Schema(description = "開始日時", example = "2025-04-10T08:00:00")
        LocalDateTime startTime,
        @NonNull @Schema(description = "終了日時", example = "2025-04-10T09:00:00")
        LocalDateTime endTime
) {
}
