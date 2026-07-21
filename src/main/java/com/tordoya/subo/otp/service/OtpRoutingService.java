package com.tordoya.subo.otp.service;

import com.tordoya.subo.otp.client.OtpClient;
import com.tordoya.subo.otp.dto.response.OtpPlanResponse;
import com.tordoya.subo.otp.dto.request.OtpRoutingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OtpRoutingService {
    private final OtpClient otpClient;

    public OtpPlanResponse.PlanConnection search(OtpRoutingRequest request) {
        return otpClient.plan(request);
    }
}