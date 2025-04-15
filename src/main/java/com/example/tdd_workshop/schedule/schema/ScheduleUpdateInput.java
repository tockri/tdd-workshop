package com.example.tdd_workshop.schedule.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;

@Schema(
        description = "スケジュール更新時のリクエストボディ。プロパティはすべてオプショナル。指定したプロパティのみ更新する。"
)
public record ScheduleUpdateInput(
        @NonNull @Schema(description = "タイトル", example = "入学式") Optional<String> title,
        @NonNull @Schema(description = "説明", example = "8時開場、9時開式") Optional<String> description,
        @NonNull @Schema(description = "開始日時", example = "2025-04-10T08:00:00") Optional<LocalDateTime> startTime,
        @NonNull @Schema(description = "終了日時", example = "2025-04-10T09:00:00") Optional<LocalDateTime> endTime
) {
}