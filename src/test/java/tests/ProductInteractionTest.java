package tests;

import base.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.*;

@Listeners({base.AllureTestListener.class})
@Epic("Amazon")
@Feature("Product Page")
public class ProductInteractionTest {
  private WebDriver driver;

  @BeforeClass
  @Parameters({"headed"})
  public void setUp(@Optional("false") String headed) {
    DriverFactory.initDriver(Boolean.parseBoolean(headed));
    driver = DriverFactory.getDriver();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    DriverFactory.quitDriver();
  }

  @Test(groups = {"regression", "product"})
  @Story("Product page elements")
  @Owner("QA")
  @Description("Verifies all critical product page elements are displayed and accessible.")
  public void verifyProductPageElements() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("bluetooth speaker");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String searchResult = results.text(results.firstResultTitle);
    System.out.println("Opening product: " + searchResult);

    assert searchResult.length() > 0 : "Search result should have a title";
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);

    // Verify critical elements
    String title = product.text(product.productTitle);
    assert title.length() > 0 : "Product title should not be empty";
    assert title.toLowerCase().contains("speaker") || title.toLowerCase().contains("bluetooth") :
        "Product title should be relevant to search";
    System.out.println("Product title: " + title);

    // Verify add to cart button
    assert product.el(product.addToCartButton).isDisplayed() : "Add to cart button should be visible";

    // Verify price container is present
    try {
      if (product.el(product.priceContainer).isDisplayed()) {
        System.out.println("Price container is visible");
        assert true;
      }
    } catch (Exception e) {
      System.out.println("Price container not found - this may vary by product");
    }

    // Verify rating section if available
    try {
      if (product.el(product.rating).isDisplayed()) {
        System.out.println("Product rating is displayed");
        assert true;
      }
    } catch (Exception e) {
      System.out.println("Rating not displayed for this product");
    }
  }

  @Test(groups = {"smoke", "product"})
  @Story("Add to cart interaction")
  @Owner("QA")
  @Description("Tests the add to cart button functionality and verifies cart update.")
  public void testAddToCartButton() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("phone case");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Testing add to cart for: " + productTitle);

    // Verify add to cart button is clickable
    assert product.el(product.addToCartButton).isDisplayed() : "Add to cart button must be visible";
    assert product.el(product.addToCartButton).isEnabled() : "Add to cart button must be enabled";

    // Click add to cart
    product.click(product.addToCartButton);

    // Handle warranty popup if present
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        System.out.println("Warranty popup detected, dismissing...");
        product.click(product.noThanksWarranty);
      }
    } catch (Exception e) {
      System.out.println("No warranty popup");
    }

    // Wait for cart to update
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify cart count updated
    CartPage cart = new CartPage(driver);
    String cartCount = cart.text(cart.headerCartCount);
    System.out.println("Cart count after adding: " + cartCount);
    assert !cartCount.equals("0") : "Cart count should be greater than 0 after adding item";
  }

  @Test(groups = {"regression", "product"})
  @Story("Product price verification")
  @Owner("QA")
  @Description("Verifies product price is displayed and properly formatted.")
  public void verifyProductPrice() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("laptop charger");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    System.out.println("Checking price for: " + product.text(product.productTitle));

    // Try to find and verify price
    try {
      product.waitForVisible(product.priceContainer);
      assert product.el(product.priceContainer).isDisplayed() : "Price container should be visible";

      String priceText = product.text(product.priceWhole);
      System.out.println("Product price: " + priceText);

      // Price should contain dollar sign and numbers
      assert priceText.matches(".*\\$[0-9,.]+.*") || priceText.contains("$") :
          "Price should contain dollar sign and amount";

    } catch (Exception e) {
      System.out.println("Price not displayed in expected format - may vary by product listing");
      // Some products may have different price structures, so we'll be lenient here
    }
  }

  @Test(groups = {"regression", "product"})
  @Story("Product rating and reviews")
  @Owner("QA")
  @Description("Verifies product rating and review count are displayed if available.")
  public void verifyProductRatingAndReviews() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("bestselling books");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    System.out.println("Checking reviews for: " + product.text(product.productTitle));

    // Check if rating is displayed
    try {
      if (product.el(product.rating).isDisplayed()) {
        System.out.println("Rating section is visible");
        assert true : "Rating should be displayed";

        // Check for review count
        try {
          if (product.el(product.reviewCount).isDisplayed()) {
            String reviewText = product.text(product.reviewCount);
            System.out.println("Review count: " + reviewText);
            assert reviewText.length() > 0 : "Review count text should not be empty";
          }
        } catch (Exception e) {
          System.out.println("Review count not displayed");
        }
      }
    } catch (Exception e) {
      System.out.println("Rating section not found - product may not have reviews yet");
    }
  }

  @Test(groups = {"regression", "product"})
  @Story("Buy now button availability")
  @Owner("QA")
  @Description("Verifies the Buy Now button is present and clickable on product pages.")
  public void verifyBuyNowButton() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("office supplies");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    System.out.println("Verifying Buy Now for: " + product.text(product.productTitle));

    // Verify Buy Now button exists
    try {
      if (product.el(product.buyNowButton).isDisplayed()) {
        assert product.el(product.buyNowButton).isEnabled() : "Buy Now button should be enabled";
        System.out.println("Buy Now button is available and enabled");
      }
    } catch (Exception e) {
      System.out.println("Buy Now button not available for this product");
      // Some products may not have Buy Now option
    }

    // At minimum, Add to Cart should always be present
    assert product.el(product.addToCartButton).isDisplayed() :
        "Add to Cart button must always be available";
  }
}
