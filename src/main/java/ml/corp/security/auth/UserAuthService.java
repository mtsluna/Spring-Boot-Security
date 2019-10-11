package ml.corp.security.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

	private UserAuthRepository userAuthRepository;
	
	public UserAuthService(UserAuthRepository userAuthRepository) {
		this.userAuthRepository = userAuthRepository;
	}
	
	public boolean checkUser(UserAuth userAuth) {	
		
		int result = this.userAuthRepository.checkUser(encript(userAuth.getUsername()), encript(userAuth.getPassword()));
		if (result == 0) {
			return false;
		}
		return true;
	}
	
	public String encript(String text) {
		
		text = Base64.getEncoder().encodeToString(text.getBytes());
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			byte[] hashInBytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
			
			StringBuilder sb = new StringBuilder();
			for(byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			
			text = sb.toString();
			System.out.println(text);
			
		} catch (Exception e) {}
		
		return text;
		
	}
	
}
