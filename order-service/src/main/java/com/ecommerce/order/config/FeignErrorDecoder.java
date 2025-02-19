package com.ecommerce.order.config;

import com.ecommerce.order.exception.CustomerNotFoundException;
import com.ecommerce.order.exception.ProductValidationException;
import com.ecommerce.order.exception.ServiceCommunicationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.status());

        return switch (statusCode) {
            case NOT_FOUND -> new CustomerNotFoundException("Customer not found");
            case BAD_REQUEST -> new ProductValidationException("Invalid product data");
            default -> new ServiceCommunicationException(
                    "Error communicating with service: " + response.reason()
            );
        };
    }
}