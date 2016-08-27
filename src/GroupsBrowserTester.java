import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Created by nickwilson on 1/22/16.
 */
public class GroupsBrowserTester {

    public static void main(String[] args) {
        String testResults = checkGroupsBrowsers();
        log(testResults);
        emailIfFailures(testResults);
    }

    private static void log(String testResults) {
        try{
            Calendar cal = Calendar.getInstance();
            String date = new SimpleDateFormat("MMM_YYYY").format(cal.getTime());

            File file = new File("//home//ouit0196//groups_browser_test//groups_browser_checker_results_" + date + ".csv");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.newLine();
            bufferWriter.write(testResults);
            bufferWriter.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String checkGroupsBrowsers() {

        // initialise new JS-enabled webdriver
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);

        // login
        driver.get("https://weblearn.ox.ac.uk/portal/xlogin?returnPath=%2F");
        WebElement element = driver.findElement(By.name("eid"));
        element.sendKeys("dashing");
        element = driver.findElement(By.name("pw"));
        element.sendKeys("dashDASH123");
        element.submit();

        // check tree browsers
        driver.get("https://weblearn.ox.ac.uk/portal/hierarchy/261b9bd5-9a08-4be1-b0de-2bb39d3d7543/page/site_info");
        driver.switchTo().frame("Mainbb447cc4x7b6cx4bb9xbfcdx60888ca4089b");
        WebElement button = driver.findElement(By.name("eventSubmit_doExternalGroupsHelper"));
        button.click();

        String testResults = "" + new Date()  + ";";

        // check top nodes appear
        try {
            WebElement courses = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("courses")));
            WebElement units = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("units")));
            testResults = testResults  + "yes;";
        }
        catch (Exception e){
            testResults = testResults  + "no;";
        }

        // check course groups brwoser opens
        try {
            WebElement courseLink = driver.findElement(By.id("courses"));
            courseLink.click();
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("courses:5E05YA")));
            testResults = testResults  + "yes;";
        }
        catch (Exception e){
            testResults = testResults  + "no;";
        }

        // check units groups browser opens
        try {
            WebElement courseLink = driver.findElement(By.id("units"));
            courseLink.click();
            (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("units:acserv")));
            testResults = testResults  + "yes;";
        }
        catch (Exception e){
            testResults = testResults  + "no;";
        }

        driver.getTitle();

	    driver.switchTo().parentFrame();
	    WebElement logoutButton = driver.findElement(By.id("loginLink1"));
	    logoutButton.click();


        driver.quit();

        return testResults;
    }

    private static void emailIfFailures(String testResults) {

        String failedChecks = "";
        String[] split = testResults.split(";");

        if (split[1].trim().equals("no") || split[2].trim().equals("no") || split[3].trim().equals("no")){

            if (split[1].trim().equals("no")){
                failedChecks = failedChecks + "1) ";
            }
            if (split[2].trim().equals("no")){
                failedChecks = failedChecks + "2) ";
            }
            if (split[3].trim().equals("no")){
                failedChecks = failedChecks + "3) ";
            }


            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.ox.ac.uk");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");

            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("ouit0196","1234Names2");
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("admin@sole.oucs.ox.ac.uk"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("nick.wilson@it.ox.ac.uk, adam.marshall@it.ox.ac.uk"));
                message.setSubject("** WebLearn Groups Browser Alert ** ");
                message.setText("Dear WebLearner," +
                        "\n\nYou have received this email alert because one (or more) of the following 3 checks on WebLearn's course and unit groups tree browser has failed:" +
                        "\n\n1)   Log into WebLearn, navigate to Site Info in the 'Course-Groups-Heroku-Test' site, click the 'Add Participants' button and check if the Course and Unit " +
                        "Groups tree browsers (top nodes) appear within 10 seconds." +
                        "\n\n2)   Click on the top node of the Courses tree and check if 'Ancient History & Classical Archaeology' appears within 10 seconds." +
                        "\n\n3)   Click on the top node of the Units tree and check if 'ASUC' appears within 10 seconds." +
                        "\n\n** FAILED CHECKS **: " + failedChecks + "" +
                        "\n\nAdmin (sole.oucs.ox.ac.uk)" +
                        "\n(if you have any queries, please email or contact Nick Wilson (nick.wilson@it.ox.ac.uk) (13716) who generated this alert)");

                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
