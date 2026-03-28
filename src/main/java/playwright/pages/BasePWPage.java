package playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class BasePWPage {
  protected Page page;
  protected static final String BASE_URL = "https://www.amazon.com";

  public BasePWPage(Page page) {
    this.page = page;
  }

  public void navigate(String path) {
    page.navigate(BASE_URL + path);
  }

  public Locator locator(String selector) {
    return page.locator(selector);
  }

  public void click(String selector) {
    page.locator(selector).click();
  }

  public void fill(String selector, String text) {
    page.locator(selector).clear();
    page.locator(selector).fill(text);
  }

  public String text(String selector) {
    return page.locator(selector).textContent().trim();
  }

  public boolean isVisible(String selector) {
    return page.locator(selector).isVisible();
  }

  public void waitForVisible(String selector) {
    page.locator(selector).waitFor(
        new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
  }

  public void waitForURL(String urlPattern) {
    page.waitForURL("**" + urlPattern + "**");
  }
}
