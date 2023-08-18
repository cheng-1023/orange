package com.github.freakchick.orange.node;

import com.github.freakchick.orange.SqlMeta;
import com.github.freakchick.orange.User;
import com.github.freakchick.orange.engine.DynamicSqlEngine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: orange
 * @description:
 * @author: fuzi1996
 * @create: 2022/3/5
 **/
public class ForeachSqlNodeTest {

    @Test
    public void testEmptyIterableForeach() {
        DynamicSqlEngine engine = new DynamicSqlEngine();
        String sql = (
                "select * from user " +
                        "where name in " +
                        "<foreach collection='list' index='idx' open='(' separator=',' close=')'>" +
                            "#{item.name} == #{idx}" +
                            "<if test='id != null'>  " +
                                "and id = #{id}" +
                            "</if>" +
                        "</foreach>"
        );
        Map<String, Object> map = new HashMap<>();

        ArrayList<User> arrayList = new ArrayList<>();
        map.put("list", arrayList.toArray());
        map.put("id", 100);

        SqlMeta sqlMeta = engine.parse(sql, map);
        // System.out.println(sqlMeta.getSql());
        Assert.assertEquals("select * from user where name in ",sqlMeta.getSql());
        // sqlMeta.getJdbcParamValues().forEach(System.out::println);
        Assert.assertEquals(0,sqlMeta.getJdbcParamValues().size());
    }

    @Test
    public void testForeachWithDollarSign() {
        DynamicSqlEngine engine = new DynamicSqlEngine();
        String sql = (
                "select id, name from ( " +
                        "<foreach collection = 'data' item = 'item' index = 'index' separator = 'union all'>" +
                        "  select ${item.id} as id, '${item.name}' as name " +
                        "</foreach>" +
                        ") a"
        );
        Map<String, Object> map = new HashMap<>();
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        map.put("data", data);

        Map<String, Object> idMap = new HashMap<>();
        idMap.put("id", 1);
        idMap.put("name", "张三");
        data.add(idMap);

        idMap = new HashMap<>();
        idMap.put("id", 2);
        idMap.put("name", "李四");
        data.add(idMap);

        SqlMeta sqlMeta = engine.parse(sql, map);
        Assert.assertEquals("select id, name from (    select 1 as id, '张三' as name union all  select 2 as id, '李四' as name ) a",sqlMeta.getSql());
        Assert.assertEquals(0,sqlMeta.getJdbcParamValues().size());
    }
}