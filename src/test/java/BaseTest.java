import static java.util.concurrent.TimeUnit.SECONDS;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
  protected WebDriver driver;
  protected WebDriverWait wait;

  @BeforeClass
  public void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    wait = new WebDriverWait(driver, 10);
    driver.manage().timeouts().pageLoadTimeout(15, SECONDS);
    driver.manage().timeouts().setScriptTimeout(10, SECONDS);
    driver.manage().timeouts().implicitlyWait(5, SECONDS);
    driver.manage().window().maximize();
  }

  @AfterClass
  public void tearDown() {
    driver.quit();
  }

  protected WebElement waitUntilAndPerform(ExpectedCondition<WebElement> expectedCondition) {
    return wait.until(expectedCondition);
  }

  protected void waitUntil(ExpectedCondition<Boolean> expectedCondition) {
    wait.until(expectedCondition);
  }
}
