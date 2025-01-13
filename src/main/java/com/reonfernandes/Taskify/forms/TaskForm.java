package com.reonfernandes.Taskify.forms;

import com.reonfernandes.Taskify.models.Category;
import com.reonfernandes.Taskify.models.Priority;
import com.reonfernandes.Taskify.models.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskForm {

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime localTime;

    private Priority priority;
    private Status status;
    private Category category;
}
