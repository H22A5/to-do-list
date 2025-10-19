package com.reactive_task_management.to_do_list.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Document("users")
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@Builder
@Getter
class User {

    @Id
    private String id;

    private String username;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
