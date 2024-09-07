package com.Stapi.task.Repository;

import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import com.Stapi.task.repositories.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTests {
    @Autowired
    private TaskRepository taskRepository;

    private User user;
    private Task task;

    @BeforeEach
    public void init(){
        user = User.builder()
                .id(1)
                .username("Stapi")
                .password("Test")
                .build();
        task = Task.builder()
                .description("test")
                .user(user)
                .build();
    }

    @Test
    public void TaskRepository_FindByUser_ReturnsListofTasks(){
        taskRepository.save(task);

        List<Task> tasks = taskRepository.findByUser(user);

        Assertions.assertThat(tasks).isNotNull();
        Assertions.assertThat(tasks.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void TaskRepository_FindByIdAndUser_ReturnsOptional(){
        taskRepository.save(task);

        Optional<Task> response = taskRepository.findByIdAndUser(task.getId(), user);  // Use task.getId()

        Assertions.assertThat(response).isPresent();  // Ensure the response is present
        Assertions.assertThat(response.get().getUser()).isEqualTo(user);
    }

    @Test
    public void TaskRepository_FindByIdAndUser_ReturnsEmpty(){
        taskRepository.save(task);

        User randomUser = User.builder()
                .id(9)  // Random ID that doesn't match any existing user
                .username("Random")
                .password("RandomPass")
                .build();

        Optional<Task> response = taskRepository.findByIdAndUser(2, randomUser);

        Assertions.assertThat(response).isEmpty();
    }

}
