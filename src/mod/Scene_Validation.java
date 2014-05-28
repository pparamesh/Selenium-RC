package mod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.thoughtworks.selenium.DefaultSelenium;

@SuppressWarnings("unused")
public class Scene_Validation {

    static int order_number=0;
    static String Order_Number = "";
    static DefaultSelenium selenium;
    
    static int i=1;
    
    //"j" is the row limit till which you need to run the scripts
    static int j = 30; 
    static int O = 0;
    static Workbook wb;
    static Sheet s;  

    static{
        FileInputStream FIS; 
        try {
            FIS = new FileInputStream("C:\\Users\\Paramesh_p\\Desktop\\Paramesh\\Selenium\\Scenes_Validation_1.xls");
            wb = Workbook .getWorkbook(FIS);
        } catch (FileNotFoundException e) { 
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (BiffException | IOException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }       
        s= wb.getSheet(0);
    }
    
    public static void main (String arg[]) throws Exception {
    	
        final String URL = "http://mod01.staging.customink.com/";
        final String URL1 = "http://be02.staging.customink.com/backend/account/login/"; 
        final String browser = "*googlechrome C:\\Users\\Paramesh_p\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe";
        //final String browser = "*firefox";
        selenium = new DefaultSelenium ("localhost", 4444, browser, URL1);
        selenium.start();
        
        selenium.open("/");
        selenium.windowMaximize();
        Thread.sleep(3000);

        //Entering credentials to access application

        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='login']\")", "1000000"); 
        selenium.type("login", "ppadmanabhan@customink.com");
        selenium.type("password", "Cust0m1nk2014");
        selenium.click("commit");
        Thread.sleep(3000);
       

        selenium.open(URL);
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='feedback_search_submit']\")", "1000000"); 
       
        selenium.click("//a[text()='New MOD']");
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='order_id']\")", "1000000");
       
        for (i=1; i <= j; i++)
            {
            selenium.type("order_id",s.getCell(0,i).getContents()); 
            selenium.waitForCondition("selenium.isElementPresent(\"//div[@class='feedback_summary']\")", "1000000");
            Order_Number = selenium.getText("order_id"); 
            
            selenium.type("feedback_details", "Test MOD, please ignore : " +s.getCell(0,i).getContents());
            char OT = s.getCell(1,i).getContents().charAt(0);
            switch (OT){
            case '1':
                O = Integer.parseInt(s.getCell(0,i).getContents());
                Process(O);
                break;
                
            case '2':
                break;
             
            case '3':
                O = Integer.parseInt(s.getCell(0,i).getContents());
                Process(O);
                break;
            }
        }

    }

 
    private static void Process(int o2) throws InterruptedException {
 
        selenium.waitForCondition("selenium.isElementPresent(\"//div[@id='related_feedback']\")", "1000000");
        String Role = s.getCell(2,i).getContents();
      
        selenium.select("feedback_feedback_targets_1_order_role_id", s.getCell(2,i).getContents());
		Thread.sleep(3000);
        
        if (selenium.isVisible("feedback_feedback_targets_1_feedback_for_id"))
        {
        	selenium.waitForCondition("selenium.isElementPresent(\"//select[@id='feedback_feedback_targets_1_feedback_for_id']\")", "100000");
        	selenium.select("feedback_feedback_targets_1_feedback_for_id", s.getCell(6,i).getContents());
        }
        
        else
        {
        	selenium.select("feedback_feedback_targets_2_feedback_for_id", s.getCell(6,i).getContents());
        	Thread.sleep(2000);
        	selenium.select("feedback_feedback_targets_3_feedback_for_id", s.getCell(7,i).getContents());
        	Thread.sleep(2000);
        }
      
        selenium.select("//select[@id='feedback_feedback_type_id']", s.getCell(3,i).getContents());
        Thread.sleep(3000);
        selenium.select("//select[@id='feedback_situation_type_id']", s.getCell(4,i).getContents());
        String Sub_Scenes = s.getCell(5,i).getContents();
        if(Sub_Scenes.length() != 0)
        {
            selenium.waitForCondition("selenium.isElementPresent(\"//select[@id='list_sub_scene']\")", "1000000");
            selenium.select("//select[@id='list_sub_scene']", s.getCell(5,i).getContents());
        }
        

        if(selenium.isVisible("feedback_affects_units"))
            selenium.type("feedback_affects_units", "0");
        
        //Submitting the MOD to check whether the entered values are displayed correctly in MOD Summary screen
        selenium.click("feedback_submit");
        selenium.waitForCondition("selenium.isElementPresent(\"//a[text()='Additional MOD']\")","1000000");
        
        selenium.click("//a[text()='New MOD']");
        System.out.println("Row No : " +i);
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='order_id']\")", "1000000");

    }

}




