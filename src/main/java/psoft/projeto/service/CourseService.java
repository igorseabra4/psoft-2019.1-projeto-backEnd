package psoft.projeto.service;

import java.io.FileReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import psoft.projeto.dao.CourseDAO;
import psoft.projeto.exception.CourseNotFoundException;
import psoft.projeto.model.Course;
import psoft.projeto.model.CourseComment;
import psoft.projeto.model.CourseSimple;
import psoft.projeto.service.helpers.DeleteCommentData;

@Service
public class CourseService {
	private final CourseDAO courseDAO;
	
	CourseService(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
		init();
	}
	
	@SuppressWarnings("unchecked")
    public void init () {
        try {
            JSONParser parser = new JSONParser(new FileReader("disciplinas.json"));
        	ArrayList<Object> list = parser.parseArray();
        	
        	List<Course> courses = new ArrayList<Course>(list.size());
        	
            for (Object o : list) {
            	LinkedHashMap<String, String> m = (LinkedHashMap<String, String>) o;
            	courses.add(new Course(m.get("nome")));
            }
            
            List<Course> allCourses = courseDAO.findAll();
            
            for (Course c : courses) {
            	if (!allCourses.contains(c))
            		create(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public Course create(Course course) {
		return courseDAO.save(course);
	}

	public Course findByID(Long id) {
		Course c = courseDAO.findByID(id);

		c.emptyDeletedComments();
		c.sortComments();
		
		return c;
	}

	public List<CourseSimple> findAll() {
		List<CourseSimple> courses = new ArrayList<CourseSimple>((int) courseDAO.count());
		
		for (Course c : courseDAO.findAll())
			courses.add(new CourseSimple(c.getId(), c.getName()));
		
		courses.sort(new Comparator<CourseSimple>() 
        {
			@Override
			public int compare(CourseSimple arg0, CourseSimple arg1) {
				return (int) (arg0.id - arg1.id);
			}
        });
		
		return courses;
	}
	
	public List<CourseSimple> findAll(String substring) {
		List<Course> all = courseDAO.findAll();
		List<CourseSimple> courses = new ArrayList<CourseSimple>(all.size());
		
		for (Course c : all)
			if (normalize(c.getName()).contains(normalize(substring)))
				courses.add(new CourseSimple(c.getId(), c.getName()));

		courses.sort(new Comparator<CourseSimple>() 
        {
			@Override
			public int compare(CourseSimple arg0, CourseSimple arg1) {
				return (int) (arg0.id - arg1.id);
			}
        });
		
		return courses;
	}
	
	private static String normalize(String str) {
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
	}
	
	public void addLike(Long courseID, Long userID) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		course.addLike(userID);
		courseDAO.save(course);
	}

	public void removeLike(Long courseID, Long userID) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		course.removeLike(userID);
		courseDAO.save(course);
	}
	
	public void addGrade(Long courseID, Long userID, Float grade) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		course.addGrade(userID, grade);
		courseDAO.save(course);
	}
	
	public void addComment(Long courseID, CourseComment comment) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		course.addComment(comment);
		courseDAO.save(course);
	}

	public void removeComment(Long courseID, DeleteCommentData data) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		course.deleteComment(data);
		courseDAO.save(course);
	}
	
	// returns list ordered by like count, then grade, then comment count.
	public List<Course> findAllRank() {
		List<Course> all = courseDAO.findAll();

		all.sort(new Comparator<Course>() 
        {
			@Override
			public int compare(Course arg0, Course arg1) {
				return arg1.getComments().size() - arg0.getComments().size();
			}
        });
		all.sort(new Comparator<Course>() 
        {
			@Override
			public int compare(Course arg0, Course arg1) {
				return (int) Math.ceil(arg1.getGrade() - arg0.getGrade());
			}
        });
		all.sort(new Comparator<Course>() 
        {
			@Override
			public int compare(Course arg0, Course arg1) {
				return arg1.getLikeCount() - arg0.getLikeCount();
			}
        });
		
		for (Course c : all)
			c.emptyDeletedComments();
		
		return all;
	}
}