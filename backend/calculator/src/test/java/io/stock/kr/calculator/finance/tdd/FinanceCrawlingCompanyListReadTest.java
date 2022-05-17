package io.stock.kr.calculator.finance.tdd;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceCrawlingCompanyListReadTest {
    Logger logger = LoggerFactory.getLogger(FinanceCrawlingCompanyListReadTest.class);

    record CompanyMeta(String ticker, String companyName, String vendorCode){}

    Function<Node, String> getTextContent = node -> {
        return Optional
                .ofNullable(node.getTextContent())
                .orElse("");
    };

    @Test
    public void TEST_READ_FILE_USE_DOMBUILDER(){
        String corpCodeFile = "CORPCODE-TEST.xml";
        try {
            String rootDir = Paths.get(ClassLoader.getSystemResource("dart").toURI()).toAbsolutePath().toString();
            Path targetFilePath = Paths.get(rootDir, corpCodeFile);

            if(Files.exists(targetFilePath)){
                try(InputStream inputStream = Files.newInputStream(targetFilePath)){
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document document = builder.parse(inputStream);
                    NodeList list = document.getElementsByTagName("list");

                    List<CompanyMeta> data = IntStream.range(0, list.getLength())
                            .mapToObj(i -> list.item(i))
                            .filter(listElement -> listElement.getNodeType() == Node.ELEMENT_NODE)
                            .filter(listElement -> StringUtils.hasText(listElement.getChildNodes().item(5).getTextContent()))
                            .map(listElement -> {

                                NodeList childNodes = listElement.getChildNodes();
                                Node corpCode = childNodes.item(1);
                                Node corpName = childNodes.item(3);
                                Node stockCode = childNodes.item(5);

                                return new CompanyMeta(getTextContent.apply(stockCode), getTextContent.apply(corpName), getTextContent.apply(corpCode));
                            })
                            .collect(Collectors.toList());

                    logger.info(data.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
