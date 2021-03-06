package com.spring.controller;

import com.spring.dao.TiezihuifuMapper;
import com.spring.entity.Tiezihuifu;
import com.spring.service.TiezihuifuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;
import util.Request;
import util.Info;
import dao.Query;
import java.util.*;
import dao.CommDAO;

import com.spring.entity.Tiezi;
import com.spring.service.TieziService;

/**
 * 帖子回复 */
@Controller
public class TiezihuifuController extends BaseController
{
    @Autowired
    private TiezihuifuMapper dao;
    @Autowired
    private TiezihuifuService service;

    @Autowired
    private TieziService serviceRead;
    /**
     *  后台列表页
     *
     */
    @RequestMapping("/tiezihuifu_list")
    public String list()
    {

        // 检测是否有登录，没登录则跳转到登录页面
        if(!checkLogin()){
            return showError("尚未登录" , "./login.do");
        }

        String order = Request.get("order" , "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort  = Request.get("sort" , "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc
        int    pagesize = Request.getInt("pagesize" , 12); // 获取前台一页多少行数据
        Example example = new Example(Tiezihuifu.class); //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();          // 创建一个扩展搜索条件类
        String where = " 1=1 ";   // 创建初始条件为：1=1
        where += getWhere();      // 从方法中获取url 上的参数，并写成 sql条件语句
        criteria.andCondition(where);   // 将条件写进上面的扩展条件类中
        if(sort.equals("desc")){        // 判断前台提交的sort 参数是否等于  desc倒序  是则使用倒序，否则使用正序
            example.orderBy(order).desc();  // 把sql 语句设置成倒序
        }else{
            example.orderBy(order).asc();   // 把 sql 设置成正序
        }
        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));  // 获取前台提交的URL参数 page  如果没有则设置为1
        page = Math.max(1 , page);  // 取两个数的最大值，防止page 小于1
        List<Tiezihuifu> list = service.selectPageExample(example , page , pagesize);   // 获取当前页的行数


                    assign("tiezifenleiList" , new CommDAO().select("SELECT * FROM tiezifenlei ORDER BY id desc"));
        // 将列表写给界面使用
        assign("totalCount" , request.getAttribute("totalCount"));
        assign("list" , list);
        assign("orderby" , order);  // 把当前排序结果写进前台
        assign("sort" , sort);      // 把当前排序结果写进前台
        return json();   // 将数据写给前端
    }

    public String getWhere()
    {
        _var = new LinkedHashMap(); // 重置数据
        String where = " ";
        // 判断URL 参数tieziid是否大于0
        if( Request.getInt("tieziid") > 0 ) {
            // 大于0 则写入条件
            where += " AND tieziid='"+Request.getInt("tieziid")+"' ";
        }
        // 以下也是一样的操作，判断是否符合条件，符合则写入sql 语句
            if(!Request.get("tiezibianhao").equals("")) {
            where += " AND tiezibianhao LIKE '%"+Request.get("tiezibianhao")+"%' ";
        }
                if(!Request.get("biaoti").equals("")) {
            where += " AND biaoti LIKE '%"+Request.get("biaoti")+"%' ";
        }
                if(!Request.get("fenlei").equals("")) {
            where += " AND fenlei ='"+Request.get("fenlei")+"' ";
        }
                if(!Request.get("huifuneirong").equals("")) {
            where += " AND huifuneirong LIKE '%"+Request.get("huifuneirong")+"%' ";
        }
                if(!Request.get("huifuren").equals("")) {
            where += " AND huifuren LIKE '%"+Request.get("huifuren")+"%' ";
        }
            return where;
    }

    /**
     * 回复人列表
     */
    @RequestMapping("/tiezihuifu_list_huifuren")
    public String listhuifuren()
    {
        // 检测是否有登录，没登录则跳转到登录页面
        if(!checkLogin()){
            return showError("尚未登录" , "./login.do");
        }
        String order = Request.get("order" , "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort  = Request.get("sort" , "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc
        int    pagesize = Request.getInt("pagesize" , 12); // 获取前台一页多少行数据

        
        Example example = new Example(Tiezihuifu.class);  //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();           // 创建一个扩展搜索条件类
        // 初始化一个条件，条件为：回复人=当前登录用户
        String where = " huifuren='"+request.getSession().getAttribute("username")+"' ";
        where += getWhere();

        criteria.andCondition(where);   // 将条件写入
        if(sort.equals("desc")){        // 注释同list
            example.orderBy(order).desc(); // 注释同list
        }else{
            example.orderBy(order).asc(); // 注释同list
        }

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page")); // 注释同list
        page = Math.max(1 , page); // 注释同list

            List<Tiezihuifu> list = service.selectPageExample(example , page , pagesize);
                        assign("tiezifenleiList" , new CommDAO().select("SELECT * FROM tiezifenlei ORDER BY id desc"));        assign("totalCount" , request.getAttribute("totalCount"));
        assign("list" , list);
                assign("orderby" , order);
        assign("sort" , sort);
        return json();   // 将数据写给前端
    }




        @RequestMapping("/tiezihuifu_add")
    public String add()
    {
        _var = new LinkedHashMap(); // 重置数据
        int id = Request.getInt("id");  // 根据id 获取 帖子模块中的数据
        Tiezi readMap = serviceRead.find(id);
        // 将数据行写入给前台jsp页面
        assign("readMap" , readMap);

        
        return json();   // 将数据写给前端
    }

    @RequestMapping("/tiezihuifu_updt")
    public String updt()
    {
        _var = new LinkedHashMap(); // 重置数据
        int id = Request.getInt("id");
        // 获取行数据，并赋值给前台jsp页面
        Tiezihuifu mmm = service.find(id);
        assign("mmm" , mmm);
        assign("updtself" , 0);

        
        return json();   // 将数据写给前端
    }
    /**
     * 添加内容
     * @return
     */
    @RequestMapping("/tiezihuifuinsert")
    public String insert()
    {
        _var = new LinkedHashMap(); // 重置数据
        String tmp="";
        Tiezihuifu post = new Tiezihuifu();  // 创建实体类
        // 设置前台提交上来的数据到实体类中
        post.setTiezibianhao(Request.get("tiezibianhao"));

        post.setBiaoti(Request.get("biaoti"));

        post.setFenlei(Request.get("fenlei"));

        post.setHuifuneirong(util.DownloadRemoteImage.run(Request.get("huifuneirong")));

        post.setHuifuren(Request.get("huifuren"));

        post.setTieziid(Request.getInt("tieziid"));

        
        post.setAddtime(Info.getDateStr()); // 设置添加时间
                service.insert(post); // 插入数据
        int charuid = post.getId().intValue();
        
        return showSuccess("保存成功" , Request.get("referer").equals("") ? request.getHeader("referer") : Request.get("referer"));
    }

    /**
    * 更新内容
    * @return
    */
    @RequestMapping("/tiezihuifuupdate")
    public String update()
    {
        _var = new LinkedHashMap(); // 重置数据
        // 创建实体类
        Tiezihuifu post = new Tiezihuifu();
        // 将前台表单数据填充到实体类
        if(!Request.get("tiezibianhao").equals(""))
        post.setTiezibianhao(Request.get("tiezibianhao"));
                if(!Request.get("biaoti").equals(""))
        post.setBiaoti(Request.get("biaoti"));
                if(!Request.get("fenlei").equals(""))
        post.setFenlei(Request.get("fenlei"));
                if(!Request.get("huifuneirong").equals(""))
        post.setHuifuneirong(util.DownloadRemoteImage.run(Request.get("huifuneirong")));
            if(!Request.get("huifuren").equals(""))
        post.setHuifuren(Request.get("huifuren"));
        
        post.setId(Request.getInt("id"));
                service.update(post); // 更新数据
        int charuid = post.getId().intValue();
                        return showSuccess("保存成功" , Request.get("referer")); // 弹出保存成功，并跳转到前台提交的 referer 页面
    }
        /**
    *  删除
    */
    @RequestMapping("/tiezihuifu_delete")
    public String delete()
    {
        _var = new LinkedHashMap(); // 重置数据
        if(!checkLogin()){
            return showError("尚未登录");
        }
        int id = Request.getInt("id");  // 根据id 删除某行数据
        HashMap map = Query.make("tiezihuifu").find(id);

                service.delete(id);// 根据id 删除某行数据
                return showSuccess("删除成功",request.getHeader("referer"));//弹出删除成功，并跳回上一页
    }
}
