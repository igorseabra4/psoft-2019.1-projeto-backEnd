package psoft.projeto.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CourseComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long userID;
	private String comment;
	private long parentCommentID;
	private Date date;
	private boolean deleted;
	
	public CourseComment(Long userID, String comment, Long parentCommentID) {
		this.userID = userID;
		this.comment = comment;
		this.parentCommentID = parentCommentID;
		this.date = new Date();
		this.deleted = false;
	}
	
	public long getID() {
		return id;
	}
	
	public long getUserID() {
		return userID;
	}
	
	public void setDateNow() {
		this.date = new Date();
	}
	
	public String getDate() {
		return date.toString();
	}
	
	public String getComment() {
		return comment;
	}
	
	public long getParentCommentID() {
		return parentCommentID;
	}
	
	public void delete() {
		deleted = true;
	}
	
	public boolean getDeleted() {
		return deleted;
	}
	
	public void emptyIfDeleted() {
		if (deleted)
			comment = "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (userID ^ (userID >>> 32));
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
		CourseComment other = (CourseComment) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (userID != other.userID)
			return false;
		return true;
	}
	
}
