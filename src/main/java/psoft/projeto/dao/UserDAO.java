package psoft.projeto.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import psoft.projeto.model.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
	User save(User user);

	@Query(value="Select u from User u where u.login=:plogin")
	User findByLogin(@Param("plogin") String id);
	
	@Query(value="Select u from User u where u.id=:pid")
	Optional<User> findById(@Param("pid") Long id);
}
