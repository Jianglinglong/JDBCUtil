package com.jll.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jll.entity.BookInfo;
import com.jll.jdbc.base.DruidPoolConnection;
import com.jll.jdbc.base.SelectItem;
import com.jll.jdbc.tools.AdvanceUtil;

public class Test {
	public static void main(String[] args) {
		Map<String, SelectItem> condition = new HashMap<>();
		condition.put("book_id", new SelectItem(10005, SelectItem.LikeSelect.NOT));
		List<BookInfo> select = AdvanceUtil.select(BookInfo.class,condition);
		BookInfo bookInfo = new BookInfo();
		bookInfo.setBookPrice(BigDecimal.valueOf(85));
		AdvanceUtil.delete(bookInfo);
        DruidPoolConnection druidPoolConnection = DruidPoolConnection.getInstance();
        System.out.println(druidPoolConnection);
        System.out.println(select);
	}
}
