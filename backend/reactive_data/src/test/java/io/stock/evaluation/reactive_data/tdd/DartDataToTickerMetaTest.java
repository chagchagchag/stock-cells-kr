package io.stock.evaluation.reactive_data.tdd;


import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import reactor.core.publisher.Flux;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DartDataToTickerMetaTest {

    public Flux<TickerMetaItem> selectTickerMetaList(String fileName){
        return Flux.empty();
    }

    Function<Node, String> getTextContent = node -> {
        return Optional
                .ofNullable(node.getTextContent())
                .orElse("");
    };

    public Optional<NodeList> parseNodeList(InputStream inputStream){
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(inputStream);
            return Optional.ofNullable(document.getElementsByTagName("list"));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    Function<Node, TickerMetaItem> newTickerItem = (node) -> {
        NodeList childNodes = node.getChildNodes();
        Node corpCode = childNodes.item(1);
        Node corpName = childNodes.item(3);
        Node stockCode = childNodes.item(5);
        return new TickerMetaItem(getTextContent.apply(stockCode), getTextContent.apply(corpName), getTextContent.apply(corpCode));
    };

    Predicate<Node> isValidNode = node -> {
        // ELEMENT_NODE 타입이면서
        if(node.getNodeType() == Node.ELEMENT_NODE){
            // stockCode 가 없는 item 도 있기에 stockCode 가 존재하는 아이템일 경우에만 true 를 리턴
            if(StringUtils.hasText(node.getChildNodes().item(5).getTextContent())){
                return true;
            }
            return false;
        }
        return false;
    };

    @Test
    public void TEST(){
        try {
            String rootDir = Paths.get(ClassLoader.getSystemResource("dart").toURI()).toAbsolutePath().toString();
            Path targetFilePath = Paths.get(rootDir, "CORPCODE.xml");

            if (Files.exists(targetFilePath)){
                // Input Stream open
                try(InputStream inputStream = Files.newInputStream(targetFilePath)){
                    // xml 에서 노드리스트를 떼어온다.
                    Flux<TickerMetaItem> tickerMetaItemFlux = parseNodeList(inputStream)
                        .map(list -> {  // NodeList가 비어있지 않다면, 아래의 작업을 수행, Flux<TickerMetaItem> 을 생성해낸다.
                            return Flux
                                .range(0, list.getLength())          // 0 ~ length 만큼 데이터의 흐름을 만들어서
                                .map(list::item)                            // 각각의 list.item(i) 를 수행하고
                                .filter(node -> isValidNode.test(node))     // ELEMENT_NODE 타입이면서, 비어있지 않은 노드만 선택해서
                                .map(node -> newTickerItem.apply(node));    // node 의 각 요소를 떼어서 TickerMetaItem 을 생성한다.
                        })
                        .orElse(Flux.empty()); // 그 외의 경우는 Flux.empty() 를 리턴한다.

                    tickerMetaItemFlux.hasElements()
                        .subscribe(
                            hasData -> assertThat(hasData).isTrue()
                        );
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
