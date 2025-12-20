package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
  protected WebDriver driver;
  protected WebDriverWait wait;
  protected WebDriverWait shortWait;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
  }

  public void gotoPath(String path) { driver.navigate().to("https://www.amazon.com" + path); }

  public void waitForVisible(By locator) { wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); }

  public WebElement el(By locator) { return wait.until(ExpectedConditions.presenceOfElementLocated(locator)); }

  public void click(By locator) { wait.until(ExpectedConditions.elementToBeClickable(locator)).click(); }

  public void type(By locator, String text) {
    WebElement e = el(locator);
    e.clear(); e.sendKeys(text);
  }

  public String text(By locator) { return el(locator).getText(); }

  // Dynamic wait utilities
  public boolean isElementPresent(By locator) {
    try {
      shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public boolean isElementVisible(By locator) {
    try {
      shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void waitForPageLoad() {
    wait.until(driver -> ((JavascriptExecutor) driver)
        .executeScript("return document.readyState").equals("complete"));
  }

  public void waitForElementToDisappear(By locator) {
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
  }

  public void waitForTextInElement(By locator, String text) {
    wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  public void waitForUrlContains(String urlFragment) {
    wait.until(ExpectedConditions.urlContains(urlFragment));
  }
}
