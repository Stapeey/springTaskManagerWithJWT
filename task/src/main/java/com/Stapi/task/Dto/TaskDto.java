package com.Stapi.task.Dto;

import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskDto {
    private LocalDate deadline;
    private String description;
    private String status;
}
