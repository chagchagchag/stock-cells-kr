package io.stock.kr.calculator.langtest;

import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SublistAndPagingTest {

    @Test
    public void 리스트의_서브리스팅을_단순히_나열해본다(){
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int size1 = list.size();
        int limit = size1/5;
        int offset = 0;

        System.out.println("size1 = " + size1 + ", " + "limit = " + limit + ", offset = " + offset);

        System.out.println(list.subList(offset++, limit));
        System.out.println(list.subList(limit*offset++,limit*2));
        System.out.println(list.subList(limit*4, limit*5));
    }

    @Test
    public void 리스트를_5개의_부분으로_나누어_테스트(){
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int limit = list.size()/5;
        int offset = 0;

        int totalPageSize = list.size()%2 == 0 ? list.size()/2 : list.size()/2 +1;
        for(int o = 0; o<totalPageSize; o++){
            System.out.println(list.subList(limit*o, limit*(o+1)));
        }
    }

    @Test
    public void 리스트의_페이징_사이즈를_변수화해서_기능분리(){
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int N = 2;
        int pageCnt = list.size() % N == 0 ? N : N+1;   // 리스트의 사이즈를 2개 또는 3개의 구간으로 나누기를 원한다.
        int limit = list.size() / pageCnt;              // 한번에 읽어들일 페이지가 가진 데이터의 갯수
        int totalPageSize = list.size() % limit == 0 ? list.size()/limit : list.size()/limit +1;

        System.out.println("pageCnt = " + pageCnt + ", limit = " + limit + ", totalPageSize = " + totalPageSize);

        for(int o = 0; o<totalPageSize; o++){
            System.out.println(list.subList(limit*o, limit*(o+1)));
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class PageUnit{
        int limit;
        int totalPageSize;
    }

    public PageUnit pageUnit(List<?> list, int N){
        int pageCnt = N;   // 리스트의 사이즈를 2개 또는 3개의 구간으로 나누기를 원한다.
        int limit = list.size() / pageCnt;              // 한번에 읽어들일 페이지가 가진 데이터의 갯수
        int totalPageSize = list.size() % limit == 0 ? list.size()/limit : list.size()/limit +1;

        System.out.println("pageCnt = " + pageCnt + ", limit = " + limit + ", totalPageSize = " + totalPageSize);
        return new PageUnit(limit, totalPageSize);
    }

    public void iterate(List<?> list, PageUnit pageUnit){
        for(int o = 0; o<pageUnit.totalPageSize; o++){
            if(pageUnit.limit*(o+1) >= list.size()) {
                System.out.println(list.subList(pageUnit.limit*o, list.size()));
                break;
            }
            System.out.println(list.subList(pageUnit.limit*o, pageUnit.limit*(o+1)));
        }
    }

    @Test
    public void 리스트의_페이징_사이즈_및_다른기능역시도_변수화해서_기능함수화(){
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        PageUnit pageUnit = pageUnit(list, 3);
        iterate(list, pageUnit);
    }
}
