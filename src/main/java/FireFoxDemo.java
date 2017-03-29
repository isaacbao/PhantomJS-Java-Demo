import org.apache.commons.io.IOUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.function.Function;

public class FireFoxDemo {

    public static final HashMap<String, String> firstHashMap = new HashMap<>();

    public static String SAVE_DIR = "G:/page/";

    public static void main(String[] args) throws InterruptedException, IOException {
        String FIREFOX_DRIVER_PATH = "G:\\woshou\\geckodriver-v0.15.0-win64\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER_PATH);
        WebDriver driver = new FirefoxDriver();


        String url = "https://passport.zhaopin.com/org/login?BkUrl=/s/homepage.asp%3F";

        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CheckCodeCapt")));
        WebElement captchaButton = driver.findElement(By.cssSelector("#CheckCodeCapt"));
        captchaButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("captcha-img-sprites")));

        WebElement captchaElement = driver.findElement(By.cssSelector("#captcha-img-sprites"));
        WebElement area34 = captchaElement.findElement(By.cssSelector("i[data-num='34']"));
        WebElement area4 = captchaElement.findElement(By.cssSelector("i[data-num='4']"));
        WebElement area14 = captchaElement.findElement(By.cssSelector("i[data-num='14']"));
        Thread.sleep(5000);
//        area3.click();
        if (driver instanceof JavascriptExecutor) {
            System.out.println("driver instanceof JavascriptExecutor");
            JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
            jsExecutor.executeScript(clickCoordinateJS("captcha-img-sprites", area34.getLocation().getX() + 20, area34
                    .getLocation().getY() + 18));
            Thread.sleep(2000);
            jsExecutor.executeScript(clickCoordinateJS("captcha-img-sprites", area4.getLocation().getX() + 5, area34
                    .getLocation().getY() + 8));
            Thread.sleep(2000);
            jsExecutor.executeScript(clickCoordinateJS("captcha-img-sprites", area14.getLocation().getX() - 16, area34
                    .getLocation().getY() + 13));
        }

//        Thread.sleep(5000);
//        WebElement captchaSubmitButton = driver.findElement(By.cssSelector("#captcha-submitCode"));
//        captchaSubmitButton.click();


    }

    private static BufferedImage getCaptcha(TakesScreenshot driver, WebElement captchaElement, String savePath) {
        File loginScreenshot = driver.getScreenshotAs(OutputType.FILE);
        BufferedImage loginScreenshotImage = null;
        try {
            loginScreenshotImage = ImageIO.read(loginScreenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Point point = captchaElement.getLocation();
        int eleWidth = captchaElement.getSize().getWidth();
        int eleHeight = captchaElement.getSize().getHeight();

        BufferedImage captchaScreenshot = loginScreenshotImage.getSubimage(point.getX(), point.getY(),
                eleWidth, eleHeight);
        return captchaScreenshot;
    }

    public static String clickCoordinateJS(String elementId, int x, int y) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("js/simulate_mouse.js");

        String jsCode = IOUtils.toString(is, Charset.forName("UTF-8"));
        jsCode = jsCode.replace("{$ELEMENT_ID}", elementId).replace("{$POSITION_X}", String.valueOf(x)).replace
                ("{$POSITION_Y}", String.valueOf(y));
        return jsCode;
    }
}