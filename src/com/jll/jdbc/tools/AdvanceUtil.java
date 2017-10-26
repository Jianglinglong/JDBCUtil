package com.jll.jdbc.tools;

import com.jll.jdbc.base.SelectItem;
import com.jll.jdbc.consts.SQLOperator;
import com.jll.jdbc.content.SQLColnum;
import com.jll.jdbc.content.SQLTableName;
import com.jll.jdbc.excption.ExceptionMsg;
import com.jll.jdbc.excption.JDBCExcption;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    private static void getColnum(Object obj, List<String> Colnum, Map<String,Object> params) {
        Class<?> aClass = obj.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            SQLColnum SQLColnum = field.getAnnotation(SQLColnum.class);

            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(fieldName, aClass);
                Object invoke = pd.getReadMethod().invoke(obj);
                if (SQLColnum != null) {
                    fieldName = SQLColnum.colName();
                }
                Colnum.add(fieldName);
                if(invoke!=null){
                    params.put(fieldName,invoke);
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
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
        Class<?> cla = obj.getClass();
        StringBuffer sqlBuffer = new StringBuffer(SQLOperator.UPDATE.getOperator());
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<String> colnum = new ArrayList<String>();
        Map<String,Object> map = new HashMap<>();
        getColnum(obj, colnum, map);
        sqlBuffer.append(simpleName + SQLOperator.SET.getOperator());
        for (String col : colnum) {
            sqlBuffer.append(col + SQLOperator.EQUAL_PARAM.getOperator()+SQLOperator.COMMA.getOperator());
            params.add(map.get(col));
        }
        sqlBuffer.deleteCharAt(sqlBuffer.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sqlBuffer.append(SQLOperator.WHERE.getOperator() + colnum.get(0) + SQLOperator.EQUAL_PARAM.getOperator());
        params.add(map.get(colnum.get(0)));
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
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<String> col = new ArrayList<String>();
        Map<String,Object> map = new HashMap<>();
        getColnum(obj, col, map);
        StringBuffer sql = new StringBuffer();
        sql.append(SQLOperator.DELETE.getOperator() + simpleName);
        if (map.size() == 0) {
            return 0;
        }
        addParamsToSql(sql,map,params);
//        sql.delete(sql.length() - 4, sql.length());
        return BaseUtil.update(sql.toString(), params.toArray());
    }

    /**
     * 根据条件删除记录
     *
     * @param cla
     * @param condition
     * @return int 成功删除的数量
     */
    public static int delete(Class<?> cla, Map<String, SelectItem> condition) {
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        StringBuffer sql = new StringBuffer();
        sql.append(SQLOperator.DELETE.getOperator() + simpleName);
        List<Object> params = new ArrayList<Object>();
        sql.append(creatCondition(condition, params));
        return BaseUtil.update(sql.toString(), params.toArray());
    }

    /**
     * @param obj
     * @return int  受影响的行数
     * INSERT INTO `class` ( ? , ? ) VALUES ('0001', '二年级三班');
     * @Title: insert
     * @Description: 将封装好的对象插入到对应的表中
     */
    public static int insert(Object obj) {
        Class<? extends Object> cla = obj.getClass();
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        List<Object> params = new ArrayList<Object>();
        List<String> colnums = new ArrayList<String>();
        Map<String,Object> map = new HashMap<>();
        getColnum(obj, colnums, map);
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlParam = new StringBuffer();
        sql.append(SQLOperator.INSERT.getOperator() + simpleName + SQLOperator.LEFT_BRACKET.getOperator());
        //构建插入的字段

        for (int i = 0; i < colnums.size(); i++) {
            sql.append( colnums.get(i) + SQLOperator.COMMA.getOperator());
            params.add(map.get(colnums.get(i)));
            sqlParam.append(SQLOperator.PARAM.getOperator()+SQLOperator.COMMA.getOperator());
        }
        //删除多余的逗号
        sql.deleteCharAt(sql.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sqlParam.deleteCharAt(sqlParam.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sql.append( SQLOperator.RIGHT_BRACKET.getOperator()+SQLOperator.VALUES.getOperator()+SQLOperator.LEFT_BRACKET.getOperator());
        //构建插入的字段的值得占位符
        sql.append(sqlParam);
        sql.append(SQLOperator.RIGHT_BRACKET.getOperator());
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
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        List<T> resulet = new ArrayList<T>();
        List<String> col = new ArrayList<String>();
        getColnum(cla, col);
        StringBuffer sql = new StringBuffer();
        sql.append(SQLOperator.SELECT.getOperator());
        for (String key : col) {
            sql.append(key + SQLOperator.COMMA.getOperator());
        }
        sql.deleteCharAt(sql.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sql.append(SQLOperator.FROM.getOperator() + simpleName);
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
            sb.append(SQLOperator.WHERE.getOperator());
            Set<String> keySet = condition.keySet();
            for (String key : keySet) {
                SelectItem select = condition.get(key);
                switch (select.getLikeSelect()) {
                    case BEGIN:
                        params.add(condition.get(key).getItem() + SQLOperator.LIKE_PARAM.getOperator());
                        sb.append(key + SQLOperator.LIKE_PARAM_AND.getOperator());
                        break;
                    case CONTAIN:
                        params.add(SQLOperator.LIKE_PARAM.getOperator() + condition.get(key).getItem() + SQLOperator.LIKE_PARAM.getOperator());
                        sb.append(key + SQLOperator.LIKE_PARAM_AND.getOperator());
                        break;
                    case END:
                        params.add(SQLOperator.LIKE_PARAM.getOperator() + condition.get(key).getItem());
                        sb.append(key + SQLOperator.LIKE_PARAM_AND.getOperator());
                        break;
                    case NOT:
                        sb.append(key + SQLOperator.NOT_EQUAL_PARAM_AND.getOperator());
                        params.add(condition.get(key).getItem());
                        break;
                    case NO:
                        sb.append(key + SQLOperator.EQUAL_PARAM_AND.getOperator());
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
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        List<T> resulet = new ArrayList<T>();
        List<Object> params = new ArrayList<Object>();
        List<String> colnum = new ArrayList<String>();
        getColnum(cla, colnum);
        String sqlcondition = creatCondition(condition, params);
        StringBuffer sb = new StringBuffer();
        sb.append(SQLOperator.SELECT.getOperator());
        for (String key : colnum) {
            sb.append(SQLOperator.BLANK.getOperator() + key + SQLOperator.COMMA.getOperator());
        }
        sb.deleteCharAt(sb.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sb.append(SQLOperator.FROM.getOperator() + simpleName + SQLOperator.BLANK.getOperator() + sqlcondition);
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
                            break;
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
            throw new JDBCExcption(ExceptionMsg.ERRO_PAGE.getMsg());
        }
        List<String> colnum = new ArrayList<String>();
        getColnum(cla, colnum);
        StringBuffer sb = new StringBuffer();
        sb.append(SQLOperator.SELECT.getOperator());
        for (String key : colnum) {
            sb.append(key + SQLOperator.COMMA.getOperator());
        }
        sb.deleteCharAt(sb.lastIndexOf(SQLOperator.COMMA.getOperator()));
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        sb.append(SQLOperator.FROM.getOperator() + simpleName);
        page = (page - 1) * pageSize;
        sb.append(SQLOperator.LIMIT.getOperator() + page + SQLOperator.COMMA.getOperator() + pageSize);
        List<Map<String, Object>> pages = BaseUtil.select(sb.toString());
        return parse(cla, pages);
    }


    public static <T> List<T> select(Class<T> cla, Map<String, SelectItem> condition, int page, int pageSize) {
        if (page < 1) {
            throw new JDBCExcption(ExceptionMsg.ERRO_PAGE.getMsg());
        }
        List<String> colnum = new ArrayList<String>();
        List<Object> params = new ArrayList<Object>();
        getColnum(cla, colnum);
        String where = creatCondition(condition, params);
        StringBuffer sb = new StringBuffer();
        sb.append(SQLOperator.SELECT.getOperator());
        for (String key : colnum) {
            sb.append(key + SQLOperator.COMMA.getOperator());
        }
        String simpleName = cla.getSimpleName();
        SQLTableName SQLTableName = cla.getAnnotation(SQLTableName.class);
        if (SQLTableName !=null){
            simpleName = SQLTableName.table();
        }
        sb.deleteCharAt(sb.lastIndexOf(SQLOperator.COMMA.getOperator()));
        sb.append(SQLOperator.FROM.getOperator() + simpleName);
        sb.append(SQLOperator.BLANK.getOperator() + where + SQLOperator.BLANK.getOperator());
        page = (page - 1) * pageSize;
        sb.append(SQLOperator.LIMIT.getOperator() + page + SQLOperator.COMMA.getOperator() + pageSize);
        List<Map<String, Object>> pages = BaseUtil.select(sb.toString(), params.toArray());
        return parse(cla, pages);
    }

    public static <T> List<T> select(T obj) {
        //定义结果集合
        List<T> result = new ArrayList<>();
        //对象的对应的数据库字段集合
        List<String> colnums = new ArrayList<>();
        //字段 - 值 map集合
        Map<String,Object> map = new HashMap<>();
        List<Object> params = new ArrayList<>();
        getColnum(obj, colnums, map);
        StringBuffer sql = new StringBuffer(SQLOperator.SELECT.getOperator());
        for (String column : colnums) {
            sql.append(column + SQLOperator.COMMA.getOperator());
        }
        sql.deleteCharAt(sql.length()-1);
        Class<?> aClass = obj.getClass();
        String table = aClass.getSimpleName();
        SQLTableName annotation = aClass.getAnnotation(SQLTableName.class);
        if (annotation != null) {
            table = annotation.table();
        }
        sql.append(SQLOperator.FROM.getOperator() + table);
        addParamsToSql(sql,map,params);
//        if (params.size() > 0) {
//            sql.append(SQLOperator.WHERE.getOperator());
//            Set<String> keySet = map.keySet();
//            for(String key :keySet){
//                sql.append(key + SQLOperator.EQUAL_PARAM_AND);
//                params.add(map.get(key));
//            }
//            sql.delete(sql.length()-4,sql.length());
//        }
        List<Map<String, Object>> select = BaseUtil.select(sql.toString(), params.toArray());
        return parse((Class<T>) aClass, select);
    }
    private static void addParamsToSql(StringBuffer sql,Map<String,Object> map,List<Object> params){
        if (map.size() > 0) {
            sql.append(SQLOperator.WHERE.getOperator());
            Set<String> keySet = map.keySet();
            for(String key :keySet){
                sql.append(key + SQLOperator.EQUAL_PARAM_AND.getOperator());
                params.add(map.get(key));
            }
            sql.delete(sql.length()-4,sql.length());
        }
    }
}
