package com.cn.ccj.springboot03;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cn.ccj.springboot03.entity.ExcelUser;
import com.cn.ccj.springboot03.entity.ExcelUserIdentity;
import com.cn.ccj.springboot03.entity.User;
import com.cn.ccj.springboot03.generalException.GeneralException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jiangcongcong
 * @date 2021/5/19 19:24
 */

@SpringBootTest
public class EasyPoiTest {

    @Test
    public void test() throws Exception {
        String title = "用户信息表";
        String sheetName = "用户信息";
        ExportParams exportParams = new ExportParams(title, sheetName);
        ExcelUser user = new ExcelUser();
        user.setId("0001");
        user.setAge(20);
        user.setName("cc");
        user.setGender("man");
        ExcelUserIdentity excelUserIdentity = new ExcelUserIdentity();
        excelUserIdentity.setIdCardNum("123456789123456789");
        excelUserIdentity.setNativePlace("湖北武汉");
        user.setExcelUserIdentity(excelUserIdentity);
        List<ExcelUser> userList = new ArrayList<>();
        userList.add(user);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,ExcelUser.class, userList);
        workbook.write(new FileOutputStream("C:\\Users\\JIANGCONGCONG\\Desktop\\test.xlsx"));
        workbook.close();
    }

}
