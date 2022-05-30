package io.stock.kr.calculator.common.response;

import io.stock.kr.calculator.common.exception.type.BaseExceptionType;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Data
@ToString
public class CommonResponse<T>{
    private HttpHeaders httpHeaders;
    private int resultCode;
    private T body;
    private String description;

    public CommonResponse(){}

    private CommonResponse(InternalBuilder<T> builder){
        this.resultCode = builder.resultCode;
        this.body = builder.body;
        this.httpHeaders = builder.httpHeaders;
        this.description = builder.description;
    }

    public static <T> CommonResponse<T> of(T body){
        return Optional.ofNullable(body)
                .map(CommonResponse::ok)
                .orElseGet(() -> (CommonResponse<T>) notOk());
    }

    public static <T> CommonResponse<T> of(T body, ResponseType responseType){
        return new InternalBuilder<T>()
                .resultCode(responseType.getResultCode())
                .body(body)
                .build();
    }

    public static <T> InternalBuilder<T> ok(){
        return new InternalBuilder<T>()
                .resultCode(ResponseType.OK.getResultCode());
    }

    public static <T> CommonResponse<T> ok(@Nullable T body){
        return new InternalBuilder<T>()
                .resultCode(ResponseType.OK.getResultCode())
                .body(body)
                .build();
    }

    public static <T> CommonResponse<T> ok(ResponseType responseType, @Nullable T body){
        return new InternalBuilder<T>()
                .resultCode(responseType.getResultCode())
                .body(body)
                .build();
    }

    public static <T> CommonResponse<T> notOk(BaseExceptionType exceptionType, T body){
        return new InternalBuilder<T>()
                .resultCode(exceptionType.getResultCode())
                .description(exceptionType.getDescription())
                .body(body)
                .build();
    }

    public static CommonResponse<String> notOk(BaseExceptionType exceptionType){
        return new InternalBuilder<String>()
                .resultCode(exceptionType.getResultCode())
                .description(exceptionType.getDescription())
                .body(exceptionType.getDescription())
                .build();
    }

    public static CommonResponse<?> notOk(){
        return new InternalBuilder<>()
                .resultCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public static class InternalBuilder <T>{
        private final HttpHeaders httpHeaders = new HttpHeaders();
        private int resultCode;
        private T body;
        private String description;

        public InternalBuilder(){}

        public InternalBuilder<T> resultCode(int resultCode){
            this.resultCode = resultCode;
            return this;
        }

        // 개별 header 는 빌더 조합 메서드를 만들어야 하는데, 아직은 여건상 시간이 부족하기에, header 관련 부분은 패스.
        public InternalBuilder<T> headers(@Nullable HttpHeaders headers){
            if(headers != null){
                this.httpHeaders.putAll(headers);
            }
            return this;
        }

        public InternalBuilder<T> body(@Nullable T body){
            this.body = body;
            return this;
        }

        public InternalBuilder<T> description(@Nullable String description){
            this.description = description;
            return this;
        }

        public CommonResponse<T> build(){
            return new CommonResponse<>(this);
        }
    }

}
