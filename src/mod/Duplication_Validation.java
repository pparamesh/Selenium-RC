package mod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import com.thoughtworks.selenium.DefaultSelenium;


@SuppressWarnings("unused")
public class Duplication_Validation {
    static int order_number=0;
    static int pass,fail, NA = 0;
    static String Order_Number = "";
    static DefaultSelenium selenium;
    static String[] Rolenames = {};
    static String[] MOD = {};
    static String[] Scene = {};
    static String[] Sub_Scene = {};
    static int count, r2, m2, s2, O = 0;

    public static void main (String arg[]) throws Exception {
        
        final String URL = "http://mod01.staging.customink.com";
        final String URL1 = "http://be02.staging.customink.com/backend/account/login";
        final String browser = "*googlechrome C:\\Users\\Paramesh_p\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe";
        //final String browser = "*firefox";
        //change browser
        selenium = new DefaultSelenium ("localhost", 4444, browser, URL1);
        selenium.start();
        
        selenium.open("/");
        selenium.windowMaximize();
        Thread.sleep(3000);
        Thread.sleep(4000);


        //Entering credentials to access application
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='login']\")", "1000000"); 
        selenium.type("login", "ppadmanabhan@customink.com");
        selenium.type("password", "Cust0m1nk2014"); 
        Thread.sleep(9000);
        selenium.click("commit");
        Thread.sleep(6000);
       
        selenium.open(URL);
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='feedback_search_submit']\")", "1000000"); 
       
        selenium.click("//a[text()='New MOD']");
        selenium.waitForCondition("selenium.isElementPresent(\"//input[@id='order_id']\")", "1000000");
       
        /*
         '999572' is a bulk order number
         To validate for Singles order number, replace 999572 with a valid singles order
         For No order ID scenario, comment the next 2 lines and proceed
         */
        selenium.type("order_id","999572");
        selenium.waitForCondition("selenium.isElementPresent(\"//div[@class='feedback_summary']\")", "1000000");
        selenium.type("feedback_details", "Test MOD, please ignore");
        Duplicate(O);
   }
 
    private static void Duplicate(int o2) throws InterruptedException {
        String[] Rolenames = selenium.getSelectOptions("//select[@id='feedback_feedback_targets_1_order_role_id']");
        for (r2 = 1; r2 < Rolenames.length; r2++)
        {
            selenium.select("//select[@id='feedback_feedback_targets_1_order_role_id']", Rolenames[r2]);
            Thread.sleep(3000);
            selenium.waitForCondition("selenium.isElementPresent(\"feedback_feedback_targets_1_feedback_for_id\")", "90000");
            
            String[] MOD = selenium.getSelectOptions("//select[@id='feedback_feedback_type_id']");
            for (m2 = 1; m2 < MOD.length; m2++)
            {
                selenium.select("feedback_feedback_type_id",MOD[m2]);
                Thread.sleep(3000);
                selenium.waitForCondition("selenium.isElementPresent(\"feedback_situation_type_id\")", "90000");
                
                String[] Scene = selenium.getSelectOptions("//select[@id='feedback_situation_type_id']");
                for (s2 = 1; s2 < Scene.length; s2++)
                {
                    selenium.select("feedback_situation_type_id", Scene[s2]);
                    Thread.sleep(3000);

                    if (selenium.isElementPresent("//select[@id='list_sub_scene']"))
                    {
                        String[] Sub_Scene = selenium.getSelectOptions("//select[@id='list_sub_scene']");
                        for ( count = 1;  count < (Sub_Scene.length)-1 && !Sub_Scene[count+1].equalsIgnoreCase("Other");  count++ )
                             { 
                       
                        		if ( Sub_Scene[count].compareTo(Sub_Scene[count+1]) < 0)
                                   pass++;
                        		else
	                              {
	                            	  System.out.println("Fail");
	                            	  System.out.println("Role: "+Rolenames[r2]);
	                            	  System.out.println("MOD: "+MOD[m2]);
	                            	  System.out.println("Scene: "+Scene[s2]);
	                            	  System.out.println("Sub_Scene: "+Sub_Scene[count]);
	                            	  fail++;
	                              }
                         
                              }
           
                    }
                else
                {
	                System.out.println("Sub_Scene not available for the following combination");
	                System.out.println("Role: "+Rolenames[r2]);
	                System.out.println("MOD: "+MOD[m2]);
	                System.out.println("Scene: "+Scene[s2]);
	                NA++;
                }    
                }
		          System.out.println("Passed Combinations: "+pass);
		          System.out.println("Failed Combination: "+fail);
		          System.out.println("Sub_Scene Unavailable Combinations: "+NA);
            }
        }
    }
}
