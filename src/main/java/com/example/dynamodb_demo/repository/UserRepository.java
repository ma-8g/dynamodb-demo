package com.example.dynamodb_demo.repository;

import com.example.dynamodb_demo.model.User;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final DynamoDbTable<User> userTable;

    public UserRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.userTable = dynamoDbEnhancedClient.table("User", TableSchema.fromBean(User.class));
    }

    /**
     * IDでユーザーを取得
     */
    public User findById(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        return userTable.getItem(key);
    }

    /**
     * すべてのユーザーを取得（スキャン）
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        PageIterable<User> results = userTable.scan();
        results.items().forEach(users::add);
        return users;
    }

    /**
     * ユーザーを保存
     */
    public void save(User user) {
        userTable.putItem(user);
    }

    /**
     * ユーザーを削除
     */
    public void delete(String id) {
        Key key = Key.builder()
                .partitionValue(id)
                .build();
        userTable.deleteItem(key);
    }
}

