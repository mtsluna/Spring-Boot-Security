package ml.corp.security.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Integer>{

	@Query(value = "SELECT COUNT(*) AS exist FROM user_auth WHERE (user_auth_username = ?1 AND user_auth_password = ?2)", nativeQuery = true)
	public int checkUser(String username, String password);
	
}
