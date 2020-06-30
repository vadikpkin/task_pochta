import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class RusPochtaTaskTest extends BaseTest {

  @Test(description = "Doing google search and asserting first search result")
  public void googleSearchTest() {
    String search = "Яндекс маркет";
    String expectedUrl = "https://market.yandex.ru/";
    String googleUrl = "https://www.google.com/";
    //Открыть страницу Гугл поиска
    driver.get(googleUrl);
    assertEquals(driver.getCurrentUrl(), googleUrl, "Wrong URL opened");
    //в поисковике ввести яндекс маркет
    WebElement searchInput = driver.findElement(By.name("q"));
    searchInput.sendKeys(search);
    searchInput.submit();
    //Проверить что первая страница в поиске ссылается на яндекс маркет
    waitUntilAndPerform(ExpectedConditions.visibilityOf(driver.findElement(By.id("result-stats"))));
    WebElement firstSearchResult = driver.findElement(By.cssSelector("#rso .r > a"));
    String firstUrlInSearch = firstSearchResult.getAttribute("href");
    assertEquals(firstUrlInSearch, expectedUrl, "Wrong first URL is search");
    //перейти по ссылке
    firstSearchResult.click();
    //Откроется  яндекс маркет
    assertEquals(driver.getCurrentUrl(), expectedUrl, "Wrong URL opened");
  }

  @Test(description = "Yandex market filters test")
  public void yandexMarketTes() {
    String search = "пылесосы";
    String url = "https://market.yandex.ru/";
    List<String> brands = Arrays.asList("VITEK", "Polaris");
    String expectedPriceTo = "6000";
    //Открыть страницу яндекс маркет
    driver.get(url);
    //Страница открыта
    assertEquals(driver.getCurrentUrl(), url, "Wrong URL opened");
    //в поисковике ввести пылесосы
    WebElement searchInput = driver.findElement(By.id("header-search"));
    searchInput.sendKeys(search);
    searchInput.submit();
    //тк недостаточно просто ввести 'пылесосы' в поиск, то нажимаю для перехода
    waitUntilAndPerform(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Пылесосы']"))).click();
    //Выполнить нажатие на кнопку Все фильтры
    driver.findElement(By.xpath("//div[@data-zone-name='all-filters-button']//a")).click();
    //Выбрать в списке Polaris и Vitek
    String vendorSectionXpath = "//span[text()='Производитель']/../../..";
    waitUntilAndPerform(ExpectedConditions.elementToBeClickable(By.xpath(vendorSectionXpath + "//button"))).click();
    brands.forEach(e -> {
      driver.findElement(By.xpath(vendorSectionXpath + "//*[@class='input__control']")).clear();
      driver.findElement(By.xpath(vendorSectionXpath + "//*[@class='input__control']")).sendKeys(e);
      waitUntil(ExpectedConditions.textToBe(By.xpath(vendorSectionXpath + "//label[@class='checkbox__label']"), e));
      driver.findElement(By.xpath(vendorSectionXpath + "//label[@class='checkbox__label']")).click();
    });
    //Установить  цену в поле  до = 6000
    driver.findElement(By.id("glf-priceto-var")).sendKeys(expectedPriceTo);
    //Проверить что появилось окно с отоброжаемым количеством товаров
    WebElement popUpWithProductAmount = waitUntilAndPerform(ExpectedConditions.visibilityOf(driver.findElement(By.className("n-filter-panel-counter"))));
    assertTrue(popUpWithProductAmount.isDisplayed(), "Amount pop up with amount of product is not displayed");
    //Нажать Показать подходящие
    driver.findElement(By.xpath("//span[text()='Показать подходящие']/..")).click();
    //Проверить что данные из фильтра  отобразились в настройках с права
    waitUntilAndPerform(ExpectedConditions.visibilityOf(driver.findElement(By.id("search-prepack"))));
    String actualPriceTo = driver.findElement(By.id("glpriceto")).getAttribute("value");
    assertEquals(actualPriceTo, expectedPriceTo, "Wrong price to");
    brands.forEach(e -> {
      boolean isBrandSelected = driver.findElement(By.xpath(String.format("//*[@data-autotest-id='7893318']//input[contains(@name, '%s')]", e))).isSelected();
      assertTrue(isBrandSelected, "Expected brand checkbox is not checked");
    });
  }
}