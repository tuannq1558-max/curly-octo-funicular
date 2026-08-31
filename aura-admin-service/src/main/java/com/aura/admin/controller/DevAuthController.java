package com.aura.admin.controller;

import com.aura.admin.dto.ApiResponse;
import com.aura.admin.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * CHI DUNG DE TEST CUC BO khi chua ghep module Auth that cua TV1.
 * XOA controller nay truoc khi tich hop he thong that / deploy production.
 */
@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevAuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/token")
    public ApiResponse<String> generateToken(@RequestParam(defaultValue = "1") Long userId,
                                              @RequestParam(defaultValue = "ADMIN") String role) {
        String token = jwtTokenProvider.generateToken(userId, role);
        return ApiResponse.ok("Dev token (het han 24h) - dung header Authorization: Bearer <token>", token);
    }
}
