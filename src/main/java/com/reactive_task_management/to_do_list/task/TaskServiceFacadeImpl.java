package com.reactive_task_management.to_do_list.task;

import org.springframework.stereotype.Component;

@Component
class TaskServiceFacadeImpl implements TaskServiceFacade {

    private final TaskService taskService;

    TaskServiceFacadeImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public TaskResponseDTO getTaskById(String id) {
        return null;
    }
}
