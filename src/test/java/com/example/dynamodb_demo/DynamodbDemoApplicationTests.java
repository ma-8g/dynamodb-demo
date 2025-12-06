package com.example.dynamodb_demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.cloud.aws.dynamodb.endpoint=http://localhost:8000",
	"spring.cloud.aws.credentials.access-key=dummy",
	"spring.cloud.aws.credentials.secret-key=dummy",
	"spring.cloud.aws.region.static=us-east-1"
})
class DynamodbDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
