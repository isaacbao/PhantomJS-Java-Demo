import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainClass {

    public static final HashMap<String, String> firstHashMap = new HashMap<>();

    public static String SAVE_DIR = "G:/page/";

    public static void main(String[] args) throws InterruptedException {
//        String CHROME_DRIVER_PATH = "G:\\woshou\\chromedriver_win32\\chromedriver.exe";
//        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
//        String FIREFOX_DRIVER_PATH = "G:\\woshou\\geckodriver-v0.15.0-win64\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER_PATH);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability("takesScreenshot", true);
        String PHANTOM_PATH = "G:\\woshou\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOM_PATH);

        WebDriver driver = new PhantomJSDriver(caps);
//        String url = "https://passport.zhaopin.com/org/login";
        String url = "www.baidu.com";

        driver.get(url);
        Thread.sleep(10000);
        try {
            saveScreenShot((TakesScreenshot) driver, SAVE_DIR + "PhantomScreenShot.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CheckCodeCapt")));
//        WebElement captchaButton = driver.findElement(By.cssSelector("#CheckCodeCapt"));
//
//        captchaButton.click();
//        Thread.sleep(2000);
//        WebElement captchaElement2 = driver.findElement(By.cssSelector("div.captcha-group"));
//        getCaptcha((TakesScreenshot) driver, captchaElement2, SAVE_DIR + "captcha2.png");
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

    private static void saveScreenShot(TakesScreenshot driver, String savePath) throws IOException {
        File loginScreenshot = driver.getScreenshotAs(OutputType.FILE);
        File outFile = new File(savePath);
        FileUtils.copyFile(loginScreenshot, outFile);
    }

}