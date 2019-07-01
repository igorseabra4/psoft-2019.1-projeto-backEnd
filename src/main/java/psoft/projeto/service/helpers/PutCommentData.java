package psoft.projeto.service.helpers;

public class PutCommentData {
	public String userName;
	public String comment;
	public Long parentCommentID;
	
	public PutCommentData() {
	}
	
	public PutCommentData(String userID, String comment, Long parentCommentID) {
		this.userName = userID;
		this.comment = comment;
		this.parentCommentID = parentCommentID;
	}
}
