package com.damselbeing.jokesbot.repository;

import com.damselbeing.jokesbot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
}
