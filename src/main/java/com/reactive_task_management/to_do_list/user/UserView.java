package com.reactive_task_management.to_do_list.user;

public record UserView(String id,
                       String username) {

    static UserView fromUser(User user) {
        return new UserView(user.getId(), user.getUsername());
    }
}
