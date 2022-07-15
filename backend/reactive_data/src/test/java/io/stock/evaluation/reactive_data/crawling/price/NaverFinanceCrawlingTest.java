package io.stock.evaluation.reactive_data.crawling.price;

import io.stock.evaluation.reactive_data.crawling.stock.price.application.CrawlingValuationService;
import io.stock.evaluation.reactive_data.crawling.stock.price.dto.CrawlingData;
import io.stock.evaluation.reactive_data.crawling.stock.price.dto.Parameter;
import io.stock.evaluation.reactive_data.crawling.stock.price.type.CrawlingDataType;
import io.stock.evaluation.reactive_data.crawling.types.NaverFinanceParameterType;
import lombok.Getter;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class NaverFinanceCrawlingTest {

    private final CrawlingValuationService service = new CrawlingValuationService();

    @Test
    public void URL_TEST(){
        String url = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");
        assertThat(url).isEqualTo("https://finance.naver.com/item/main.naver?code=005930");
    }

    @Test
    public void DOCUMENT_TEST_USING_FLATMAP_USING_BUILDER(){
        String ticker = "005930";
        Mono<CrawlingData> data = service.getPriceBasicValuationData(ticker);

        Predicate<CrawlingData> isNotEmpty = d -> {
            if(d.getPer() == null) return false;
            if(d.getEps() == null) return false;
            if(d.getPbr() == null) return false;
            if(d.getMarketSum() == null) return false;
            return true;
        };

        StepVerifier.create(data)
                .expectNextMatches(isNotEmpty)
                .verifyComplete();
    }

    // 테스트 용도로만 사용하기 위한 POJO 클래스
    @Getter
    class KeyValue{
        private final String key;
        private final String value;

        public KeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }

        public KeyValue(KeyValue keyValue){
            this.key = keyValue.key;
            this.value = keyValue.value;
        }
    }

    // Reactive Java 버전
    @Test
    @DisplayName("key,value 기반의 데이터를 POJO 로 변환 (WebFlux 버전)")
    public void TEST_EACH_KEY_VALUE_TO_POJO_CrawlingData_WebFlux_Way(){
        // given
        KeyValue kv1 = new KeyValue("per", "11111");
        KeyValue kv2 = new KeyValue("pbr", "22222");
        KeyValue kv3 = new KeyValue("eps", "33333");
        KeyValue kv4 = new KeyValue("market_sum", "44444");

        Flux<KeyValue> keyValues = Flux.just(kv1, kv2, kv3, kv4);

        final CrawlingData.CrawlingDataBuilder builder = new CrawlingData.CrawlingDataBuilder();

        keyValues
                .map(kv -> {
                    String key = kv.getKey();
                    String value = kv.getValue();
                    CrawlingDataType.typeOf(key).bindParameter(builder, value);
                    return kv;
                })
                .blockLast();

        CrawlingData data = builder.build();
        System.out.println(data);
    }

    // 일반 자바 버전
    // 키/밸류가 하나씩만 들어올때 하나씩 데이터를 채워가면서 VO를 생성해내는 예제
    @Test
    @DisplayName("key,value 기반의 데이터를 POJO 로 변환 (Java 버전)")
    public void TEST_EACH_KEY_VALUE_TO_POJO_CrawlingData_GeneralJAVA_Way(){
        // given
        KeyValue kv1 = new KeyValue("per", "11111");
        KeyValue kv2 = new KeyValue("pbr", "22222");
        KeyValue kv3 = new KeyValue("eps", "33333");
        KeyValue kv4 = new KeyValue("market_sum", "44444");

        List<KeyValue> list = List.of(kv1, kv2, kv3, kv4);

        final CrawlingData.CrawlingDataBuilder builder = new CrawlingData.CrawlingDataBuilder();
        // {KV kv, CrawlingDataBuilder builder} 이런 형식의 데이터는 없을까?
        // when
        list.stream()
                .forEach(kv -> {
                    String key = kv.getKey();
                    String value = kv.getValue();
                    CrawlingDataType.typeOf(key).bindParameter(builder, value);
                });

        CrawlingData data = builder.build();
        System.out.println(data);
    }

    @Test
    public void 현재가_파싱_테스트_1(){
        final String ticker = "005930";
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl(ticker);

        Mono<String> data = service.getDocument(targetUrl)
                .map(document -> {
                    return document.select("p.no_today .no_up span:not(.shim)").eachText().get(0);
                })
                .map(list -> {
                    System.out.println(" text >> " + list);
                    return list;
                });

        data.subscribe(dat -> System.out.println(dat));
    }

    @Test
    public void 현재가_파싱_테스트_2(){
        Function<Elements, Parameter> priceParser = (elements) -> {
            String type = "price";
            String value = elements.eachText().get(0);
            return new Parameter(type, value);
        };

        final String ticker = "005930";
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl(ticker);

        Mono<Parameter> data = service.getDocument(targetUrl)
                .map(document -> {
                    return document.select("p.no_today .no_up span:not(.shim)");
                })
                .map(elements -> {
                    return priceParser.apply(elements);
                })
                .map(parameter -> {
                    System.out.println(" text >> " + parameter);
                    return parameter;
                });

        data.subscribe(dat -> System.out.println(dat));
    }

    @Test
    public void TEST_TEST(){
        Function<Elements, Parameter> idBasedParser = (elements) -> {
            String type = elements.attr("id").substring(1);
            String value = elements.text();
            return new Parameter(type, value);
        };

        Function<Elements, Parameter> priceParser = (elements) -> {
            String type = "price";
            String value = elements.eachText().get(0);
            return new Parameter(type, value);
        };

        final String ticker = "005930";
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl(ticker);
        final CrawlingData.CrawlingDataBuilder dataBuilder = new CrawlingData.CrawlingDataBuilder();


        Mono<CrawlingData> result = service.getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                idBasedParser.apply(document.select("em[id='_per']")),
                                idBasedParser.apply(document.select("em[id='_eps']")),
                                idBasedParser.apply(document.select("em[id='_pbr']")),
                                idBasedParser.apply(document.select("em[id='_dvr']")),
                                idBasedParser.apply(document.select("em[id='_market_sum']")),
                                priceParser.apply(document.select("p.no_today .no_up span:not(.shim)"))
                        )
                )
                .map(parameter -> {
                    dataBuilder.bindParameter(parameter.getType(), parameter.getValue());
                    return dataBuilder;
                })
                .last()
                .map(builder -> builder.build());


        result.subscribe(d -> System.out.println(d));
    }

}
