package com.rental_api.ServiceBooking.Dtos.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String fullname;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String location;

    // List of role IDs to assign to the user
    private List<Long> roleIds;
}
