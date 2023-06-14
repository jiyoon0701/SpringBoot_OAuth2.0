package com.example.authServerPassword.Persistence;

import com.example.authServerPassword.Domain.ResourceOwner;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberDAOImpl implements MemberDAO{
	
	@Autowired
	private SqlSession sqlSession;
	
	private static final String namespace = "com.example.authServerPassword.mapper.memberMapper";
	
	@Override
	public void save(ResourceOwner user) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+".save", user);
	}

	@Override
	public ResourceOwner findByUsername(String username) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(namespace+".findByUsername",username);
	}




	
	
	
	
}
