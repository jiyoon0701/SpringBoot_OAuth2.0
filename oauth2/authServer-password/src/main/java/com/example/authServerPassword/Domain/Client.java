package com.example.authServerPassword.Domain;

import com.example.authServerPassword.constrant.ClientType;
import lombok.Getter;

public class Client extends CustomClient{
    private static final long serialVersionUID = 5840531070411146325L; // 직렬화 버전의 고유값

    @Getter
    private ClientType clientType;

    @Getter
    private Boolean tokenType;

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
        this.addAdditionalInformation("client_type", clientType.name());
    }

    public void setTokenType(Boolean tokenType) {
        this.tokenType = tokenType;
        this.addAdditionalInformation("token_type", tokenType);
    }
}
