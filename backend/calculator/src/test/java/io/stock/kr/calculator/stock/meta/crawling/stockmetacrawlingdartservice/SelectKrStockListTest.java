package io.stock.kr.calculator.stock.meta.crawling.stockmetacrawlingdartservice;

import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectKrStockListTest {
    Logger logger = LoggerFactory.getLogger(SelectKrStockListTest.class);

    record CompanyMeta(String ticker, String companyName, String vendorCode){}

    @Test
    public void 정상적인_종목리스트_XML파일을_올바르게_파싱하는지_검증한다(){
        StockMetaCrawlingDartService service = new StockMetaCrawlingDartService(null);
        List<StockMetaDto> stockMetaList = service.selectKrStockList("CORPCODE-TEST.xml");
        assertThat(stockMetaList).isNotEmpty();
        logger.info(stockMetaList.toString());
    }

}
