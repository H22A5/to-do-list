package com.reactive_task_management.to_do_list.user;

record UserResponse(String id,
                    String username) {

    static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}
