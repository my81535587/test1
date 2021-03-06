package com.spring.controller;

import com.spring.dao.TikuMapper;
import com.spring.entity.Tiku;
import com.spring.service.TikuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;
import util.Request;
import util.Info;
import dao.Query;

import java.util.*;

import dao.CommDAO;


/**
 * 题库
 */
@Controller
public class TikuController extends BaseController {
    @Autowired
    private TikuMapper dao;
    @Autowired
    private TikuService service;

    /**
     * 后台列表页
     */
    @RequestMapping("/tiku_list")
    public String list() {

        // 检测是否有登录，没登录则跳转到登录页面
        if (!checkLogin()) {
            return showError("尚未登录", "./login.do");
        }

        String order = Request.get("order", "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort = Request.get("sort", "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc
        int pagesize = Request.getInt("pagesize", 12); // 获取前台一页多少行数据
        Example example = new Example(Tiku.class); //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();          // 创建一个扩展搜索条件类
        String where = " 1=1 ";   // 创建初始条件为：1=1
        where += getWhere();      // 从方法中获取url 上的参数，并写成 sql条件语句
        criteria.andCondition(where);   // 将条件写进上面的扩展条件类中
        if (sort.equals("desc")) {        // 判断前台提交的sort 参数是否等于  desc倒序  是则使用倒序，否则使用正序
            example.orderBy(order).desc();  // 把sql 语句设置成倒序
        } else {
            example.orderBy(order).asc();   // 把 sql 设置成正序
        }
        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));  // 获取前台提交的URL参数 page  如果没有则设置为1
        page = Math.max(1, page);  // 取两个数的最大值，防止page 小于1
        List<Tiku> list = service.selectPageExample(example, page, pagesize);   // 获取当前页的行数


        // 将列表写给界面使用
        assign("totalCount", request.getAttribute("totalCount"));
        assign("list", list);
        assign("orderby", order);  // 把当前排序结果写进前台
        assign("sort", sort);      // 把当前排序结果写进前台
        return json();   // 将数据写给前端
    }

    public String getWhere() {
        _var = new LinkedHashMap(); // 重置数据
        String where = " ";
        // 以下也是一样的操作，判断是否符合条件，符合则写入sql 语句
        if (!Request.get("tikumingcheng").equals("")) {
            where += " AND tikumingcheng LIKE '%" + Request.get("tikumingcheng") + "%' ";
        }
        return where;
    }

    /**
     * 发布人列表
     */
    @RequestMapping("/tiku_list_faburen")
    public String listfaburen() {
        // 检测是否有登录，没登录则跳转到登录页面
        if (!checkLogin()) {
            return showError("尚未登录", "./login.do");
        }
        String order = Request.get("order", "id"); // 获取前台提交的URL参数 order  如果没有则设置为id
        String sort = Request.get("sort", "desc"); // 获取前台提交的URL参数 sort  如果没有则设置为desc
        int pagesize = Request.getInt("pagesize", 12); // 获取前台一页多少行数据


        Example example = new Example(Tiku.class);  //  创建一个扩展搜索类
        Example.Criteria criteria = example.createCriteria();           // 创建一个扩展搜索条件类
        // 初始化一个条件，条件为：发布人=当前登录用户
        String where = " faburen='" + request.getSession().getAttribute("username") + "' ";
        where += getWhere();

        criteria.andCondition(where);   // 将条件写入
        if (sort.equals("desc")) {        // 注释同list
            example.orderBy(order).desc(); // 注释同list
        } else {
            example.orderBy(order).asc(); // 注释同list
        }

        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page")); // 注释同list
        page = Math.max(1, page); // 注释同list

        List<Tiku> list = service.selectPageExample(example, page, pagesize);
        assign("totalCount", request.getAttribute("totalCount"));
        assign("list", list);
        assign("orderby", order);
        assign("sort", sort);
        return json();   // 将数据写给前端
    }

    /**
     * 前台列表页
     */
    @RequestMapping("/tikulist")
    public String index() {
        String order = Request.get("order", "id");
        String sort = Request.get("sort", "desc");

        Example example = new Example(Tiku.class);
        Example.Criteria criteria = example.createCriteria();
        String where = " 1=1 ";
        where += getWhere();
        criteria.andCondition(where);
        if (sort.equals("desc")) {
            example.orderBy(order).desc();
        } else {
            example.orderBy(order).asc();
        }
        int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
        page = Math.max(1, page);
        List<Tiku> list = service.selectPageExample(example, page, 12);

        assign("totalCount", request.getAttribute("totalCount"));
        assign("list", list);
        assign("where", where);
        assign("orderby", order);
        assign("sort", sort);
        return json();
    }


    @RequestMapping("/tiku_add")
    public String add() {
        _var = new LinkedHashMap(); // 重置数据

        assign("jiaoshiList", new CommDAO().select("SELECT * FROM jiaoshi ORDER BY id desc"));
        return json();   // 将数据写给前端
    }

    @RequestMapping("/tiku_updt")
    public String updt() {
        _var = new LinkedHashMap(); // 重置数据
        int id = Request.getInt("id");
        // 获取行数据，并赋值给前台jsp页面
        Tiku mmm = service.find(id);
        assign("mmm", mmm);
        assign("updtself", 0);

        assign("jiaoshiList", new CommDAO().select("SELECT * FROM jiaoshi ORDER BY id desc"));
        return json();   // 将数据写给前端
    }

    /**
     * 添加内容
     *
     * @return
     */
    @RequestMapping("/tikuinsert")
    public String insert() {
        _var = new LinkedHashMap(); // 重置数据
        String tmp = "";
        Tiku post = new Tiku();  // 创建实体类
        // 设置前台提交上来的数据到实体类中
        post.setTikubianhao(Request.get("tikubianhao"));

        post.setTikumingcheng(Request.get("tikumingcheng"));

        post.setJiaoshi(Request.get("jiaoshi"));

        post.setFaburen(Request.get("faburen"));


        post.setAddtime(Info.getDateStr()); // 设置添加时间
        service.insert(post); // 插入数据
        int charuid = post.getId().intValue();

        return showSuccess("保存成功", Request.get("referer").equals("") ? request.getHeader("referer") : Request.get("referer"));
    }

    /**
     * 更新内容
     *
     * @return
     */
    @RequestMapping("/tikuupdate")
    public String update() {
        _var = new LinkedHashMap(); // 重置数据
        // 创建实体类
        Tiku post = new Tiku();
        // 将前台表单数据填充到实体类
        if (!Request.get("tikubianhao").equals(""))
            post.setTikubianhao(Request.get("tikubianhao"));
        if (!Request.get("tikumingcheng").equals(""))
            post.setTikumingcheng(Request.get("tikumingcheng"));
        if (!Request.get("jiaoshi").equals(""))
            post.setJiaoshi(Request.get("jiaoshi"));
        if (!Request.get("faburen").equals(""))
            post.setFaburen(Request.get("faburen"));

        post.setId(Request.getInt("id"));
        service.update(post); // 更新数据
        int charuid = post.getId().intValue();
        return showSuccess("保存成功", Request.get("referer")); // 弹出保存成功，并跳转到前台提交的 referer 页面
    }

    /**
     * 后台详情
     */
    @RequestMapping("/tiku_detail")
    public String detail() {
        _var = new LinkedHashMap(); // 重置数据
        int id = Request.getInt("id");
        Tiku map = service.find(id);  // 根据前台url 参数中的id获取行数据
        assign("map", map);  // 把数据写到前台
        return json();   // 将数据写给前端
    }

    /**
     * 前台详情
     */
    @RequestMapping("/tikudetail")
    public String detailweb() {
        _var = new LinkedHashMap(); // 重置数据
        int id = Request.getInt("id");
        Tiku map = service.find(id);
        assign("prev", new CommDAO().find("SELECT * FROM tiku WHERE id<'" + request.getParameter("id") + "' ORDER BY id desc"));
        assign("next", new CommDAO().find("SELECT * FROM tiku WHERE id>'" + request.getParameter("id") + "' ORDER BY id desc"));


        List<HashMap> wenda = Query.make("shiti").where("tikuid",id)
                .where("leixing" , "单选题")
                .limit("10").order("rand()").select();


        wenda.addAll(Query.make("shiti")
                .where("tikuid",id).where("leixing","多选题")
                .limit("10")
                .order("rand()")
                .select());

        assign("webda" , wenda);   //004


        assign("map", map);
        return json();   // 将数据写给前端
    }

    /**
     * 删除
     */
    @RequestMapping("/tiku_delete")
    public String delete() {
        _var = new LinkedHashMap(); // 重置数据
        if (!checkLogin()) {
            return showError("尚未登录");
        }
        int id = Request.getInt("id");  // 根据id 删除某行数据
        HashMap map = Query.make("tiku").find(id);

        service.delete(id);// 根据id 删除某行数据
        return showSuccess("删除成功", request.getHeader("referer"));//弹出删除成功，并跳回上一页
    }
}
