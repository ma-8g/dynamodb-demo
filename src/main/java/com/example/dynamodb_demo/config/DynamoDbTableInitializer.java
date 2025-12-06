package com.example.dynamodb_demo.config;

import com.example.dynamodb_demo.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

/**
 * DynamoDBテーブルの初期化クラス
 * アプリケーション起動時にテーブルが存在しない場合、自動的に作成します
 */
@Component
public class DynamoDbTableInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbTableInitializer.class);
    private static final String TABLE_NAME = "User";

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbTableInitializer(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                    DynamoDbClient dynamoDbClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostConstruct
    public void initializeTables() {
        createUserTableIfNotExists();
    }

    /**
     * Userテーブルが存在しない場合、作成します
     */
    private void createUserTableIfNotExists() {
        try {
            // テーブルが既に存在するか確認
            DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .build();

            try {
                dynamoDbClient.describeTable(describeTableRequest);
                logger.info("テーブル '{}' は既に存在します", TABLE_NAME);
                return;
            } catch (ResourceNotFoundException e) {
                // テーブルが存在しない場合は作成
                logger.info("テーブル '{}' が見つかりません。作成します...", TABLE_NAME);
            }

            // テーブルスキーマを取得
            DynamoDbTable<User> userTable = dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(User.class));

            // テーブル作成リクエスト
            CreateTableEnhancedRequest createTableRequest = CreateTableEnhancedRequest.builder()
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            // テーブルを作成
            userTable.createTable(createTableRequest);

            // テーブルがアクティブになるまで待機
            waitForTableToBecomeActive(TABLE_NAME);

            logger.info("テーブル '{}' の作成が完了しました", TABLE_NAME);

        } catch (Exception e) {
            logger.error("テーブル '{}' の作成中にエラーが発生しました: {}", TABLE_NAME, e.getMessage(), e);
        }
    }

    /**
     * テーブルがアクティブになるまで待機します
     */
    private void waitForTableToBecomeActive(String tableName) {
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                DescribeTableResponse response = dynamoDbClient.describeTable(
                        DescribeTableRequest.builder().tableName(tableName).build());

                if (TableStatus.ACTIVE.equals(response.table().tableStatus())) {
                    logger.info("テーブル '{}' がアクティブになりました", tableName);
                    return;
                }

                Thread.sleep(1000); // 1秒待機
                attempt++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("テーブルの待機中に割り込みが発生しました", e);
                return;
            } catch (Exception e) {
                logger.error("テーブルの状態確認中にエラーが発生しました", e);
                return;
            }
        }

        logger.warn("テーブル '{}' がアクティブになるまでに時間がかかりすぎています", tableName);
    }
}

