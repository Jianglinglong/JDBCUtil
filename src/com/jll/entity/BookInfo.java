package com.jll.entity;

import com.jll.jdbc.content.SQLColnum;
import com.jll.jdbc.content.SQLPrimaryKey;
import com.jll.jdbc.content.TableName;

@TableName(table = "t_bookinfo")
public class BookInfo {
    @SQLPrimaryKey(auto = true)
	@SQLColnum(colName = "book_id")
	private Integer bookID;
	@SQLColnum(colName = "book_name")
	private String bookName;
	@SQLColnum(colName = "book_price")
	private java.math.BigDecimal bookPrice;
	@SQLColnum(colName = "book_num")
	private Integer bookNum;
	@SQLColnum(colName = "book_author")
	private String bookAuthor;
	@SQLColnum(colName = "book_public")
	private String bookPublic;
	@SQLColnum(colName = "book_content")
	private String bookContent;

	public void setBookPublic(String bookPublic) {
		this.bookPublic = bookPublic;
	}

	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}

	public BookInfo() {
		super();
	}
	
	public BookInfo(Integer bookID) {
		super();
		this.bookID = bookID;
	}

	public BookInfo(String bookName, java.math.BigDecimal bookPrice, Integer bookNum) {
		super();
		this.bookName = bookName;
		this.bookPrice = bookPrice;
		this.bookNum = bookNum;
	}

	public Integer getBookID() {
		return bookID;
	}
	public Integer getBookNum() {
		return bookNum;
	}
	public String getBookName() {
		return bookName;
	}
	public java.math.BigDecimal getBookPrice() {
		return bookPrice;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public String getBookPublic() {
		return bookPublic;
	}
	public String getBookContent() {
		return bookContent;
	}

	public void setBookNum(Integer bookNum) {
		this.bookNum = bookNum;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public void setBookPrice(java.math.BigDecimal bookPrice) {
		this.bookPrice = bookPrice;
	}
	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	@Override
	public String toString() {
		return "[bookID=" + bookID + ", bookName=" + bookName+"bookAuthor="+bookAuthor + ", bookPrice=" + bookPrice + ", bookNum="
				+ bookNum + "]";
	}
	
}
