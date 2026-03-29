package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AmazonHomePage extends BasePage {
  public final By logoLink = By.cssSelector("#nav-logo-sprites, a[aria-label='Amazon']");
  public final By deliverToButton = By.cssSelector("#glow-ingress-line2, #nav-global-location-popover-link");
  public final By searchInput = By.id("twotabsearchtextbox");
  public final By searchDropdown = By.id("searchDropdownBox");
  public final By searchSubmit = By.id("nav-search-submit-button");
  public final By languageSelector = By.cssSelector("#icp-nav-flyout, #icp-nav-link-inner");
  public final By accountAndLists = By.id("nav-link-accountList");
  public final By returnsAndOrders = By.id("nav-orders");
  public final By cartLink = By.id("nav-cart");
  public final By hamburgerMenu = By.id("nav-hamburger-menu");

  public final By todaysDealsLink = By.linkText("Today's Deals");
  public final By customerServiceLink = By.linkText("Customer Service");
  public final By registryLink = By.linkText("Registry");
  public final By giftCardsLink = By.linkText("Gift Cards");
  public final By sellLink = By.linkText("Sell");

  public final By heroCarouselNext = By.cssSelector("button[aria-label*='Next'], .a-carousel-goto-nextpage");
  public final By heroCarouselPrev = By.cssSelector("button[aria-label*='Previous'], .a-carousel-goto-prevpage");

  public AmazonHomePage(WebDriver driver) { super(driver); }

  public void search(String query) {
    type(searchInput, query);
    click(searchSubmit);
    waitForPageLoad();
  }
}
