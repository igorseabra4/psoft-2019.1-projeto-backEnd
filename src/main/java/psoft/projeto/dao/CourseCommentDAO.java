package psoft.projeto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import psoft.projeto.model.CourseComment;

@Repository
public interface CourseCommentDAO extends JpaRepository<CourseComment, Long> {
	CourseComment save(CourseComment course);
	
	@Query(value="Select c from CourseComment c where c.id=:plogin")
	CourseComment findByID(@Param("plogin") Long id);
}
