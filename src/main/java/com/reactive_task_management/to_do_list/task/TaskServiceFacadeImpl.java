package com.reactive_task_management.to_do_list.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TaskServiceFacadeImpl implements TaskServiceFacade {

    private final TaskService taskService;

    @Override
    public TaskView getTaskById(String id) {
        return null;
    }
}
