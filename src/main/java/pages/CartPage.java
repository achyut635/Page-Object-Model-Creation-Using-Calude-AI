package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {
  public final By headerCartCount = By.id("nav-cart-count");
  public final By headerCartLink = By.id("nav-cart");
  public final By cartSubTotalLabel = By.cssSelector("#sc-subtotal-label-buybox, #sc-subtotal-label-activecart");
  public final By cartSubTotalAmount = By.cssSelector("#sc-subtotal-amount-buybox .a-offscreen, #sc-subtotal-amount-activecart .a-offscreen");

  public final By activeCart = By.id("sc-active-cart");
  public final By cartItems = By.cssSelector("#sc-active-cart .sc-list-item");

  public final By emptyMessage = By.cssSelector("#sc-empty-cart-message, h2.a-size-extra-large");
  public final By continueShopping = By.linkText("Continue shopping");

  public final By itemTitleSel = By.cssSelector(".sc-product-title, span.a-truncate-full");
  public final By itemPriceSel = By.cssSelector(".sc-product-price, .a-price .a-offscreen");
  public final By quantityDropdownSel = By.cssSelector("select[name='quantity'], select[data-action='select-quantity']");
  public final By saveForLaterSel = By.cssSelector("input[value='Save for later'], .sc-action-save-for-later input");
  public final By deleteButtonSel = By.cssSelector("input[value='Delete'], .sc-action-delete input");
  public final By itemCheckboxSel = By.cssSelector("input[type='checkbox'][name='sc-selection']");

  public CartPage(WebDriver driver) { super(driver); }

  public void open() {
    gotoPath("/gp/cart/view.html");
    waitForPageLoad();
    // Wait for cart to fully load (either items or empty message)
    boolean cartLoaded = false;
    try {
      waitForVisible(activeCart);
      cartLoaded = true;
    } catch (Exception e) {
      // If active cart not visible, wait for empty message
      try {
        waitForVisible(emptyMessage);
        cartLoaded = true;
      } catch (Exception ex) {
        // Neither element found - cart page failed to load
        throw new RuntimeException("Cart page failed to load - neither cart items nor empty message found after 20s");
      }
    }
  }

  public void waitForCartCountUpdate(String expectedCount) {
    waitForTextInElement(headerCartCount, expectedCount);
  }

  public List<WebElement> items() { return driver.findElements(cartItems); }

  public WebElement item(int index) { return items().get(index); }
}
