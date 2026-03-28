package playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.List;

public class CartPWPage extends BasePWPage {
  public static final String CART_COUNT      = "#nav-cart-count";
  public static final String CART_LINK       = "#nav-cart";
  public static final String SUBTOTAL_LABEL  =
      "#sc-subtotal-label-buybox, #sc-subtotal-label-activecart";
  public static final String SUBTOTAL_AMOUNT =
      "#sc-subtotal-amount-buybox .a-offscreen, #sc-subtotal-amount-activecart .a-offscreen";
  public static final String ACTIVE_CART     = "#sc-active-cart";
  public static final String CART_ITEMS      = "#sc-active-cart .sc-list-item";
  public static final String EMPTY_MSG       = "#sc-empty-cart-message, h2.a-size-extra-large";
  public static final String CONTINUE_SHOP   = "text=Continue shopping";

  public CartPWPage(Page page) {
    super(page);
  }

  public void open() {
    navigate("/gp/cart/view.html");
  }

  public List<Locator> items() {
    return page.locator(CART_ITEMS).all();
  }

  public int itemCount() {
    return (int) page.locator(CART_ITEMS).count();
  }

  public String cartCount() {
    return text(CART_COUNT);
  }
}
