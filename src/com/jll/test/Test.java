package com.jll.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jll.entity.BookInfo;
import com.jll.jdbc.base.DruidPoolConnection;
import com.jll.jdbc.base.SelectItem;
import com.jll.jdbc.tools.AdvanceUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class Test {
	public static void main(String[] args) {
	   BookInfo bookInfo = new BookInfo();
//	   bookInfo.setBookPrice(BigDecimal.valueOf(50));
//	   AdvanceUtil.insert(bookInfo);
        Map<String,SelectItem> condition = new HashMap<>();
        condition.put("book_name",new SelectItem(3, SelectItem.LikeSelect.CONTAIN));
        List<BookInfo> select = AdvanceUtil.select(BookInfo.class,condition);
        for (BookInfo bookInfo1 : select){
            System.out.println(bookInfo1.getBookName());
        }
    }
}
