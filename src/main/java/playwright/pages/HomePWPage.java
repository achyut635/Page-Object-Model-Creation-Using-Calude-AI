package playwright.pages;

import com.microsoft.playwright.Page;

public class HomePWPage extends BasePWPage {
  public static final String LOGO         = "#nav-logo-sprites, a[aria-label='Amazon']";
  public static final String SEARCH_INPUT = "#twotabsearchtextbox";
  public static final String SEARCH_BTN   = "#nav-search-submit-button";
  public static final String CART_LINK    = "#nav-cart";
  public static final String ACCOUNT_LINK = "#nav-link-accountList";
  public static final String HAMBURGER    = "#nav-hamburger-menu";
  public static final String DELIVER_TO   = "#glow-ingress-line2, #nav-global-location-popover-link";

  public HomePWPage(Page page) {
    super(page);
    navigate("/");
  }

  public void search(String query) {
    fill(SEARCH_INPUT, query);
    click(SEARCH_BTN);
  }
}
