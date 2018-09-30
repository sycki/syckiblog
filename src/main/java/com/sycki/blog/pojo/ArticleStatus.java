package com.sycki.blog.pojo;

public enum ArticleStatus {
	OPENT(1), CLOSE(2), PENDING(3);
	
	private int status;
	
	private ArticleStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	
	
}
