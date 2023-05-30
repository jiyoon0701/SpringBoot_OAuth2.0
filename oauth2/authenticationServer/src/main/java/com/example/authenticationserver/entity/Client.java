package com.example.authenticationserver.entity;

import lombok.Getter;
import com.example.authenticationserver.constrant.ClientType;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * 
 * @author yun-yeoseong
 *
 */
public class Client extends BaseClientDetails{

	private static final long serialVersionUID = 5840531070411146325L; // 직렬화 버전의 고유값
	
	@Getter
	private ClientType clientType;


	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
		this.addAdditionalInformation("client_type", clientType.name());
	}

}
