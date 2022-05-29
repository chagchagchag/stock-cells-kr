package io.stock.kr.calculator.common.response;

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
    private HttpStatus status;
    private T body;
    private String description;

    public CommonResponse(){}

    private CommonResponse(InternalBuilder<T> builder){
        this.status = builder.status;
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
                .status(responseType.getHttpStatusCode())
                .body(body)
                .build();
    }

    public static <T> InternalBuilder<T> ok(){
        return new InternalBuilder<T>()
                .status(HttpStatus.OK);
    }

    public static <T> CommonResponse<T> ok(@Nullable T body){
        return new InternalBuilder<T>()
                .status(HttpStatus.OK)
                .body(body)
                .build();
    }

    public static <T> CommonResponse<T> ok(@Nullable T body, ResponseType responseType){
        return new InternalBuilder<T>()
                .status(responseType.getHttpStatusCode())
                .body(body)
                .build();
    }

    public static <T> CommonResponse<T> notOk(ResponseType responseType, T data){
        return new InternalBuilder<T>()
                .status(responseType.getHttpStatusCode())
                .body(data)
                .build();
    }

    public static CommonResponse<String> notOk(ResponseType responseType){
        return new InternalBuilder<String>()
                .status(responseType.getHttpStatusCode())
                .body(responseType.getDescription())
                .build();
    }

    public static CommonResponse<?> notOk(){
        return new InternalBuilder<>()
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static class InternalBuilder <T>{
        private final HttpHeaders httpHeaders = new HttpHeaders();
        private HttpStatus status;
        private T body;
        private String description;

        public InternalBuilder(){}

        public InternalBuilder<T> status(@Nullable HttpStatus httpStatus){
            this.status = httpStatus;
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
