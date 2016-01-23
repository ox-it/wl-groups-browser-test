import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by nickwilson on 1/22/16.
 */
public class GroupsBrowserTester {

    public static void main(String[] args) {


        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);

        // And now use this to visit Google
        driver.get("https://weblearn.ox.ac.uk/portal/xlogin?returnPath=%2F");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("eid"));

        // Enter something to search for
        element.sendKeys("dashing");

        // Find the text input element by its name
        element = driver.findElement(By.name("pw"));

        // Enter something to search for
        element.sendKeys("dashDASH123");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page

        driver.get("https://weblearn.ox.ac.uk/portal/hierarchy/261b9bd5-9a08-4be1-b0de-2bb39d3d7543/page/site_info");


        driver.switchTo().frame("Mainbb447cc4x7b6cx4bb9xbfcdx60888ca4089b");
//        WebDriverWait wait = new WebDriverWait(driver, 10); wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("eventSubmit_doExternalGroupsHelper")));

        WebElement button = driver.findElement(By.name("eventSubmit_doExternalGroupsHelper"));
        button.click();


        WebElement courses = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("courses")));

        WebElement units = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("units")));

        System.out.println("Page title is: " + driver.getTitle());

        driver.quit();



//        log
//                email
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.ox.ac.uk");
//        props.put("mail.smtp.socketFactory.port", "587");
//        props.put("mail.smtp.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        //You need to put your user name and password here
                        return new PasswordAuthentication("ouy","password");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("admin@sole.oucs.ox.ac.uk"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("nick.wilson@it.ox.ac.uk"));
            message.setSubject("** WebLearn Groups Browser Alert ** ");
            message.setText("Welcome to makecodeeasy blog," +
                    "\n\n Mail from my mail Server!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
