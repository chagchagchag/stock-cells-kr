package io.stock.kr.calculator.stock.price.fakeresponsecontroller;

import io.stock.kr.calculator.common.exception.type.DateExceptionType;
import io.stock.kr.calculator.common.response.CommonResponse;
import io.stock.kr.calculator.stock.price.api.FakeResponseController;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class GetStockPriceListTest {

    @Test
    public void TEST_비어있는_기간데이터가_오면_NOT_OK_를_리턴해야한다(){
        FakeResponseController controller = new FakeResponseController();
        Mono<CommonResponse<String>> stockPriceList = controller.getStockPriceList(null, null);

        stockPriceList.subscribe(commonResponse -> {
            assertThat(commonResponse.getResultCode()).isEqualTo(DateExceptionType.DATE_NULL.getResultCode());
            assertThat(commonResponse.getDescription()).isEqualTo(DateExceptionType.DATE_NULL.getDescription());

//            Flux<StockPriceItem> body = commonResponse.getBody();
//            StepVerifier.create(body)
//                        .expectNextCount(1);
        });
    }

    @Test
    public void TEST_잘못된_기간데이터가_오면_NOT_OK_를_리턴해야한다(){
        FakeResponseController controller = new FakeResponseController();
        Mono<CommonResponse<String>> stockPriceList = controller.getStockPriceList("2022-11-22", "2022-11-23");

        stockPriceList.subscribe(commonResponse -> {
            assertThat(commonResponse.getResultCode()).isEqualTo(DateExceptionType.DATE_FORMAT_INVALID.getResultCode());
            assertThat(commonResponse.getDescription()).isEqualTo(DateExceptionType.DATE_FORMAT_INVALID.getDescription());

//            Flux<StockPriceItem> body = commonResponse.getBody();
//            StepVerifier.create(body).expectNextCount(1);
        });
    }


}
