package io.stock.kr.calculator.stock.meta.crawling.stockmetaxmlreaderservice;

import io.stock.kr.calculator.stock.meta.crawling.StockMetaXmlReaderService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectKrStockListTest {
    Logger logger = LoggerFactory.getLogger(SelectKrStockListTest.class);

    record CompanyMeta(String ticker, String companyName, String vendorCode){}

    @Test
    public void TEST_READ_FILE_USE_DOM_BUILDER(){
        StockMetaXmlReaderService service = new StockMetaXmlReaderService();
        List<StockMetaDto> stockMetaList = service.selectKrStockList("CORPCODE-TEST.xml");
        assertThat(stockMetaList).isNotEmpty();
        logger.info(stockMetaList.toString());
    }

    @Test
    public void TEST_USING_STAX_API(){
        String corpCodeFile = "CORPCODE-TEST.xml";
        try {
            String rootDir = Paths.get(ClassLoader.getSystemResource("dart").toURI()).toAbsolutePath().toString();
            String fileName = "CORPCODE-TEST.xml";
            Path path = Paths.get(rootDir, fileName);

            if(Files.exists(path)){
                try (InputStream inputStream = Files.newInputStream(path)){
                    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

                    while(reader.hasNext()){
                        reader.next();
                        int eventType = reader.getEventType();
                        if (eventType == XMLStreamConstants.START_ELEMENT){
//                            String elementText = reader.getElementText();
                            System.out.println(reader.getAttributeCount());
                            // 잠시 스킵

                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
