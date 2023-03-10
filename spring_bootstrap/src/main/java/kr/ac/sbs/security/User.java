package kr.ac.sbs.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jsp.dto.MemberVO;

public class User implements UserDetails{
	
	private MemberVO member;
	public User(MemberVO member) {
		this.member = member;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //권한: 여러개의 권한을 부여할수도 있어서 컬렉션형태
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority(member.getAuthority()));
		return roles;
	}

	@Override
	public String getPassword() { //패스워드
		
		return member.getPwd();
	}

	@Override
	public String getUsername() { //아이디
		
		return member.getId();
	}

	@Override
	public boolean isAccountNonExpired() { //기간제 계정의 경우 기간만료 여부 : enabled =4   \4가 아닐떄 true
		
		return member.getEnabled()!=4;
	}

	@Override
	public boolean isAccountNonLocked() {  //계정 정지 or 휴먼 계정 enabled = 3 \3이 아닐떄 true
		
		return member.getEnabled()!=3;
	}

	@Override
	public boolean isCredentialsNonExpired() {  //인증정보 만료 여부 : enabled =2 \2가 아닐떄 true
		
		return member.getEnabled()!=2;
	}

	@Override
	public boolean isEnabled() { //탈퇴 혹은 삭제 : enabled = 0
		
		return member.getEnabled()!=0;
	}
	
	public MemberVO getMemberVO() {
		return this.member;
	}
	
	
}
