package com.moa.model.service;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moa.model.MemberDto;
import com.moa.model.mapper.MemberMapper;

/**
 * 2021-01-26
 * 로그인 서비스 구현 - id, pw 필수 검사
 * 
 * @author Team Together
 */

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		// 아이디와 비밀번호 둘 중 하나라도 입력으로 주어지지 않았다면 null 반환
		if(memberDto.getId() == null || memberDto.getPw() == null)
			return null;
		return sqlSession.getMapper(MemberMapper.class).login(memberDto);
	}

	@Override
	public boolean idChk(String id) {
		// 중복된 아이디가 존재할 경우
		if(sqlSession.getMapper(MemberMapper.class).idChk(id) > 0)
            return false;
        return true;
	}
	
	public MemberDto memberUpdate(MemberDto memberDto) throws SQLException {
		//회원정보 수정은 이미 로그인이 된 상태로 진행 가능
		sqlSession.getMapper(MemberMapper.class).memberUpdate(memberDto);
		return sqlSession.getMapper(MemberMapper.class).memberSearch(memberDto);
	}
}
