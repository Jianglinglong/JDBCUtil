package com.jll.jdbc.tools;

import com.jll.jdbc.base.SelectItem;
import com.jll.jdbc.content.SQLColnum;
import com.jll.jdbc.content.TableName;
import com.jll.jdbc.excption.JDBCExcption;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdvanceUtil {

    /**
     * 获取字段，存入List集合 Colnum 中
     *
     * @param cla
     * @param Colnum
     */
    private static void getColnum(Class<?> cla, List<String> Colnum) {
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            SQLColnum SQLColnum = field.getAnnotation(SQLColnum.class);
            if (SQLColnum == null) {
                Colnum.add(field.getName());
            } else {
                Colnum.add(SQLColnum.colName());
            }
        }
    }

    /**
     * 获取字段存入col中，对应字段的值存入params中
     *
     * @param obj
     * @param Colnum
     * @param params
     */
    private static void getColnum(Object obj, List<String> Colnum, List<Object> params) {
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            SQLColnum SQLColnum = field.getAnnotation(SQLColnum.class);
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), aClass);
                Object invoke = pd.getReadMethod().invoke(obj);
                params.add(invoke);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (SQLColnum == null) {
                Colnum.add(field.getName());
            } else {
                Colnum.add(SQLColnum.colName());
            }
        }
    }

    /**
     * @param obj 封装好的数据对象
     * @return int 受影响的行数
     * @Title: update
     * @Description: 利用反射修改数据库的记录
     */
    public static int update(Object obj) {
        StringBuffer sqlBuffer = new StringBuffer("update ");
        String table = obj.getClass().getSimpleName();
        TableName tableName = obj.getClass().getAnnotation(TableName.class);
        if(tableName!=null){
            table = tableName.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<String> colnum = new ArrayList<String>();
        getColnum(obj, colnum, params);
        sqlBuffer.append(table + " set ");
        for (String col : colnum) {
            sqlBuffer.append(col + "=?,");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.lastIndexOf(","));
        sqlBuffer.append(" where " + colnum.get(0) + "=?");
        params.add(params.get(0));
        return BaseUtil.update(sqlBuffer.toString(), params.toArray());
    }

    /**
     * 删除一条或者多条记录，根据封装对象中的属性值
     *
     * @param obj
     * @return int 成功删除的数量
     */
    public static int delete(Object obj) {
        Class<?> cla = obj.getClass();
        String tableName = cla.getSimpleName();
        TableName table = cla.getAnnotation(TableName.class);
        if (table!=null){
            tableName = table.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<Object> param = new ArrayList<Object>();
        List<String> col = new ArrayList<String>();
        getColnum(obj, col, params);
        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + tableName + " where ");
        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);
            if (value != null) {
                sql.append(col.get(i) + "=? and ");
                param.add(params.get(i));
            }
        }
        if (param.size() == 0) {
            return 0;
        }
        sql.delete(sql.length() - 4, sql.length());
        return BaseUtil.update(sql.toString(), param.toArray());
    }

    /**
     * 根据条件删除记录
     *
     * @param cla
     * @param condition
     * @return int 成功删除的数量
     */
    public static int delete(Class<?> cla, Map<String, SelectItem> condition) {
        String tableName = cla.getSimpleName();
        TableName table = cla.getAnnotation(TableName.class);
        if (table!=null){
            tableName = table.table();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + tableName);
        List<Object> params = new ArrayList<Object>();
        sql.append(creatCondition(condition, params));
        return BaseUtil.update(sql.toString(), params.toArray());
    }

    /**
     * @param obj
     * @return int  受影响的行数
     * INSERT INTO `class` VALUES ('0001', '二年级三班');
     * @Title: insert
     * @Description: 将封装好的对象插入到对应的表中
     */
    public static int insert(Object obj) {
        Class<? extends Object> cla = obj.getClass();
        String tableName = cla.getSimpleName();
        TableName table = cla.getAnnotation(TableName.class);
        if (table!=null){
            tableName = table.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<String> colnums = new ArrayList<String>();
        getColnum(obj, colnums, params);
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO " + tableName + "(");
        //构建插入的字段
        for (int i = 0; i < colnums.size(); i++) {
            sql.append(" " + colnums.get(i) + ",");
        }
        //删除多余的逗号
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(") VALUES (");
        //构建插入的字段的值得占位符
        for (int i = 0; i < params.size(); i++) {
            sql.append("?,");
        }
        //删除多余的逗号
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");
        return BaseUtil.update(sql.toString(), params.toArray());
    }

    /**
     * <pre>
     * @Title: select
     * @Description: 根据类型查询指定表中的数据
     * @param  cla 与数据库中表名相同、属性匹配的类对象
     * @return List<T>    返回类型
     * @throws
     * </pre>
     */
    public static <T> List<T> select(Class<T> cla) {
        String tableName = cla.getSimpleName();
        TableName table = cla.getAnnotation(TableName.class);
        if (table!=null){
            tableName = table.table();
        }
        List<T> resulet = new ArrayList<T>();
        List<String> col = new ArrayList<String>();
        getColnum(cla, col);
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        for (String key : col) {
            sql.append(key + ",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" FROM " + tableName);
        List<Map<String, Object>> rs = BaseUtil.select(sql.toString());
        resulet = parse(cla, rs);
        return resulet;
    }

    /**
     * 构建SQL查询的where条件语句
     *
     * @param condition
     * @param params
     * @return
     */

    private static String creatCondition(Map<String, SelectItem> condition, List<Object> params) {
        StringBuffer sb = new StringBuffer();
        if (condition != null && condition.size() > 0) {
            sb.append(" WHERE ");
            Set<String> keySet = condition.keySet();
            for (String key : keySet) {
                SelectItem select = condition.get(key);
                switch (select.getLikeSelect()) {
                    case BEGIN:
                        params.add(condition.get(key).getItem() + "%");
                        sb.append(key + " like ? and ");
                        break;
                    case CONTAIN:
                        params.add("%" + condition.get(key).getItem() + "%");
                        sb.append(key + " like ? and ");
                        break;
                    case END:
                        params.add("%" + condition.get(key).getItem());
                        sb.append(key + " like ? and ");
                        break;
                    case NOT:
                        sb.append(key + " <>? and ");
                        params.add(condition.get(key).getItem());
                        break;
                    case NO:
                        sb.append(key + " =? and ");
                        params.add(condition.get(key).getItem());
                        break;
                }
            }
            sb.delete(sb.length() - 5, sb.length());
        }
        return sb.toString();
    }

    /**
     * 根据条件查询
     *
     * @param cla
     * @param condition
     * @return 封装对象的集合
     */

    public static <T> List<T> select(Class<T> cla, Map<String, SelectItem> condition) {
        List<T> resulet = new ArrayList<T>();
        List<Object> params = new ArrayList<Object>();
        List<String> colnum = new ArrayList<String>();
        String tableName = cla.getSimpleName();
        TableName table = cla.getAnnotation(TableName.class);
        if(table!=null){
            tableName = table.table();
        }
        getColnum(cla, colnum);
        String sqlcondition = creatCondition(condition, params);
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        for (String key : colnum) {
            sb.append(" " + key + ",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" FROM " + tableName + " " + sqlcondition);
        List<Map<String, Object>> rs = BaseUtil.select(sb.toString(), params.toArray());
        resulet = parse(cla, rs);
        return resulet;
    }

    /**
     * <pre>
     * @Title: parse
     * @Description: 将工具类查询结果封装到cla类型的List集合中
     * @param  cla 封装的类型
     * @param  rs 使用MysqlUtil查询的List集合
     * @return List<T>    封装好的List集合
     * @throws
     * </pre>
     */
    private static <T> List<T> parse(Class<T> cla, List<Map<String, Object>> rs) {
        List<T> result = new ArrayList<>();
        Field[] fields = cla.getDeclaredFields();
        for (Map<String, Object> row : rs) {
            Set<String> dbFieldNames = row.keySet();
            try {
                T t = cla.newInstance();
                for (String dbFieldName : dbFieldNames) {
                    for (Field field : fields) {
                        SQLColnum SQLColnum = field.getAnnotation(SQLColnum.class);
                        String colName = field.getName();
                        PropertyDescriptor pd = new PropertyDescriptor(colName, cla);
                        if (SQLColnum != null) {
                            colName = SQLColnum.colName();
                        }
                        if (colName.equalsIgnoreCase(dbFieldName)) {
                            pd.getWriteMethod().invoke(t, row.get(dbFieldName));
                            continue;
                        }
                    }
                }
                result.add(t);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 在整个表中分页查询
     *
     * @param cla      表明对应的类对象
     * @param page     页码
     * @param pageSize 页面大小
     * @return 返回默认显示页面的对象集合
     */
    public static <T> List<T> select(Class<T> cla, int page, int pageSize) {
        if (page < 1) {
            throw new JDBCExcption("页码错误");
        }
        List<String> colnum = new ArrayList<String>();
        getColnum(cla, colnum);
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        for (String key : colnum) {
            sb.append(key + ",");
        }
        String simpleName = cla.getSimpleName();
        TableName tableName = cla.getAnnotation(TableName.class);
        if (tableName != null){
            simpleName = tableName.table();
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" FROM " + simpleName);
        page = (page - 1) * pageSize;
        sb.append(" LIMIT " + page + "," + pageSize);
        List<Map<String, Object>> pages = BaseUtil.select(sb.toString());
        return parse(cla, pages);
    }


    public static <T> List<T> select(Class<T> cla, Map<String, SelectItem> condition, int page, int pageSize) {
        if (page < 1) {
            throw new JDBCExcption("页码错误");
        }
        List<String> colnum = new ArrayList<String>();
        List<Object> params = new ArrayList<Object>();
        getColnum(cla, colnum);
        String where = creatCondition(condition, params);
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        for (String key : colnum) {
            sb.append(key + ",");
        }
        String simpleName = cla.getSimpleName();
        TableName tableName = cla.getAnnotation(TableName.class);
        if (tableName!=null){
            simpleName = tableName.table();
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" FROM " + simpleName);
        sb.append(" " + where + " ");
        page = (page - 1) * pageSize;
        sb.append(" LIMIT " + page + "," + pageSize);
        List<Map<String, Object>> pages = BaseUtil.select(sb.toString(), params.toArray());
        return parse(cla, pages);
    }

}
