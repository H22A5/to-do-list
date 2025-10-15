package com.reactive_task_management.to_do_list.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface UserRepository extends ReactiveMongoRepository<User, String> {
}
