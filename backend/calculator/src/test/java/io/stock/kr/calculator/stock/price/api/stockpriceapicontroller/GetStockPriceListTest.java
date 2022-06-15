package io.stock.kr.calculator.stock.price.api.stockpriceapicontroller;

import io.stock.kr.calculator.common.exception.handler.GlobalExceptionHandler;
import io.stock.kr.calculator.stock.price.api.StockPriceApiController;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

// -- 이건 추후 이슈해결로 돌려야 할듯
// -- WebFluxTest 와 ControllerAdvice를 함께 테스트하려고 할때 같이 돌리지 못하고
// -- 전체 애플리케이션 컨텍스트를 @SpringBootTest로 불러와야 하는데, 이때 WebTestClient 를 그렇게 제대로 불러오지는 못한다.
// -- (2) test with ControllerAdvice
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureWebTestClient

// -- (1) not using ControllerAdvice
//@ActiveProfiles("test-docker")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = StockPriceApiController.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class, StockPriceApiController.class})
public class GetStockPriceListTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("from, to 가 비어있지 않고, 정상적인 파라미터일 경우")
    public void TEST_IF_THE_DATES_FROM_TO_ARE_NOT_NULL_AND_NORMAL(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        webTestClient.get()
//                .uri("/api/v1/stock/price/list")
                .uri(uriBuilder -> {
                    return uriBuilder
                            .path("/api/v1/stock/price/list")
                            .queryParam("from", "20220601")
                            .queryParam("to", "20220602")
                            .build();
                })
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("from 이 비어있는 파라미터일 경우")
    public void TEST_IF_DATES_FROM_IS_EMPTY(){
        webTestClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/api/v1/stock/price/list")
                            .queryParam("to", "20220602")
                            .build();
                })
                .header(HttpHeaders.ACCEPT, "application/json")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
//                .expectStatus().isOk();
                .expectStatus().is4xxClientError()
                .expectBody().consumeWith(System.out::println);
    }

}
