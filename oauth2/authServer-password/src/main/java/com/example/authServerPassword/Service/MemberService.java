package com.example.authServerPassword.Service;


import com.example.authServerPassword.Domain.ResourceOwner;

public interface MemberService {
	public void save(ResourceOwner owner) throws Exception;

	public ResourceOwner findByUsername(String username) throws Exception;


}
