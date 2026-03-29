package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AmazonProductPage extends BasePage {
  public final By productTitle = By.id("productTitle");
  public final By priceContainer = By.cssSelector("#corePriceDisplay_desktop_feature_div, .a-price");
  public final By priceWhole = By.cssSelector("#corePriceDisplay_desktop_feature_div .a-offscreen, .a-price .a-offscreen");
  public final By rating = By.cssSelector("#averageCustomerReviews, #acrPopover");
  public final By reviewCount = By.id("acrCustomerReviewText");

  public final By addToCartButton = By.cssSelector("#add-to-cart-button, input#add-to-cart-button");
  public final By buyNowButton = By.id("buy-now-button");
  public final By quantityDropdown = By.cssSelector("select#quantity");
  public final By addedToCartMessage = By.xpath("(//h1 | //*[@class='a-size-medium-plus'])[contains(., 'added to cart')]");

  public final By colorSwatches = By.cssSelector("#variation_color_name li, #variation_color_name img");
  public final By sizeDropdown = By.cssSelector("#native_dropdown_selected_size_name, #size_name");
  public final By aboutThisItem = By.xpath("//h1|//h2[contains(., 'About this item')]/..");
  public final By imageGallery = By.cssSelector("#imageBlock, #imgTagWrapperId img");

  public final By noThanksWarranty = By.xpath("//button[contains(., 'No Thanks') or contains(., 'No, thanks')]");

  public AmazonProductPage(WebDriver driver) { super(driver); }

  public void addToCartAndHandlePopups() {
    click(addToCartButton);
    waitForPageLoad();

    // Handle warranty popup if it appears (with short timeout)
    if (isElementVisible(noThanksWarranty)) {
      click(noThanksWarranty);
      waitForPageLoad();
    }
  }
}
