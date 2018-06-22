package cn.fly.redis;

import cn.fly.model.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-data-solr
 * @description:
 * @author: Arctic_Xiong
 * @create: 2018-06-09 22:10
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-solr.xml")
public class SpringDataSolrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 增加测试
     */
    @Test
    public void testAdd(){
        Item item = new Item();
        item.setId(2333L);
        item.setBrand("华ew为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号we专卖店111");
        item.setTitle("华为Mawete9111");
        item.setPrice(new BigDecimal(3000));
        Item item1 = new Item();
        item1.setId(22L);
        item1.setBrand("华2为");
        item1.setCategory("手2机");
        item1.setGoodsId(21L);
        item1.setSeller("华为22号专卖店111");
        item1.setTitle("华为2Mate9111");
        item1.setPrice(new BigDecimal(23000));

        List<Item> items = new ArrayList<Item>();
        items.add(item);
        items.add(item1);
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    /**
     * 根据id查询
     */
    @Test
    public void testGetById(){
        Item item = solrTemplate.getById("1", Item.class);
        System.out.println(item);
    }

    /***
     * 根据主键删除
     */
    @Test
    public void testDeleteById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    /***
     * 分页查询
     */
    @Test
    public void testPageSearch(){
        Query query = new SimpleQuery("*:*");
        //设置分页
        query.setOffset(10); //开始索引(默认0)
        query.setRows(15);   //每页记录数(默认10)

        //分页查询
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        //总记录数
        System.out.println("总记录数:"+items.getTotalElements());

        //输出记录
        for (Item item : items.getContent()) {
            System.out.println(item);
        }
    }

    /***
     * 条件查询
     */
    @Test
    public void testPageSearchMutil(){
        Query query = new SimpleQuery("*:*");

        //封装查询条件
        Criteria criteria = new Criteria("item_seller").contains("111");

        //把Criteria加入到Query中
        query.addCriteria(criteria);
        //设置分页
        query.setOffset(0); //开始索引(默认0)
        query.setRows(15);   //每页记录数(默认10)

        //分页查询
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        //总记录数
        System.out.println("总记录数:"+items.getTotalElements());

        //输出记录
        for (Item item : items.getContent()) {
            System.out.println(item);
        }
    }
    /***
     * 全部删除
     */
    @Test
    public void testDeleteAll(){
        Query query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}