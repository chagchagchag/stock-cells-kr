package io.stock.kr.calculator.stock.price.repository.dynamo;

import org.springframework.data.repository.CrudRepository;

public interface PriceDayRepository extends CrudRepository<PriceDayDocument, String> {
}
