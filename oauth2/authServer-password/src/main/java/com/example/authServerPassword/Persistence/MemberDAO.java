package com.example.authServerPassword.Persistence;

import com.example.authServerPassword.Domain.ResourceOwner;

public interface MemberDAO {
	public void save(ResourceOwner user) throws Exception;
	public ResourceOwner findByUsername(String username) throws Exception;

}
