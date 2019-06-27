package psoft.projeto.service.helpers;

public class DeleteCommentData {
	public Long userID;
	public Long commentID;
	
	public DeleteCommentData() {
	}
	
	public DeleteCommentData(Long userID, Long commentID) {
		this.userID = userID;
		this.commentID = commentID;
	}
}
