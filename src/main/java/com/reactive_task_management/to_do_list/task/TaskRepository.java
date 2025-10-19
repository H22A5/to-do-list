package com.reactive_task_management.to_do_list.task;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Flux<Task> findAllByUserId(String userId);
}
