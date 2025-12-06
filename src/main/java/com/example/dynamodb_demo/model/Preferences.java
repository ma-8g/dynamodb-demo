package com.example.dynamodb_demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {
    private String theme;
    private String language;
    private Boolean notificationsEnabled;
    private Integer itemsPerPage;
}

