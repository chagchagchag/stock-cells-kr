package io.stock.kr.calculator.stock.meta.crawling;

import io.stock.kr.calculator.common.types.CrawlingVendorType;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaDocument;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaDynamoDBMapper;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class StockMetaCrawlingDartService {

    Function<Node, String> getTextContent = node -> {
        return Optional
                .ofNullable(node.getTextContent())
                .orElse("");
    };

    private StockMetaDynamoDBMapper stockMetaDynamoDBMapper;
    private StockMetaRepository stockMetaRepository;

    public StockMetaCrawlingDartService(
            StockMetaDynamoDBMapper stockMetaDynamoDBMapper,
            StockMetaRepository stockMetaRepository
    ){
        this.stockMetaDynamoDBMapper = stockMetaDynamoDBMapper;
        this.stockMetaRepository = stockMetaRepository;
    }

    /**
     * @param corpCodeFileName : xml 파일명 (확장자는 .xml 이어야만 하고, 바이너리 파일이 아닌 텍스트파일이어야 한다.)
     * @return List of StockMetaDto
     */
    public List<StockMetaDto> selectKrStockList(String corpCodeFileName){
        List<StockMetaDto> result = Collections.emptyList();

        try {
            String rootDir = Paths.get(ClassLoader.getSystemResource("dart").toURI()).toAbsolutePath().toString();
            Path targetFilePath = Paths.get(rootDir, corpCodeFileName);

            if (Files.exists(targetFilePath)){
                try(InputStream inputStream = Files.newInputStream(targetFilePath)){
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = builder.parse(inputStream);
                    NodeList list = document.getElementsByTagName("list");

                    return IntStream.range(0, list.getLength())
                            .mapToObj(list::item)
                            .filter(listElement -> listElement.getNodeType() == Node.ELEMENT_NODE)
                            .filter(listElement -> StringUtils.hasText(listElement.getChildNodes().item(5).getTextContent()))
                            .map(listElement -> {
                                NodeList childNodes = listElement.getChildNodes();
                                Node corpCode = childNodes.item(1);
                                Node corpName = childNodes.item(3);
                                Node stockCode = childNodes.item(5);

                                return new StockMetaDto(getTextContent.apply(stockCode), getTextContent.apply(corpName), getTextContent.apply(corpCode), CrawlingVendorType.DART);
                            })
                            .collect(Collectors.toList());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void batchWriteStockList(List<StockMetaDto> stockList){
        List<StockMetaDocument> stocks = stockList.stream()
                .map(this::toStockMetaDocument)
                .collect(Collectors.toList());

        stockMetaDynamoDBMapper.batchWriteStockMetaList(stocks);
    }

    public void batchDeleteStockList(List<StockMetaDocument> stockList){
        stockMetaDynamoDBMapper.batchDelete(stockList);
    }

    public void batchDeleteStockListAll(){
        List<StockMetaDocument> stockDocList = stockMetaRepository.findAll();
        stockMetaDynamoDBMapper.batchDelete(stockDocList);
    }

    public StockMetaDocument toStockMetaDocument(StockMetaDto stockMetaDto){
        return StockMetaDocument.builder()
                .ticker(stockMetaDto.getTicker())
                .companyName(stockMetaDto.getCompanyName())
                .vendorCode(stockMetaDto.getVendorCode())
                .vendorType(stockMetaDto.getVendorType())
                .build();
    }
}
