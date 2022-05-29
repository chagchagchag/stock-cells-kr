package io.stock.kr.calculator.stock.price;

import io.stock.kr.calculator.common.response.CommonResponse;
import io.stock.kr.calculator.common.response.ResponseType;
import io.stock.kr.calculator.stock.price.response.StockPriceItem;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * for test.
 * 데이터베이스 관련 사항이 결정되기 전 까지는 모의데이터가 필요하기에 개설
 */
@RestController
public class FakeResponseController {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public FakeResponseController(){}

    @GetMapping(value = "/fake/price/{from}/{to}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CommonResponse<Flux<StockPriceItem>>> getStockPriceList(@PathVariable String from, @PathVariable String to){
        // Response 타입 정의할것
        if(Optional.ofNullable(from).isEmpty()) return Mono.just(CommonResponse.notOk(ResponseType.DATE_NULL, Flux.empty()));
        if(Optional.ofNullable(to).isEmpty()) return Mono.just(CommonResponse.notOk(ResponseType.DATE_NULL, Flux.empty()));

        try{
            LocalDate startDt = LocalDate.parse(from, formatter);
            LocalDate endDt = LocalDate.parse(to, formatter);
        }
        catch (Exception e){
            e.printStackTrace();
//            return Flux.just("");
        }

        return null;
//        return Flux.just("안녕하세여", "잠깐 임시로 테스트할께요", "반갑습니다.");
    }
}
