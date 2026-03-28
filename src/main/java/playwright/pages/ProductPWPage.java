package playwright.pages;

import com.microsoft.playwright.Page;

public class ProductPWPage extends BasePWPage {
  public static final String PRODUCT_TITLE   = "#productTitle";
  public static final String PRICE_WHOLE     =
      "#corePriceDisplay_desktop_feature_div .a-offscreen, .a-price .a-offscreen";
  public static final String RATING          = "#averageCustomerReviews, #acrPopover";
  public static final String REVIEW_COUNT    = "#acrCustomerReviewText";
  public static final String ADD_TO_CART_BTN = "#add-to-cart-button, input#add-to-cart-button";
  public static final String BUY_NOW_BTN     = "#buy-now-button";
  public static final String IMAGE_GALLERY   = "#imageBlock, #imgTagWrapperId img";
  public static final String NO_THANKS       =
      "button:has-text('No Thanks'), button:has-text('No, thanks')";

  public ProductPWPage(Page page) {
    super(page);
  }

  public void addToCart() {
    click(ADD_TO_CART_BTN);
    dismissWarrantyPopupIfPresent();
  }

  public void dismissWarrantyPopupIfPresent() {
    try {
      if (page.locator(NO_THANKS).isVisible()) {
        click(NO_THANKS);
      }
    } catch (Exception ignored) {
    }
  }

  public String getTitle() {
    waitForVisible(PRODUCT_TITLE);
    return text(PRODUCT_TITLE);
  }
}
