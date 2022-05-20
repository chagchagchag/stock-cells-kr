package io.stock.kr.calculator.stock.price;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StockPriceDto implements Serializable {
    private String basDt;
    private String srtnCd;
    private String isinCd;
    private String itmsNm;
    private String mrktCtg;
    private String vs;

    private BigDecimal mkp;   // open
    private BigDecimal hipr;  // high
    private BigDecimal lopr;  // low
    private BigDecimal clpr;  // close

    public StockPriceDto(){}

    @Builder
    public StockPriceDto(
            String basDt, String srtnCd, String isinCd, String itmsNm, String mrktCtg,
            String vs, BigDecimal mkp, BigDecimal hipr, BigDecimal lopr, BigDecimal clpr
    ){
        this.basDt = basDt;
        this.srtnCd = srtnCd;
        this.isinCd = isinCd;
        this.itmsNm = itmsNm;
        this.mrktCtg = mrktCtg;
        this.vs = vs;
        this.mkp = mkp;
        this.hipr = hipr;
        this.lopr = lopr;
        this.clpr = clpr;
    }
}
