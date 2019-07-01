package psoft.projeto.service.helpers;

public class PutCommentData {
	public Long userID;
	public String userName;
	public String comment;
	public Long parentCommentID;
	
	public PutCommentData() {
	}
	
	public PutCommentData(Long userID, String userName, String comment, Long parentCommentID) {
		this.userID = userID;
		this.userName = userName;
		this.comment = comment;
		this.parentCommentID = parentCommentID;
	}
}
