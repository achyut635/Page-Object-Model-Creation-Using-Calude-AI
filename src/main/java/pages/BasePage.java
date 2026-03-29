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

  public void gotoPath(String path) {
    try {
      driver.navigate().to("https://www.amazon.com" + path);
      waitForPageLoad();
    } catch (TimeoutException e) {
      throw new RuntimeException("Failed to load page: " + path + " - Timeout after 20s", e);
    }
  }

  public void waitForVisible(By locator) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    } catch (TimeoutException e) {
      throw new RuntimeException("Element not visible after 20s: " + locator.toString(), e);
    }
  }

  public WebElement el(By locator) {
    try {
      return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (TimeoutException e) {
      throw new RuntimeException("Element not found after 20s: " + locator.toString(), e);
    }
  }

  public void click(By locator) {
    try {
      wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    } catch (TimeoutException e) {
      throw new RuntimeException("Element not clickable after 20s: " + locator.toString(), e);
    }
  }

  public void type(By locator, String text) {
    try {
      WebElement e = el(locator);
      e.clear();
      e.sendKeys(text);
    } catch (Exception e) {
      throw new RuntimeException("Failed to type into element: " + locator.toString(), e);
    }
  }

  public String text(By locator) {
    try {
      return el(locator).getText();
    } catch (Exception e) {
      throw new RuntimeException("Failed to get text from element: " + locator.toString(), e);
    }
  }

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
    try {
      wait.until(driver -> ((JavascriptExecutor) driver)
          .executeScript("return document.readyState").equals("complete"));
    } catch (TimeoutException e) {
      throw new RuntimeException("Page did not finish loading after 20s", e);
    }
  }

  public void waitForElementToDisappear(By locator) {
    try {
      wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    } catch (TimeoutException e) {
      throw new RuntimeException("Element did not disappear after 20s: " + locator.toString(), e);
    }
  }

  public void waitForTextInElement(By locator, String text) {
    try {
      wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    } catch (TimeoutException e) {
      throw new RuntimeException("Text '" + text + "' not present in element after 20s: " + locator.toString(), e);
    }
  }

  public void waitForUrlContains(String urlFragment) {
    try {
      wait.until(ExpectedConditions.urlContains(urlFragment));
    } catch (TimeoutException e) {
      throw new RuntimeException("URL did not contain '" + urlFragment + "' after 20s. Current URL: " + driver.getCurrentUrl(), e);
    }
  }
}
