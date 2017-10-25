package com.jll.jdbc.base;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;

/**
 * 定义用于条件查询的对象
 * @author Direct
 *
 */
public class SelectItem {
	/**
	 * 查询的值
	 */
	private Object item;
	/**
	 * 查询的方式 YES 为模糊查询 NO 为一般查询
	 */
	private LikeSelect likeSelect = LikeSelect.NO;

	public SelectItem() {
		super();
	}
	
	public SelectItem(Object item) {
		super();
		this.item = item;
	}
	
	public SelectItem(Object item, LikeSelect likeSelect) {
		super();
		this.item = item;
		this.likeSelect = likeSelect;
	}

	public Object getItem() {
		return item;
	}

	public LikeSelect getLikeSelect() {
		return likeSelect;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public void setLikeSelect(LikeSelect likeSelect) {
		this.likeSelect = likeSelect;
	}

	public enum LikeSelect {
		NO,BEGIN,CONTAIN,END,NOT;
	}
}
