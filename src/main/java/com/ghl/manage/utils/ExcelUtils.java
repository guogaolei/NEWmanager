package com.ghl.manage.utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtils {

    public static void praseExcel(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            String id = row.getCell(0).toString();//序号
            String  no= row.getCell(1).toString();//编号
            String  position= row.getCell(2).toString();//区域
            String  type= row.getCell(3).toString();//类型
            String  x= row.getCell(4).toString();//
            String  y= row.getCell(5).toString();//
            String  layer= row.getCell(6).toString();//层位
            StringBuffer sb=new StringBuffer();
            sb.append("INSERT INTO water_ground(NO,POSITION,TYPE,X,Y,layer) VALUES('");
            sb.append(no);sb.append("','");
            sb.append(position);sb.append("','");
            sb.append(type);sb.append("','");
            sb.append(x);sb.append("','");
            sb.append(y);sb.append("','");
            sb.append(layer);sb.append("');");
            System.out.println(sb.toString());
        }
        workbook.close();
    }

    public static void main(String[] args) throws IOException {
        FileInputStream in=new FileInputStream("D:\\桌面\\zyf\\01地下水\\01地下水\\地下水监测位置.xlsx");
        praseExcel(in);
    }

}
