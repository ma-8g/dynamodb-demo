## 参考サイト
- [Spring BootからDynamoDBへのアクセス（Spring Cloud AWS 3.0）](https://qiita.com/gigilu/items/fb35fa94a8e18618d7a5)

## DynamoDBテーブルの作成方法

### アプリケーション起動時に自動作成

`DynamoDbTableInitializer`クラスがアプリケーション起動時に自動的にUserテーブルを作成します。

1. Docker ComposeでDynamoDB Localを起動:
```bash
docker-compose up -d
```

2. Spring Bootアプリケーションを起動:
```bash
./gradlew bootRun
```

アプリケーション起動時に、Userテーブルが存在しない場合は自動的に作成されます。

### アプリケーション起動時に自動作成されない場合
#### 方法1: AWS CLIを使用して手動で作成

```bash
aws dynamodb create-table \
    --table-name User \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000 \
    --region ap-northeast-1
```

#### 方法2: DynamoDB Admin UIを使用

1. Docker ComposeでDynamoDB Adminを起動:
```bash
docker-compose up -d
```

2. ブラウザで http://localhost:8001 にアクセス
3. UIからテーブルを作成

## DynamoDBテーブルへのデータ登録方法
### 以下の JSON を POSTMAN で1件ずつ登録する。（配列のまま複数同時登録は不可）

```
[
  {
    "id": "user001",
    "name": "山田太郎",
    "email": "yamada@example.com",
    "age": 28
  },
  {
    "id": "user002",
    "name": "佐藤花子",
    "email": "sato@example.com",
    "age": 32
  },
  {
    "id": "user003",
    "name": "鈴木一郎",
    "email": "suzuki@example.com",
    "age": 25
  }
]
```
