package io.stock.kr.calculator.stock.price.api;

import io.stock.kr.calculator.common.date.DateFormatType;
import io.stock.kr.calculator.common.exception.DateFormatIllegalException;
import io.stock.kr.calculator.common.exception.IllegalDateArgumentException;
import io.stock.kr.calculator.common.exception.type.DateExceptionType;
import io.stock.kr.calculator.common.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class StockPriceApiController {

//    @ExceptionHandler
//    @GetMapping(value = "/api/v1/stock/price/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @GetMapping(value = "/api/v1/stock/price/list")
    public Mono<CommonResponse<Flux<StockPriceResponse>>> getStockPriceList(@RequestParam("from") String from, @RequestParam("to") String to){
        if(Optional.ofNullable(from).isEmpty() || Optional.ofNullable(to).isEmpty())
            throw new IllegalDateArgumentException(DateExceptionType.DATE_NULL);

        if(!StringUtils.hasText(from) || !StringUtils.hasText(to))
            throw new IllegalDateArgumentException(DateExceptionType.DATE_NULL);

        try{
            LocalDate startDate = DateFormatType.yyyyMMdd.parse(from);
            LocalDate endDate = DateFormatType.yyyyMMdd.parse(to);
        }
        catch (Exception e){
            throw new DateFormatIllegalException(DateExceptionType.DATE_FORMAT_INVALID, e);
        }

        return Mono.empty();
    }
}
