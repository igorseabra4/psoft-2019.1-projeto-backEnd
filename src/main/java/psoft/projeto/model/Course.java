package psoft.projeto.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import psoft.projeto.service.helpers.DeleteCommentData;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private ArrayList<Long> likedUserIDs;
	private ArrayList<CourseComment> comments;
	private HashMap<Long, Float> grades;
	
	public Course() {
		likedUserIDs = new ArrayList<Long>();
		comments = new ArrayList<CourseComment>();
		grades = new HashMap<Long, Float>();
	}
	
	public Course(String name) {
		this();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void addLike(Long userID) {
		if (!likedUserIDs.contains(userID))
			likedUserIDs.add(userID);
	}
	
	public void removeLike(Long userID) {
		likedUserIDs.remove(userID);
	}
	
	public ArrayList<Long> getLikedUserIDs(){
		return likedUserIDs;
	}

	public int getLikeCount() {
		return likedUserIDs.size();
	}
	
	public void addGrade(Long userID, float grade) {
		if (grade < 0)
			grade = 0;
		if (grade > 10)
			grade = 10;
		
		grades.put(userID, grade);
	}
	
	public HashMap<Long, Float> getGrades(){
		return grades;
	}
	
	public float getGrade() {
		if (grades.values().size() == 0)
			return 0;
		
		float add = 0;
		for (Float f : grades.values())
			add += f;
		return add / grades.values().size();
	}
	
	public void addComment(CourseComment comment) {
		comments.add(comment);
	}

	public void deleteComment(DeleteCommentData data) {
		for (CourseComment c : comments)
			if (c.getID() == data.commentID && c.getUserID() == data.userID)
				c.delete();
	}
	
	public ArrayList<CourseComment> getComments(){
		return comments;
	}
	
	public void sortComments() {

		comments.sort(new Comparator<CourseComment>() 
        {
			@Override
			public int compare(CourseComment arg0, CourseComment arg1) {
				return (int) (arg1.getParentCommentID() - arg0.getParentCommentID());
			}
        });
	}
	
	public void emptyDeletedComments() {
		for (CourseComment c : comments)
			c.emptyIfDeleted();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
