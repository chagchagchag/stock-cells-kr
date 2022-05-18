package io.stock.kr.calculator.dynamo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface FinanceGainLossQuarterlyRepository extends CrudRepository<FinanceGainLossQuarterly, String> {
    public List<FinanceGainLossQuarterly> findAllByTicker(String ticker);
    public List<FinanceGainLossQuarterly> findAll();
}
