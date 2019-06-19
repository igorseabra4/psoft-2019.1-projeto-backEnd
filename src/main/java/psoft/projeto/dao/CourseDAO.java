package psoft.projeto.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import psoft.projeto.model.Course;

@Repository
public interface CourseDAO extends JpaRepository<Course, Long> {
	Course save(Course course);
	
	@Query(value="Select c from Course c where c.id=:plogin")
	Course findByID(@Param("plogin") Long id);
}
