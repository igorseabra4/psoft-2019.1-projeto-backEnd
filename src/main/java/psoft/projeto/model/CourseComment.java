package psoft.projeto.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Entity;

@Entity
public class CourseComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String userName;
	private String comment;
	private long parentCommentID;
	private Date date;
	private boolean deleted;

	public CourseComment() {
	}
	
	public CourseComment(String userName, String comment, Long parentCommentID) {
		this.userName = userName;
		this.comment = comment;
		this.parentCommentID = parentCommentID;
		this.date = new Date();
		this.deleted = false;
	}
	
	public long getID() {
		return id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getDateString() {
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
		if (userName != other.userName)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return id + " " + userName + " " + comment + " " + date.toString();
	}
	
}
