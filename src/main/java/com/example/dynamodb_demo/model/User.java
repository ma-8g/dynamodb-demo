package com.example.dynamodb_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private Integer age;

    // DynamoDBデータ型のフィールド
    // S — 文字列
    private String phoneNumber;

    // N — 数値
    private Long score;

    // B — バイナリ
    private ByteBuffer profileImage;

    // BOOL — ブール
    private Boolean isActive;

    // NULL — Null（null許容型として表現）
    private String middleName;

    // M — マップ
    private Preferences preferences;

    // L — リスト
    private List<Hobby> hobbies;

    // NS — 数値セット
    private Set<Integer> favoriteNumbers;

    // 更新日（文字列型）
    private String updatedAt;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}

