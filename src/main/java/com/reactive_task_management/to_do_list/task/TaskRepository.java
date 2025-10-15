package com.reactive_task_management.to_do_list.task;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface TaskRepository extends ReactiveMongoRepository<Task, String> {

}
