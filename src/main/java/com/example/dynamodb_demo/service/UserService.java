package com.example.dynamodb_demo.service;

import com.example.dynamodb_demo.model.User;
import com.example.dynamodb_demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * IDでユーザーを取得
     */
    public User getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * すべてのユーザーを取得
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * ユーザーを保存
     */
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    /**
     * ユーザーを削除
     */
    public void deleteUser(String id) {
        userRepository.delete(id);
    }
}

