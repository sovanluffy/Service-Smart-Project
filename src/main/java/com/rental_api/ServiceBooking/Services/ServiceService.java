package com.rental_api.ServiceBooking.Services;

import com.rental_api.ServiceBooking.Dto.Request.ServiceRequest;
import com.rental_api.ServiceBooking.Dto.Response.ServiceResponse;

public interface ServiceService {

    ServiceResponse createService(Long userId, ServiceRequest request);

}
