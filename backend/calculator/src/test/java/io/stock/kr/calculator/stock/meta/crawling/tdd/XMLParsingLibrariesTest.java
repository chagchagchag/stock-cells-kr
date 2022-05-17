package io.stock.kr.calculator.stock.meta.crawling.tdd;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLParsingLibrariesTest {
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
