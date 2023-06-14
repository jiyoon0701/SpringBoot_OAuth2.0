package com.example.authServerPassword.Service;

import com.example.authServerPassword.Domain.ResourceOwner;
import com.example.authServerPassword.Persistence.MemberDAO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private MemberDAO memberDAO;
	
	private static final String namespace = "com.example.authServerPassword.mapper.memberMapper";
	
	@Override
	public void save(ResourceOwner member) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert(namespace+".save", member);
	}

	@Override
	public ResourceOwner findByUsername(String username) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.findByUsername(username);
	}

	
}
