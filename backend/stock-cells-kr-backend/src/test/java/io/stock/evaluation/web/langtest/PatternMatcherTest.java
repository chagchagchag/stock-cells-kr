package io.stock.evaluation.web.langtest;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcherTest {

    @Test
    public void TEST(){
        Pattern pattern1 = Pattern.compile("§§§$");
//        Pattern pattern = Pattern.compile("§§§$");
        Matcher matcher1 = pattern1.matcher("삼성전자§§§");
        System.out.println(matcher1.matches());

        Pattern pattern2 = Pattern.compile("\\w+§§§");
        Matcher matcher2 = pattern2.matcher("삼성전자§§§");
        System.out.println(matcher2.matches());

        Pattern pattern3 = Pattern.compile(".*\\§§§");
        Matcher matcher3 = pattern3.matcher("삼성전자§§§");
        System.out.println(matcher3.matches());
//        System.out.println("삼성전자§§§".endsWith("§§§"));
//        Assertions.assertThat(matcher.matches()).isTrue();
    }
}
