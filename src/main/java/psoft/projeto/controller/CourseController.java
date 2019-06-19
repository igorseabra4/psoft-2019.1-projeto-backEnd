package psoft.projeto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import psoft.projeto.exception.CourseNotFoundException;
import psoft.projeto.model.Course;
import psoft.projeto.model.CourseComment;
import psoft.projeto.service.CourseService;
import psoft.projeto.service.helpers.GradeData;

@RestController
@RequestMapping({"/v1/courses"})
public class CourseController {
	public static String[] getPrivatePatterns() {
		return new String[] {
				"/v1/courses/profile/*",
				};
	}
	
	@Autowired
	private CourseService courseService;

	@RequestMapping(value = "/")
	@ResponseBody
	public ResponseEntity<List<Course>> findAll() {
		List<Course> courses = courseService.findAll();
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/rank")
	@ResponseBody
	public ResponseEntity<List<Course>> findAllRank() {
		List<Course> courses = courseService.findAllRank();
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/substring")
	@ResponseBody
	public ResponseEntity<List<Course>> findAllSubstring(@RequestBody String substring) {
		List<Course> courses = courseService.findAll(substring);
		
		if (courses == null)
			throw new InternalError("Something went wrong");
		
		return new ResponseEntity<List<Course>>(courses, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/profile/{id}")
	@ResponseBody
	public ResponseEntity<Course> findProfile(@PathVariable Long id) throws CourseNotFoundException {
		Course course = courseService.findByID(id);
		
		if (course == null)
			throw new CourseNotFoundException("Disciplina nao encontrada");
		
		return new ResponseEntity<Course>(course, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/profile/{id}/like")
	public void addLike(@PathVariable Long id, @RequestBody Long userID) throws CourseNotFoundException {
		courseService.addLike(id, userID);
	}
	
	@DeleteMapping(value = "/profile/{id}/like")
	public void removeLike(@PathVariable Long id, @RequestBody Long userID) throws CourseNotFoundException {
		courseService.removeLike(id, userID);
	}
	
	@PutMapping(value = "/profile/{id}/grade")
	public void addGrade(@PathVariable Long id, @RequestBody GradeData data) throws CourseNotFoundException {
		courseService.addGrade(id, data.userID, data.grade);
	}
	
	@PutMapping(value = "/profile/{id}/comment")
	public void addComment(@PathVariable Long id, @RequestBody CourseComment data) throws CourseNotFoundException {
		courseService.addComment(id, data);
	}
}
