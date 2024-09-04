package com.Stapi.task.repositories;

import com.Stapi.task.models.Task;
import com.Stapi.task.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{

    List<Task> findByUser(User user);

    Optional<Task> findByIdAndUser(Integer id, User user);

    //List<Task> findByUserAndStatus(User user, String status);

    //List<Task> findByUserAndDeadline(User user, Date deadline);

}
