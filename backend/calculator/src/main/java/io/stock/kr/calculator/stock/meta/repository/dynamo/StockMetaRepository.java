package io.stock.kr.calculator.stock.meta.repository.dynamo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface StockMetaRepository extends CrudRepository<StockMetaDocument, String> {
    List<StockMetaDocument> findAll();
}
