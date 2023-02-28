package kr.ac.sbs.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.jsp.dto.MemberVO;

import kr.ac.sbs.exception.InvalidPasswordException;
import kr.ac.sbs.exception.NotFoundIdException;
import kr.ac.sbs.service.MemberService;

public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	private MemberService memberService;
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String login_id = (String) auth.getPrincipal();
		String login_pwd = (String) auth.getCredentials();
		
		try {
			memberService.login(login_id, login_pwd);
			
			MemberVO member = memberService.getMember(login_id);
			
			UserDetails authUser = new User(member); 
			boolean invalidCheck= authUser.isAccountNonExpired()
					&& authUser.isAccountNonLocked()
					&& authUser.isCredentialsNonExpired()
					&& authUser.isEnabled();
			if(invalidCheck) {
				UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken
						(authUser.getUsername(), authUser.getPassword(), authUser.getAuthorities());
				
				result.setDetails(authUser);
				
				return result;
				
			}else {
				throw new BadCredentialsException("유효하지 않은 계정입니다.");
			}
			
		} catch (NotFoundIdException |InvalidPasswordException e) {
			throw new BadCredentialsException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthenticationServiceException("서버 장애로 서비스가 불가합니다.");
		}
		
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
