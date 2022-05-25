package io.stock.kr.calculator.request.fsc;

import lombok.Getter;

@Getter
public enum FSCParameters {
    NUM_OF_ROWS("numOfRows"),
    PAGE_NO("pageNo"),
    RESULT_TYPE("resultType"),
    BEGIN_BAS_DT("beginBasDt"),
    END_BAS_DT("endBasDt"),
    LIKE_SRTN_CD("likeSrtnCd"),
    SERVICE_KEY("serviceKey");

    private final String parameterName;
    FSCParameters(String parameterName){
        this.parameterName = parameterName;
    }
}
