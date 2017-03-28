package ghostdriver;

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
import java.util.HashMap;
import java.util.function.Function;

public class MainClass2 {

    public static final HashMap<String, String> firstHashMap = new HashMap<>();

    public static String SAVE_DIR = "G:/page/";

    public static void main(String[] args) throws InterruptedException {
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
        WebElement area3 = captchaElement.findElement(By.cssSelector("i[data-num='34']"));
//        area3.click();
        if (driver instanceof JavascriptExecutor) {
            System.out.println("driver instanceof JavascriptExecutor");
            JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);
            jsExecutor.executeScript(clickCoordinateJS(area3.getLocation().getX()+2, area3.getLocation
                    ().getY() + 15));
        }

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

    public static String clickCoordinateJS(int x, int y) {
        String jsCode = "var mousemove = $.Event('mousemove');\n" +
                "mousemove.pageX = "+x+";\n" +
                "mousemove.pageY = "+y+";\n" +
                "$(document).trigger(mousemove);\n" +
                "\n" +
                "var eClick = $.Event('click');\n" +
                "eClick.pageX = "+x+";\n" +
                "eClick.pageY = "+y+";\n" +
                "$(document).trigger(eClick);\n";
        return jsCode;
    }
}