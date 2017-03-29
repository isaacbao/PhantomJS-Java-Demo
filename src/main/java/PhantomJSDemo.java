import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PhantomJSDemo {

    public static String SAVE_DIR = "G:/page/";

    public static void main(String[] args) throws InterruptedException {
        try {
            configure();
            prepareDriver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebDriver driver = getDriver();
        String url = "https://passport.zhaopin.com/org/login";

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
            ((JavascriptExecutor)driver).executeScript(clickCoordinateJS(area3.getLocation().getX(),area3.getLocation
                    ().getY()+50));
        }

        try {
            saveScreenShot((TakesScreenshot) driver, SAVE_DIR + "PhantomScreenShot.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String clickCoordinateJS(int x,int y){
        String jsCode = "e = $.Event('click');\n" +
                "        e.pageX = "+x+";\n" +
                "        e.pageY = "+y+";\n" +
                "        $(document).trigger(e);";
        return jsCode;
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

    private static WebDriver mDriver = null;
    private boolean mAutoQuitDriver = true;

    private static final String DRIVER_FIREFOX = "firefox";
    private static final String DRIVER_CHROME = "chrome";
    private static final String DRIVER_PHANTOMJS = "phantomjs";

    protected static DesiredCapabilities sCaps;

    public static void configure() throws IOException {
        sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", true);

        String driver = DRIVER_PHANTOMJS;

        if (driver.equals(DRIVER_PHANTOMJS)) {
            sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "G:/woshou/phantomjs-2.1.1-windows/phantomjs-2.1.1-windows/bin/phantomjs.exe");
            System.out.println("Test will use PhantomJS internal GhostDriver");
        }

        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]{
                "--logLevel=" + ("INFO")
        });
    }

    public static void prepareDriver() throws Exception {
//        mDriver = new PhantomJSDriver(sCaps);

        //        String CHROME_DRIVER_PATH = "G:\\woshou\\chromedriver_win32\\chromedriver.exe";
//        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
//        mDriver = new ChromeDriver();

        String FIREFOX_DRIVER_PATH = "G:\\woshou\\geckodriver-v0.15.0-win64\\geckodriver.exe";
//        String FIREFOX_DRIVER_PATH = "G:\\woshou\\geckodriver-v0.11.1-win64\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER_PATH);
        mDriver = new FirefoxDriver();
    }

    public static WebDriver getDriver() {
        return mDriver;
    }

    protected void disableAutoQuitDriver() {
        mAutoQuitDriver = false;
    }

    protected void enableAutoQuitDriver() {
        mAutoQuitDriver = true;
    }

    protected boolean isAutoQuitDriverEnabled() {
        return mAutoQuitDriver;
    }

    public void quitDriver() {
        if (mAutoQuitDriver && mDriver != null) {
            mDriver.quit();
            mDriver = null;
        }
    }
}