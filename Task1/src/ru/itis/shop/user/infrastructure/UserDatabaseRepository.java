package ru.itis.shop.user.infrastructure;

import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

public class UserDatabaseRepository implements UserRepository {
    @Override
    public void save(User user){
        System.out.println("Saving user: " + user);
    }

    @Override
    public User findById(String id){
        System.out.println("Finding user: " + id);
        return null;
    }
}
