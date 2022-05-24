package io.stock.kr.calculator.stock.price.repository.dynamo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface PriceDayRepository extends CrudRepository<PriceDayDocument, PriceDayDocumentId> {
    List<PriceDayDocument> findAll();
}
