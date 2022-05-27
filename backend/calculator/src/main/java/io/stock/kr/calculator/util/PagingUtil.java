package io.stock.kr.calculator.util;

import io.stock.kr.calculator.request.api.data_portal.DataPortalPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class PagingUtil {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageUnit{
        long limit;
        long totalPageSize;
    }

    public static PageUnit pageUnit(List<?> list, int N){
        int pageCnt = N;   // 리스트의 사이즈를 2개 또는 3개의 구간으로 나누기를 원한다.
        int limit = list.size() / pageCnt;              // 한번에 읽어들일 페이지가 가진 데이터의 갯수
        int totalPageSize = list.size() % limit == 0 ? list.size()/limit : list.size()/limit +1;

        log.debug("pageCnt = " + pageCnt + ", limit = " + limit + ", totalPageSize = " + totalPageSize);
        return new PageUnit(limit, totalPageSize);
    }

    public static PageUnit pageUnit(Long totalSize, int N){
        int pageCnt = N;   // 리스트의 사이즈를 2개 또는 3개의 구간으로 나누기를 원한다.
        long limit = totalSize / pageCnt;              // 한번에 읽어들일 페이지가 가진 데이터의 갯수
        long totalPageSize = totalSize % limit == 0 ? totalSize/limit : totalSize/limit +1;

        log.debug("pageCnt = " + pageCnt + ", limit = " + limit + ", totalPageSize = " + totalPageSize);
        return new PageUnit((int)limit, (int)totalPageSize);
    }

    public static void iterateList(List<?> list, PageUnit pageUnit){
        for(int o = 0; o<pageUnit.totalPageSize; o++){
            if(pageUnit.limit*(o+1) >= list.size()) {
                log.debug(list.subList((int)pageUnit.limit*o, list.size()).toString());
                break;
            }
            log.debug(list.subList((int)pageUnit.limit*o, (int)pageUnit.limit*(o+1)).toString());
        }
    }

    public static <T> void iterateList(PageUnit pageUnit, List<T> list, Consumer<List<T>> consumer){
        IntStream.range(0, (int)pageUnit.totalPageSize)
                .forEach(o -> {
                    if(pageUnit.limit*(o+1) >= list.size()) {
                        consumer.accept(list.subList((int)pageUnit.limit*o, list.size()));
                        return;
                    }
                    consumer.accept(list.subList((int)pageUnit.limit*o, (int)pageUnit.limit*(o+1)));
                });
    }

    // 지감 당장 추상화를 더 하기에는 아직은 사소한 단계.
    public static <T> void iterateApiConsumer(long totalPageCnt, long partitionSize, long limit, Consumer<DataPortalPage> consumer){
        LongStream.range(1, totalPageCnt+1)
                .forEach(offset -> {
                    consumer.accept(new DataPortalPage(offset, offset+1));
                });

        if(limit * (totalPageCnt+1) > partitionSize){
            consumer.accept(new DataPortalPage(totalPageCnt+1, partitionSize+2));
        }
    }

}
