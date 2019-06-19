package psoft.projeto.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import psoft.projeto.model.Usuario;

@Repository
public interface UserDAO extends JpaRepository<Usuario, Long> {
	Usuario save(Usuario user);

	@Query(value="Select u from Usuario u where u.login=:plogin")
	Usuario findByLogin(@Param("plogin") String id);
	
	@Query(value="Select u from Usuario u where u.id=:pid")
	Optional<Usuario> findById(@Param("pid") Long id);
}
