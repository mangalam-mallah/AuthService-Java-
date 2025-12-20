package org.example.repository;

import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserInfo, String> {
    public UserInfo findByUsername(String username);
}
