package com.project.foradhd.domain.auth.web.dto.response;

import java.util.List;

public record ApplePublicKeyListResponse(List<ApplePublicKeyResponse> keys) {

    public record ApplePublicKeyResponse(String kty, String kid, String use,
                                        String alg, String n, String e) {

    }
}
