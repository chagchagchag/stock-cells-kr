package io.stock.evaluation.reactive_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

//@SpringBootApplication
// 아래 설정은 임시. DynamoDB 관련 설정이 마무리 되면 아래의 설정은 제거 예정
@SpringBootApplication(exclude = R2dbcAutoConfiguration.class)
public class ReactiveDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveDataApplication.class, args);
	}

}
