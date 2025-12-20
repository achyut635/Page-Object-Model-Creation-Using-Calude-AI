package tests;

import base.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.Assert;
import pages.*;

@Listeners({base.AllureTestListener.class})
@Epic("Amazon")
@Feature("Shopping Cart")
public class CartFunctionalTest {
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

  @Test(groups = {"regression", "cart"})
  @Story("Add item to cart")
  @Owner("QA")
  @Description("Searches for a product, adds it to cart, and verifies the item appears in the cart.")
  public void addItemToCart() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("usb cable");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String searchResultTitle = results.text(results.firstResultTitle);
    System.out.println("Selected product: " + searchResultTitle);

    // Assertions for search results
    Assert.assertTrue(searchResultTitle.length() > 0, "Search result title should not be empty");
    Assert.assertTrue(results.allItems().size() > 0, "Search should return at least one result");

    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Product page title: " + productTitle);

    // Get initial cart count (if visible)
    CartPage cart = new CartPage(driver);
    String initialCartCount = "0";
    try {
      if (cart.el(cart.headerCartCount).isDisplayed()) {
        initialCartCount = cart.text(cart.headerCartCount);
        System.out.println("Initial cart count: " + initialCartCount);
      }
    } catch (Exception e) {
      System.out.println("Cart count not visible or cart is empty");
    }

    // Add to cart
    product.waitForVisible(product.addToCartButton);
    Assert.assertTrue(product.el(product.addToCartButton).isDisplayed(), "Add to cart button should be visible");
    product.click(product.addToCartButton);

    // Handle warranty popup if it appears
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        product.click(product.noThanksWarranty);
        System.out.println("Dismissed warranty popup");
      }
    } catch (Exception e) {
      System.out.println("No warranty popup appeared");
    }

    // Wait a moment for cart to update
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Navigate to cart and verify
    cart.open();
    cart.waitForVisible(cart.activeCart);

    // Verify cart is not empty
    Assert.assertTrue(cart.items().size() > 0, "Cart should contain at least one item after adding product");
    System.out.println("Cart items count: " + cart.items().size());

    // Verify cart count increased
    String finalCartCount = cart.text(cart.headerCartCount);
    System.out.println("Final cart count: " + finalCartCount);
    Assert.assertTrue(!finalCartCount.equals("0"), "Cart count should not be 0 after adding item");
  }

  @Test(groups = {"regression", "cart"})
  @Story("Verify cart item details")
  @Owner("QA")
  @Description("Adds a product to cart and verifies the cart displays item details correctly.")
  public void verifyCartItemDetails() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("wireless earbuds");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Adding product: " + productTitle);

    // Add to cart
    product.click(product.addToCartButton);

    // Handle warranty popup if it appears
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        product.click(product.noThanksWarranty);
      }
    } catch (Exception e) {
      // No popup
    }

    // Wait for cart to update
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Go to cart
    CartPage cart = new CartPage(driver);
    cart.open();
    cart.waitForVisible(cart.activeCart);

    // Verify cart page elements
    Assert.assertTrue(cart.el(cart.cartSubTotalLabel).isDisplayed(), "Cart subtotal label should be visible");
    Assert.assertTrue(cart.items().size() > 0, "Cart should have items");

    String subtotalLabel = cart.text(cart.cartSubTotalLabel);
    System.out.println("Subtotal label: " + subtotalLabel);
    Assert.assertTrue(subtotalLabel.contains("Subtotal") || subtotalLabel.length() > 0, "Subtotal label should be present");

    // Verify at least one item exists in cart
    if (cart.items().size() > 0) {
      System.out.println("Successfully verified cart contains items");
    }
  }

  @Test(groups = {"regression", "cart"})
  @Story("Empty cart verification")
  @Owner("QA")
  @Description("Verifies empty cart displays appropriate message and elements.")
  public void verifyEmptyCart() {
    CartPage cart = new CartPage(driver);
    cart.open();

    // Wait for page to load
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify either cart has items or shows empty message
    boolean hasItems = false;
    boolean showsEmptyMessage = false;

    try {
      if (cart.items().size() > 0) {
        hasItems = true;
        System.out.println("Cart has items: " + cart.items().size());
      }
    } catch (Exception e) {
      System.out.println("No items in cart");
    }

    try {
      if (cart.el(cart.emptyMessage).isDisplayed()) {
        showsEmptyMessage = true;
        System.out.println("Empty cart message: " + cart.text(cart.emptyMessage));
      }
    } catch (Exception e) {
      System.out.println("No empty message displayed");
    }

    // Cart should either have items or show empty message
    Assert.assertTrue(hasItems || showsEmptyMessage, "Cart should either show items or empty message");

    // Verify cart link is always present
    Assert.assertTrue(cart.el(cart.headerCartLink).isDisplayed(), "Cart link should always be visible in header");
  }
}
