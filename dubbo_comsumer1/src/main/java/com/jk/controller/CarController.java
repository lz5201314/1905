/**
 * Copyright (C), 2019, 金科教育
 * FileName: CarController
 * Author:   zyl
 * Date:     2019/11/4 10:32
 * History:
 * zyl          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.jk.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jk.model.Tree;


import com.jk.service.CarService;
import com.jk.util.ExportExcel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈a〉
 *
 * @author zyl
 * @create 2019/11/4
 * @since 1.0.0
 */
@Controller
//@RestController   controller+ResponseBody  把当前类的所有返回值当做json返回
public class CarController {

    @Reference
    private CarService carService;

    @RequestMapping("queryquanAll")
    @ResponseBody
    public Map<String, Object> queryquanAll(Tree t, int page, int rows) {
        return carService.queryquanAll(t, page, rows);

    }




    @RequestMapping("toAddtree")
    public String toAdddai() {
        return "addtree";
    }


    @RequestMapping("addtree")
    @ResponseBody
    public void addtree(Tree t) {
        carService.addtree(t);

    }


    @RequestMapping("totree")
    public String totree(Tree t, Model model) {
        t = carService.querytreeById(t.getId());
        model.addAttribute("dai", t);
        return "updatetree";
    }

    @RequestMapping("updatetree")
    @ResponseBody
    public void updatetree(Tree t) {
        carService.updatetree(t);

    }


    @RequestMapping("removeAlltree")
    @ResponseBody
    public int removeAlltree(String ids) {
        int n = carService.removeAlltree(ids);
        return n;
    }

    @RequestMapping("queryquanA")
    @ResponseBody
    public List<Tree> queryquanAll2() {
       return   carService.queryquanAll2();

    }


    @RequestMapping("export")
    public void export(HttpServletResponse response,String[] id){

        if (id.length<=0 || id!=null){
            List<Tree> list= new ArrayList<Tree>();
            try {

                list = (List<Tree>) carService.queryquanAll2();

                //定义表格的标题
                String title ="权限信息";
                //定义列名
                String[] rowName={"id","text","pid","Iconcls","Url","Children"};
                //定义工具类要的数据
                List<Object[]>  dataList = new ArrayList<Object[]>();




                //循环数据把数据存放到 List<Object[]>
                for (Tree car:list) {
                    Object[] obj=new Object[rowName.length];
                    obj[0]=car.getId();
                    obj[1]= car.getText();
                    obj[2]=car.getPid();
                    obj[3]=car.getIconcls();
                    obj[4]=car.getUrl();
                    obj[5]=car.getChildren();



                    dataList.add(obj);
                }
                ExportExcel exportExcel=new ExportExcel(title,rowName,dataList,response);
                exportExcel.export();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            List<Tree> list= new ArrayList<Tree>();
            try {

                list = (List<Tree>) carService.queryquanAll3(id);

                //定义表格的标题
                String title ="权限信息";
                //定义列名
                String[] rowName={"id","text","pid","Iconcls","Url","Children"};
                //定义工具类要的数据
                List<Object[]>  dataList = new ArrayList<Object[]>();




                //循环数据把数据存放到 List<Object[]>
                for (Tree car:list) {
                    Object[] obj=new Object[rowName.length];
                    obj[0]=car.getId();
                    obj[1]= car.getText();
                    obj[2]=car.getPid();
                    obj[3]=car.getIconcls();
                    obj[4]=car.getUrl();
                    obj[5]=car.getChildren();



                    dataList.add(obj);
                }
                ExportExcel exportExcel=new ExportExcel(title,rowName,dataList,response);
                exportExcel.export();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @RequestMapping("importExcel")
    public String importExcel(MultipartFile file, HttpServletResponse response){
        //上传文件的名称
        String fileName = file.getOriginalFilename();
        //生成新的文件名称
        String filePath = "./src/main/resources/templates/fileupload/";

        //创建list集合接收传递的参数
        List<Tree> list= new ArrayList<Tree>();

        //上传文件
        try {
            uploadFile(file.getBytes(), filePath, fileName);


            //通过文件创建流
            FileInputStream fileInputStream = new FileInputStream(filePath+fileName);
            //创建操作excel的对象   因为xls的文件需要HSSFWorkbook  xlsx需要的XSSFWorkbook 因此直接使用workBook对象就行了
            Workbook workbook= WorkbookFactory.create(fileInputStream) ;
            //通过workbook获得sheet页  sheet有可能有多个
            for(int i=0;i<workbook.getNumberOfSheets();i++){
                //创建sheet对象
                Sheet sheetAt = workbook.getSheetAt(i);
                //根绝sheet创建行  row
                for(int j=3;j<sheetAt.getPhysicalNumberOfRows();j++){
                    //创建row对象
                    Row row = sheetAt.getRow(j);

                    //创建对象接收从文件读取的内容
                    Tree car=new Tree();
                    if( !"".equals(row.getCell(1)) && row.getCell(1)!=null){
                        car.setChildren(row.getCell(1).toString());
                    }
                    if( !"".equals(row.getCell(2)) && row.getCell(2)!=null){
                        car.setIconcls(row.getCell(2).toString());
                    }
                    if( !"".equals(row.getCell(3)) && row.getCell(3)!=null){
                        car.setText(Double.parseDouble(row.getCell(3).toString()));
                    }
                    if( !"".equals(row.getCell(4)) && row.getCell(4)!=null){
                        car.setUrl((row.getCell(4).toString()));
                    }


                    list.add(car);
                }

            }
            carService.addtree((Tree) list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    //上传文件的方法
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }


    @RequestMapping("queryVeiw")
    @ResponseBody
    public List<Map<String,Object>> queryVeiw(){
        List<Map<String,Object>>  map1 = carService.queryVeiw();
        List<Map<String,Object>>  map2=new ArrayList<Map<String,Object>>();

        for(Map<String,Object> map:map1){
            Map<String,Object> map3=new HashMap<>();
            map3.put("y",map.get("y"));
            map3.put("name",map.get("typename"));
            map2.add(map3);
        }
        return map2;
    }


}
