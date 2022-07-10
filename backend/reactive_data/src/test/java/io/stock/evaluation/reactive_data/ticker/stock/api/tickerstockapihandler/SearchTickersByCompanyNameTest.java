package io.stock.evaluation.reactive_data.ticker.stock.api.tickerstockapihandler;

import io.stock.evaluation.reactive_data.ticker.stock.api.TickerStockApiHandler;
import io.stock.evaluation.reactive_data.ticker.stock.cache.TickerStockRedisService;
import io.stock.evaluation.reactive_data.ticker.stock.dto.TickerStockDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

@ActiveProfiles("test-docker")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SearchTickersByCompanyNameTest {

    // (start) ===
    //      시간이 많지 않은데 일단은 테스트를 남겨야해서 이번 테스트 케이스에서는
    //      실제 레디스 객체를 주입했는데,
    //      ((TODO) 아래 내용으로 변환해야 함. Mock 객체 기반 레디스로 테스트 전환 필요)
    //      가급적 핸들러테스트에서는 레디스를 Mock 객체로 두어서 레디스가 리턴하는 값을 가정후
    //      가정한 값에 따라서 어떻게 리턴하는지를 테스트하는 케이스로 변경해두어야 한다.
    //      (가급적이면 @SpringBootTest, test-docker 는 제거 필요)

    @Autowired
    ReactiveRedisOperations<String, TickerStockDto> tickerMetaMapOps;

    @Autowired
    @Qualifier("tickerAutoCompleteRedisOperation")
    ReactiveRedisOperations<String, String> tickerMetaAutoCompleteOps;
    // (end) ===

    private final TickerStockRedisService redisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
    private final TickerStockApiHandler handler = new TickerStockApiHandler(redisService);

    @Test
    @DisplayName("검색어_파라미터가_정상적일_경우_200_OK_를_리턴해야_한다")
    public void 검색어_파라미터가_정상적일_경우_200_OK_를_리턴해야_한다(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .queryParam("companyName", "삼성전자")
                .build();

        Mono<ServerResponse> response = handler.searchTickersByCompanyName(request);

        Predicate<ServerResponse> is2xxSuccessful = _response -> _response.statusCode().is2xxSuccessful();

        StepVerifier.create(response)
                .expectNextMatches(is2xxSuccessful)
                .verifyComplete();
    }
    
    @Test
    @DisplayName("검색어_파라미터가_정상적이지_않을경우_4xx를_리턴하는지_검증한다")
    public void 검색어_파라미터가_정상적이지_않을경우_4xx를_리턴하는지_검증한다(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .queryParam("c", "삼성전자")
                .build();

        Mono<ServerResponse> response = handler.searchTickersByCompanyName(request);

        Predicate<ServerResponse> is4xxClientError = _response -> _response.statusCode().is4xxClientError();

        StepVerifier.create(response)
                .expectNextMatches(is4xxClientError)
                .verifyComplete();
    }
}
