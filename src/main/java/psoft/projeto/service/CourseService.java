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
import psoft.projeto.dao.CourseCommentDAO;
import psoft.projeto.exception.CourseNotFoundException;
import psoft.projeto.model.Course;
import psoft.projeto.model.CourseComment;
import psoft.projeto.model.CourseSimple;
import psoft.projeto.service.helpers.DeleteCommentData;
import psoft.projeto.service.helpers.PutCommentData;

@Service
public class CourseService {
	private final CourseDAO courseDAO;
	private final CourseCommentDAO courseCommentDAO;
	
	CourseService(CourseDAO courseDAO, CourseCommentDAO courseCommentDAO) {
		this.courseDAO = courseDAO;
		this.courseCommentDAO = courseCommentDAO;
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
		return courseDAO.findByID(id);
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
		
		int number = -1;
        boolean numeric = true;
        
        try {
        	number = Integer.parseInt(substring);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        
		for (Course c : all)
			if ((numeric && number == c.getId()) || normalize(c.getName()).contains(normalize(substring)))
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
			throw new CourseNotFoundException("Disciplina não encontrada");
		
		course.addLike(userID);
		courseDAO.save(course);
	}

	public void removeLike(Long courseID, Long userID) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina não encontrada");
		
		course.removeLike(userID);
		courseDAO.save(course);
	}
	
	public void addGrade(Long courseID, Long userID, Float grade) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina não encontrada");
		
		course.addGrade(userID, grade);
		courseDAO.save(course);
	}
	
	public List<CourseComment> findComments(Long courseID) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina não encontrada");
		
		List<CourseComment> comments = new ArrayList<CourseComment>();
		
		for (Long l : course.getCommentsIDs())
			comments.add(courseCommentDAO.findByID(l));
		
		comments.sort(new Comparator<CourseComment>() 
        {
			@Override
			public int compare(CourseComment arg1, CourseComment arg0) {
				return arg1.getDate().compareTo(arg0.getDate());
			}
        });
		
		comments.sort(new Comparator<CourseComment>() 
        {
			@Override
			public int compare(CourseComment arg0, CourseComment arg1) {
				return (int) (arg0.getParentCommentID() - arg1.getParentCommentID());
			}
        });
		
		return comments;
	}
	
	public void addComment(Long courseID, PutCommentData comment) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina não encontrada");

		if (comment.userName.isEmpty())
			throw new IllegalArgumentException("Comentário precisa de nome de usuário");
		if (comment.comment.isEmpty())
			throw new IllegalArgumentException("Comentário não pode ser vazio");
		if (comment.parentCommentID != -1 && !course.getCommentsIDs().contains(comment.parentCommentID))
			throw new IllegalArgumentException("Comentário é filho de comentário inexistente");
		
		CourseComment saved = courseCommentDAO.save(
				new CourseComment(comment.userID, comment.userName, comment.comment, comment.parentCommentID));
		
		course.addComment(saved.getID());
		
		courseDAO.save(course);
	}
	
	public void removeComment(Long courseID, DeleteCommentData data) throws CourseNotFoundException {
		Course course = courseDAO.findByID(courseID);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina não encontrada");
		
		if (course.getCommentsIDs().contains(data.commentID)) {
			CourseComment comment = courseCommentDAO.findByID(data.commentID);

			comment.delete();
			courseCommentDAO.save(comment);
		}
	}
	
	// returns list ordered by like count, then grade, then comment count.
	public List<Course> findAllRank() {
		List<Course> all = courseDAO.findAll();
		
		all.sort(new Comparator<Course>() 
        {
			@Override
			public int compare(Course arg0, Course arg1) {
				return arg1.getCommentsIDs().size() - arg0.getCommentsIDs().size();
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
		
		return all;
	}

	public List<CourseComment> findAllComments() {
		return courseCommentDAO.findAll();
	}
	
	public void resetComments() {
		for (CourseComment comment : courseCommentDAO.findAll()) {
			boolean found = false;
			
			comment.undelete();
			
			for (Course course : courseDAO.findAll())
				if (course.getCommentsIDs().contains(comment.getID())) {
					found = true;
					break;
				}
			
			if (!found)
				courseCommentDAO.delete(comment);
			else
				courseCommentDAO.save(comment);
		}
	}

	public void resetComments(Long id) throws CourseNotFoundException {
		Course course = courseDAO.findByID(id);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		for (Long l : course.getCommentsIDs()) {
			CourseComment c = courseCommentDAO.findByID(l);
			
			c.undelete();
			
			courseCommentDAO.save(c);
		}
	}
}
