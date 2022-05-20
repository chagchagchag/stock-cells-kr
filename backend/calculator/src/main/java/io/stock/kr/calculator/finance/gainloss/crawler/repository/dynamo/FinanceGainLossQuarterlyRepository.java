package io.stock.kr.calculator.finance.gainloss.crawler.repository.dynamo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface FinanceGainLossQuarterlyRepository extends CrudRepository<FinanceGainLossQuarterlyDocument, String> {
    public List<FinanceGainLossQuarterlyDocument> findAllByTicker(String ticker);
    public List<FinanceGainLossQuarterlyDocument> findAll();
}
