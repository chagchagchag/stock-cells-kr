package io.stock.kr.calculator.stock.price.crawling;

import io.stock.kr.calculator.request.fsc.FSCAPIType;
import io.stock.kr.calculator.request.fsc.FSCParameters;
import io.stock.kr.calculator.stock.price.crawling.dto.FSCStockPriceResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 금융결제원 API 요청 객체
 */
@Slf4j
public class FSCAPIRequestProcessor {
    private final FSCAPIType fscapiType;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Builder
    public FSCAPIRequestProcessor(
            FSCAPIType fscapiType
    ){
        this.fscapiType = fscapiType;
    }

    public FSCStockPriceResponse requestStockPrice(LocalDate startDate, LocalDate endDate, long offset, long limit){
        return newWebClient(fscapiType.getBaseUrl()).get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .scheme(fscapiType.getScheme())
                            .host(fscapiType.getHost())
                            .path(fscapiType.webClientEndpoint())
                            .queryParam(FSCParameters.NUM_OF_ROWS.getParameterName(), String.valueOf(limit))
                            .queryParam(FSCParameters.PAGE_NO.getParameterName(), String.valueOf(offset))
                            .queryParam(FSCParameters.RESULT_TYPE.getParameterName(), "json")
                            .queryParam(FSCParameters.BEGIN_BAS_DT.getParameterName(), startDate)
                            .queryParam(FSCParameters.END_BAS_DT.getParameterName(), endDate)
                            .queryParam(FSCParameters.SERVICE_KEY.getParameterName(), fscapiType.getEncodedServiceKey())
                            .build();

                    log.info(uri.getHost() + ", " + uri.getPath() + ", " + uri.getRawPath());
                    log.info(uri.toString());
                    return uri;
                })
                .retrieve()
                .bodyToMono(FSCStockPriceResponse.class)
                .block();
    }

    public WebClient newWebClient(String BASE_URL){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(BASE_URL)
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.47")
                .defaultHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .defaultHeader("accept-encoding", "gzip, deflate, br")
                .defaultHeader("accept-language", "ko,en;q=0.9,en-US;q=0.8")
                .build();

        return webClient;
    }

    public String formatLocalDate(LocalDate localDate, DateTimeFormatter formatter){
        return localDate.format(formatter);
    }
}
