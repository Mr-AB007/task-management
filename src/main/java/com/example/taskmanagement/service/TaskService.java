package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDto;
import com.example.taskmanagement.dto.TaskResponseDto;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.exception.UserNotFoundException;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private TaskResponseDto mapToDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setUsername(task.getUser().getUsername());
        return dto;
    }

    @Transactional
    public TaskResponseDto createTask(TaskDto taskDto, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setUser(user);

        return mapToDto(taskRepository.save(task));
    }

    public Page<TaskResponseDto> getAllTasks(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Page<Task> tasks;
        if (user.getRoles().contains("ROLE_ADMIN")) {
            tasks = taskRepository.findAll(pageable);
        } else {
            tasks = taskRepository.findByUserId(user.getId(), pageable);
        }
        return tasks.map(this::mapToDto);
    }

    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskDto taskDto, String username) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (!task.getUser().getId().equals(user.getId()) && 
            !user.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Not authorized to update this task");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());

        return mapToDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (!task.getUser().getId().equals(user.getId()) && 
            !user.getRoles().contains("ROLE_ADMIN")) {
            throw new RuntimeException("Not authorized to delete this task");
        }

        taskRepository.delete(task);
    }
} 