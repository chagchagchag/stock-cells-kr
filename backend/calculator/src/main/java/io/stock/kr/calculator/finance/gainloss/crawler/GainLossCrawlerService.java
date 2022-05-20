package io.stock.kr.calculator.finance.gainloss.crawler;

import io.stock.kr.calculator.finance.gainloss.crawler.dto.GainLossColumn;
import io.stock.kr.calculator.finance.gainloss.crawler.dto.GainLossPeriodType;
import io.stock.kr.calculator.finance.gainloss.crawler.dto.GainLossPeriods;
import io.stock.kr.calculator.finance.gainloss.crawler.dto.GainLossValue;
import io.stock.kr.calculator.request.crawling.html.ElementSelectorPair;
import io.stock.kr.calculator.request.crawling.html.ElementType;
import io.stock.kr.calculator.request.crawling.html.SelectorType;
import io.stock.kr.calculator.request.crawling.html.SpecifierType;
import io.stock.kr.calculator.request.fnguide.FnGuidePageParam;
import io.stock.kr.calculator.request.fnguide.ParameterPair;
import io.stock.kr.calculator.request.fnguide.ParameterType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GainLossCrawlerService {
    final String baseURL                = "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp";
    final String tablesDivClassSelector = "ul_col2wrap pd_t25";
    final String tableSelector          = "ul_co1_c pd_t1";

    public static final String TABLE_DIV_CLASS_SELECTOR 	= "ul_col2wrap pd_t25";
    public static final String TABLE_SELECTOR 			    = "ul_co1_c pd_t1";

    final String yearlyGainLossTableSelector        = "divSonikY";
    final String quarterlyGainLossTableSelector     = "divSonikQ";

    Function<String, BigDecimal> commaStringToDecimal = str -> {
        try {
            Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
            BigDecimal parsedValue = new BigDecimal(parse.toString());
            return parsedValue;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    };

    // 연간 조회
    public List<GainLossValue> findGainLossDataYearly(String companyCode){
        List<GainLossValue> list = new ArrayList<>();

        getDocument(newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, newGainLossPageParameters(companyCode))).ifPresent(document -> {
            findGainLossSection(document).ifPresent(divGainLossSectionElement -> {
                findYearlyTableElement(divGainLossSectionElement).ifPresent(yearlyTableElement -> {

                    // 1) 연도 파싱
                    findYearsList(yearlyTableElement).ifPresent(yearsData-> {
                        System.out.println(yearsData);

                        // 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
                        List<GainLossValue> yearlyGainLossValues = findGainLossValues(yearlyTableElement);
                        System.out.println(yearlyGainLossValues);
                        list.addAll(yearlyGainLossValues);
                    });
                });
            });
        });

        return list;
    }

    // 분기별 조회
    public List<GainLossValue> findGainLossDataQuarterly(String companyCode){
        List<GainLossValue> list = new ArrayList<>();

        getDocument(newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, newGainLossPageParameters(companyCode))).ifPresent(document -> {
            findGainLossSection(document).ifPresent(divGainLossSectionElement -> {
                findQuarterlyTableElement(divGainLossSectionElement).ifPresent(quarterlyTableElement -> {

                    // 1) 기간 파싱
                    findYearsList(quarterlyTableElement).ifPresent(yearsData-> {
                        System.out.println(yearsData);

                        // 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
                        List<GainLossValue> gainLossQuarterValues = findGainLossValues(quarterlyTableElement);
                        System.out.println(gainLossQuarterValues);
                        list.addAll(gainLossQuarterValues);
                    });
                });
            });
        });

        return list;
    }

    // 1) 연도 파싱
    public Optional<GainLossPeriods> findYearsList(Elements yearlyTableElement){
        Stream<Element> limit = yearlyTableElement.select("tr").tagName("tr").stream().limit(1);
        Optional<GainLossPeriods> gainLossPeriods = yearlyTableElement
                .select("tr").tagName("tr")
                .stream().findFirst()
                .map(thtd -> {
                    List<String> data = thtd
                            .select("th").tagName("th").eachText()
                            .stream().skip(1).limit(4)
                            .collect(Collectors.toList());

                    GainLossPeriods periods = GainLossPeriods.builder()
                            .firstPrev(data.get(GainLossPeriodType.FIRST_PREV.getIndexAs()))
                            .secondPrev(data.get(GainLossPeriodType.SECOND_PREV.getIndexAs()))
                            .thirdPrev(data.get(GainLossPeriodType.THIRD_PREV.getIndexAs()))
                            .fourthPrev(data.get(GainLossPeriodType.FOURTH_PREV.getIndexAs()))
                            .build();

                    return periods;
                });

        return gainLossPeriods;
    }

    List<GainLossValue> findGainLossValues(Elements tableElement){
        return tableElement.select("tr").tagName("tr")
                .stream().skip(1)
                .filter(thtd -> {
                    if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
                    if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
                    if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
                    else return false;
                })
                .map(thtd->{
                    String label = thtd.tagName("th").select("div").text(); // 매출액, 영업이익, 당기순이익
                    String value = thtd.tagName("td").select(".r").text();
                    GainLossColumn gainLossColumn = GainLossColumn.krTypeOf(label);
                    List<String> strings = Arrays.asList(value.split(" ")).subList(0, 4);
                    System.out.println(label + " = " + strings);

//					GainLossPeriodType.FIRST_PREV.getIndexAs()
                    BigDecimal firstPeriodValue = commaStringToDecimal.apply(strings.get(GainLossPeriodType.FIRST_PREV.getIndexAs()));
                    BigDecimal secondPeriodValue = commaStringToDecimal.apply(strings.get(GainLossPeriodType.SECOND_PREV.getIndexAs()));
                    BigDecimal thirdPeriodValue = commaStringToDecimal.apply(strings.get(GainLossPeriodType.THIRD_PREV.getIndexAs()));
                    BigDecimal fourthPeriodValue = commaStringToDecimal.apply(strings.get(GainLossPeriodType.FOURTH_PREV.getIndexAs()));

                    return new GainLossValue(gainLossColumn, firstPeriodValue, secondPeriodValue, thirdPeriodValue, fourthPeriodValue);
                })
                .collect(Collectors.toList());
    }

    public Optional<Document> getDocument(String url){
        try {
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public String newFnGuideUrl(FnGuidePageParam.PageType pageType, List<ParameterPair> parameterPairs){
        return FnGuidePageParam.builder()
                .pageType(FnGuidePageParam.PageType.FINANCE)
                .parameterPairs(parameterPairs)
                .build()
                .buildUrl();
    }

    Optional<Elements> findGainLossSection(Document document){
        ElementSelectorPair first = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, TABLE_DIV_CLASS_SELECTOR);
        ElementSelectorPair second = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, TABLE_SELECTOR);

        Elements divGainLossSection = document
                .select(first.ofSelector().toString())
                .select(second.ofSelector().toString());

        return Optional.ofNullable(divGainLossSection);
    }

    public Optional<Elements> findYearlyTableElement(Elements divGainLossSection){
        // 포괄 손익 계산 DIV
        ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, yearlyGainLossTableSelector);
        // 손익 계산 내에서 table 을 파싱
        ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

        Elements yearlyTableElement = divGainLossSection
                .select(yearlyGainLossDiv.ofSelector().toString())
                .select(yearlyGainLossTable.ofSelector().toString());

        return Optional.ofNullable(yearlyTableElement);
    }

    public Optional<Elements> findQuarterlyTableElement(Elements divGainLossSection){
        // 포괄 손익 계산 DIV
        ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, quarterlyGainLossTableSelector);
        // 손익 계산 내에서 table 을 파싱
        ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

        Elements yearlyTableElement = divGainLossSection
                .select(yearlyGainLossDiv.ofSelector().toString())
                .select(yearlyGainLossTable.ofSelector().toString());

        return Optional.ofNullable(yearlyTableElement);
    }

    public ElementSelectorPair ofElementSelectorPair(ElementType elementType, SelectorType selectorType, SpecifierType specifierType, String specifier){
        Objects.requireNonNull(elementType);
        Objects.requireNonNull(selectorType);
        Objects.requireNonNull(specifierType);

        return ElementSelectorPair.builder()
                .elementType(elementType)
                .selectorType(selectorType)
                .specifierType(specifierType)
                .specifier(specifier)
                .build();
    }

    public List<ParameterPair> newGainLossPageParameters(String companyCode){
        ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
        ParameterPair gicode = new ParameterPair(ParameterType.gicode, companyCode);
        return List.of(pGb, gicode);
    }
}
