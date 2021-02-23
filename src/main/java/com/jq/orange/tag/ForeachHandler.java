package com.jq.orange.tag;

import com.jq.orange.node.ForeachSqlNode;
import com.jq.orange.node.MixedSqlNode;
import com.jq.orange.node.SqlNode;
import org.dom4j.Element;

import java.util.List;

/**
 * @program: orange
 * @description:
 * @author: jiangqiang
 * @create: 2021-02-22 17:27
 **/
public class ForeachHandler implements TagHandler {
    @Override
    public void handle(Element element, List<SqlNode> targetContents) {
        List<SqlNode> contents = XmlParser.parseElement(element);

        String open = element.attributeValue("open");
        String close = element.attributeValue("close");
        String collection = element.attributeValue("collection");
        String separator = element.attributeValue("separator");
        String item = element.attributeValue("item");
        String index = element.attributeValue("index");

        ForeachSqlNode foreachSqlNode = new ForeachSqlNode(collection, open, close, separator, item, index, new MixedSqlNode(contents));
        targetContents.add(foreachSqlNode);

    }
}
