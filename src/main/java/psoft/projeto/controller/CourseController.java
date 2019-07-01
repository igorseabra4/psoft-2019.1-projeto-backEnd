package psoft.projeto.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import psoft.projeto.exception.CourseNotFoundException;
import psoft.projeto.model.Course;
import psoft.projeto.model.CourseComment;
import psoft.projeto.model.CourseSimple;
import psoft.projeto.service.CourseService;
import psoft.projeto.service.helpers.DeleteCommentData;
import psoft.projeto.service.helpers.GradeData;
import psoft.projeto.service.helpers.PutCommentData;

@RestController
@RequestMapping({"/v1/courses"})
public class CourseController {
	public static String[] getPrivatePatterns() {
		return new String[] {
				"/v1/courses/profile/*",
				"/v1/courses/rank",
		};
	}
	
	@Autowired
	private CourseService courseService;

	@RequestMapping(value = "")
	public ResponseEntity<List<CourseSimple>> findAll() {
		List<CourseSimple> courses = courseService.findAll();
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<CourseSimple>>(courses, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/substring")
	public ResponseEntity<List<CourseSimple>> findAllSubstring(@RequestParam(name="str", required=false, defaultValue="") String substring) {
		List<CourseSimple> courses = courseService.findAll(substring);
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<CourseSimple>>(courses, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rank")
	public ResponseEntity<List<Course>> findAllRank() {
		List<Course> courses = courseService.findAllRank();
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/profile/{id}")
	public ResponseEntity<Course> findProfile(@PathVariable Long id) throws CourseNotFoundException {
		Course course = courseService.findByID(id);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile/{id}/like")
	public ResponseEntity<Boolean> addLike(@PathVariable Long id, @RequestBody Long userID) throws CourseNotFoundException {
		courseService.addLike(id, userID);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/profile/{id}/like")
	public ResponseEntity<Boolean> removeLike(@PathVariable Long id, @RequestBody Long userID) throws CourseNotFoundException {
		courseService.removeLike(id, userID);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile/{id}/grade")
	public ResponseEntity<Boolean> addGrade(@PathVariable Long id, @RequestBody GradeData data) throws CourseNotFoundException {
		courseService.addGrade(id, data.userID, data.grade);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@RequestMapping(value = "/profile/{id}/comments")
	public ResponseEntity<List<CourseComment>> getComments(@PathVariable Long id) throws CourseNotFoundException {
		List<CourseComment> comments = courseService.findComments(id);
		
		if (comments == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<CourseComment>>(comments, HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile/{id}/comment")
	public ResponseEntity<Boolean> addComment(@PathVariable Long id, @RequestBody PutCommentData data) throws CourseNotFoundException {
		courseService.addComment(id, data);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/profile/{id}/comment")
	public ResponseEntity<Boolean> removeComment(@PathVariable Long id, @RequestBody DeleteCommentData data) throws CourseNotFoundException {
		courseService.removeComment(id, data);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	@PutMapping(value = "/profile/{id}/resetcomments")
	public ResponseEntity<Boolean> resetComments(@PathVariable Long id) throws CourseNotFoundException {
		courseService.resetComments(id);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
