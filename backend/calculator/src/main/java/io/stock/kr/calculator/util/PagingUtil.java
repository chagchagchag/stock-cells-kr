package io.stock.kr.calculator.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class PagingUtil {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageUnit{
        int limit;
        int totalPageSize;
    }

    public static PageUnit pageUnit(List<?> list, int N){
        int pageCnt = N;   // 리스트의 사이즈를 2개 또는 3개의 구간으로 나누기를 원한다.
        int limit = list.size() / pageCnt;              // 한번에 읽어들일 페이지가 가진 데이터의 갯수
        int totalPageSize = list.size() % limit == 0 ? list.size()/limit : list.size()/limit +1;

        System.out.println("pageCnt = " + pageCnt + ", limit = " + limit + ", totalPageSize = " + totalPageSize);
        return new PageUnit(limit, totalPageSize);
    }

    public static void iterate(List<?> list, PageUnit pageUnit){
        for(int o = 0; o<pageUnit.totalPageSize; o++){
            if(pageUnit.limit*(o+1) >= list.size()) {
                System.out.println(list.subList(pageUnit.limit*o, list.size()));
                break;
            }
            System.out.println(list.subList(pageUnit.limit*o, pageUnit.limit*(o+1)));
        }
    }

    public static <T> void iterate(PageUnit pageUnit, List<T> list, Consumer<List<T>> consumer){
        IntStream.range(0, pageUnit.totalPageSize)
                .forEach(o -> {
                    if(pageUnit.limit*(o+1) >= list.size()) {
                        consumer.accept(list.subList(pageUnit.limit*o, list.size()));
                        return;
                    }
                    consumer.accept(list.subList(pageUnit.limit*o, pageUnit.limit*(o+1)));
                });
    }
}
