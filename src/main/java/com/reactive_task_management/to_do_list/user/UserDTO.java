package com.reactive_task_management.to_do_list.user;

record UserDTO(String id,
               String username) {

    static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }
}
