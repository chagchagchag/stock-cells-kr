package io.stock.evaluation.reactive_data.crawling.price;

import io.stock.evaluation.reactive_data.crawling.types.NaverFinanceParameterType;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NaverFinanceCrawlingTest {

//    public Mono<Document>
    public Mono<Document> getDocument(String url){
        try{
            return Mono.just(Jsoup.connect(url).get());
        } catch (Exception e){
            e.printStackTrace();
        }
        return Mono.empty();
    }

    @Test
    public void URL_TEST(){
        String url = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");
        assertThat(url).isEqualTo("https://finance.naver.com/item/main.naver?code=005930");
    }

    @Test
    public void DOCUMENT_TEST(){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");

        Mono<Elements> element = getDocument(targetUrl)
                .map(document -> {
                    String selector = "em[id='_per']";
                    return document.select(selector);
                })
                .log();

        StepVerifier.create(element)
                .expectNextMatches(el -> el.attr("id").equals("_per"))
                .verifyComplete();

//        StepVerifier.create()
    }

    @Test
    public void DOCUMENT_TEST_USING_FLATMAP(){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");
        final CrawlingData.CrawlingDataBuilder dataBuilder = new CrawlingData.CrawlingDataBuilder();

        Flux<Elements> eachSelectJobs = getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                document.select("em[id='_per']"),
                                document.select("em[id='_eps']"),
                                document.select("em[id='_pbr']"),
                                document.select("em[id='_dvr']"),
                                document.select("em[id='_market_sum']")
                        )
                );

        eachSelectJobs.subscribe(elements -> {
            String type = elements.attr("id").substring(1);
            StringBuilder builder = new StringBuilder().append("type = ").append(type).append(", ").append("text = ").append(elements.text());
            System.out.println(builder.toString());
        });
    }

    @Test
    public void DOCUMENT_TEST_USING_FLATMAP_USING_BUILDER(){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");

        // TODO
        // Builder 내에서 CrawlingDataBuilder 를 받아서 타입별로 조합하게끔 구조를 바꿔보는 시도를 해볼것!!
        final CrawlingData.CrawlingDataBuilder dataBuilder = new CrawlingData.CrawlingDataBuilder();

        getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                document.select("em[id='_per']"),
                                document.select("em[id='_eps']"),
                                document.select("em[id='_pbr']"),
                                document.select("em[id='_dvr']"),
                                document.select("em[id='_market_sum']")
                        )
                )
                .map(elements -> {
                    String type = elements.attr("id").substring(1);
                    String value = elements.text();
                    System.out.println("type = " + type);
                    CrawlingDataType.typeOf(type).bindParameter(dataBuilder, value);
                    return elements;
                })
                .blockLast();

        CrawlingData data = dataBuilder.build();
        System.out.println(data);

        assertThat(data.getPer()).isNotEmpty();
        assertThat(data.getPbr()).isNotEmpty();
        assertThat(data.getEps()).isNotEmpty();
        assertThat(data.getMarketSum()).isNotEmpty();
    }

    @Getter
    enum CrawlingDataType {
        PER("per"){
            @Override
            public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
                builder.per(value);
            }
        },
        EPS("eps"){
            @Override
            public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
                builder.eps(value);
            }
        },
        PBR("pbr"){
            @Override
            public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
                builder.pbr(value);
            }
        },
        DVR("dvr"){
            @Override
            public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
                builder.dvr(value);
            }
        },
        MARKET_SUM("market_sum"){
            @Override
            public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
                builder.marketSum(value);
            }
        };

        private final String dataName;

        CrawlingDataType(String dataName){
            this.dataName = dataName;
        }

        public abstract void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value);

        private static final Map<String, CrawlingDataType> typeMap = new HashMap<>();

        static{
            for(CrawlingDataType type : CrawlingDataType.values()){
                typeMap.putIfAbsent(type.getDataName(), type);
                typeMap.putIfAbsent(type.getDataName().toUpperCase(), type);
            }
        }

        public static CrawlingDataType typeOf(String dataName){
            return typeMap.get(dataName);
        }
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

}
