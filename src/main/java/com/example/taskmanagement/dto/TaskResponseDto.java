package com.example.taskmanagement.dto;

import com.example.taskmanagement.model.TaskStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;  // Only include the username of the user, not the entire user object
} 