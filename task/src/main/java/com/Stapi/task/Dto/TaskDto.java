package com.Stapi.task.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Data
public class TaskDto {
    private LocalDate deadline;
    private String description;
    private String status;
}
