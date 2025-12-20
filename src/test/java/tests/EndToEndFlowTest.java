package tests;

import base.DriverFactory;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.Assert;
import pages.*;

@Listeners({base.AllureTestListener.class})
@Epic("Amazon")
@Feature("End-to-End User Journeys")
public class EndToEndFlowTest {
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

  @Test(groups = {"smoke", "e2e"}, priority = 1)
  @Story("Complete shopping flow")
  @Owner("QA")
  @Description("End-to-end test: Search for product -> View product details -> Add to cart -> Verify cart")
  public void completeShoppingFlow() {
    // Step 1: Search for product
    AmazonHomePage home = new AmazonHomePage(driver);
    String searchTerm = "wireless headphones";
    System.out.println("Step 1: Searching for '" + searchTerm + "'");
    home.search(searchTerm);

    // Step 2: Verify search results
    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String firstResultTitle = results.text(results.firstResultTitle);
    System.out.println("Step 2: Found result - " + firstResultTitle);

    Assert.assertTrue(firstResultTitle.length() > 0, "Search results should display product titles");
    Assert.assertTrue(results.allItems().size() > 0, "Search should return multiple results");
    Assert.assertTrue(firstResultTitle.toLowerCase().contains("headphone") ||
           firstResultTitle.toLowerCase().contains("wireless"), "Search results should be relevant to query");

    // Step 3: Open product page
    System.out.println("Step 3: Opening product page");
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Product page loaded: " + productTitle);

    Assert.assertTrue(productTitle.length() > 0, "Product page should show title");
    Assert.assertTrue(product.el(product.addToCartButton).isDisplayed(), "Add to cart button should be visible");

    // Step 4: Add to cart
    System.out.println("Step 4: Adding product to cart");
    product.click(product.addToCartButton);

    // Handle warranty popup
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        System.out.println("Dismissing warranty popup");
        product.click(product.noThanksWarranty);
      }
    } catch (Exception e) {
      System.out.println("No warranty popup");
    }

    // Wait for cart update
    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Step 5: Verify cart
    System.out.println("Step 5: Verifying cart contents");
    CartPage cart = new CartPage(driver);
    cart.open();
    cart.waitForVisible(cart.activeCart);

    Assert.assertTrue(cart.items().size() > 0, "Cart should contain the added product");
    Assert.assertTrue(cart.el(cart.cartSubTotalLabel).isDisplayed(), "Cart should show subtotal");

    String cartCount = cart.text(cart.headerCartCount);
    System.out.println("Final cart count: " + cartCount);
    Assert.assertTrue(!cartCount.equals("0"), "Cart count should reflect added items");

    System.out.println("✓ Complete shopping flow test PASSED");
  }

  @Test(groups = {"regression", "e2e"}, priority = 2)
  @Story("Multi-product browsing flow")
  @Owner("QA")
  @Description("End-to-end test: Search -> Browse multiple products -> Compare -> Add to cart")
  public void multiProductBrowsingFlow() {
    // Step 1: Search for a category
    AmazonHomePage home = new AmazonHomePage(driver);
    String searchTerm = "computer mouse";
    System.out.println("Step 1: Searching for '" + searchTerm + "'");
    home.search(searchTerm);

    // Step 2: Verify multiple results
    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    int resultCount = results.allItems().size();
    System.out.println("Step 2: Found " + resultCount + " results");

    Assert.assertTrue(resultCount >= 3, "Should have at least 3 products to browse");

    // Step 3: Browse first product
    System.out.println("Step 3: Browsing first product");
    String firstProductTitle = results.text(results.firstResultTitle);
    System.out.println("First product: " + firstProductTitle);
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String product1Title = product.text(product.productTitle);
    String product1Price = "N/A";

    try {
      product1Price = product.text(product.priceWhole);
    } catch (Exception e) {
      System.out.println("Price not available for first product");
    }

    System.out.println("Product 1 - Title: " + product1Title + ", Price: " + product1Price);
    Assert.assertTrue(product1Title.length() > 0, "First product should have a title");

    // Step 4: Navigate back to search results
    System.out.println("Step 4: Navigating back to search results");
    driver.navigate().back();

    results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    Assert.assertTrue(results.allItems().size() > 0, "Should return to search results");

    // Step 5: Add first product to cart
    System.out.println("Step 5: Adding first product to cart");
    results.openFirstResult();

    product = new AmazonProductPage(driver);
    product.waitForVisible(product.addToCartButton);
    product.click(product.addToCartButton);

    // Handle warranty popup
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        product.click(product.noThanksWarranty);
      }
    } catch (Exception e) {
      // No popup
    }

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Step 6: Verify cart
    System.out.println("Step 6: Verifying cart");
    CartPage cart = new CartPage(driver);
    cart.open();
    cart.waitForVisible(cart.activeCart);

    Assert.assertTrue(cart.items().size() > 0, "Cart should contain browsed product");
    System.out.println("Cart items: " + cart.items().size());

    System.out.println("✓ Multi-product browsing flow test PASSED");
  }

  @Test(groups = {"regression", "e2e"}, priority = 3)
  @Story("Search with navigation flow")
  @Owner("QA")
  @Description("End-to-end test: Home -> Search -> Results verification -> Product -> Back to home")
  public void searchAndNavigationFlow() {
    // Step 1: Verify home page
    AmazonHomePage home = new AmazonHomePage(driver);
    System.out.println("Step 1: Verifying home page elements");

    Assert.assertTrue(home.el(home.searchInput).isDisplayed(), "Search box should be visible on home");
    Assert.assertTrue(home.el(home.logoLink).isDisplayed(), "Amazon logo should be visible");
    Assert.assertTrue(home.el(home.cartLink).isDisplayed(), "Cart link should be visible");

    // Step 2: Perform search
    String searchQuery = "gaming keyboard";
    System.out.println("Step 2: Searching for '" + searchQuery + "'");
    home.search(searchQuery);

    // Step 3: Validate search results page
    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    System.out.println("Step 3: Validating search results");

    Assert.assertTrue(results.allItems().size() > 0, "Should have search results");
    Assert.assertTrue(results.el(results.sortDropdown).isDisplayed(), "Sort dropdown should be available");

    String firstResult = results.text(results.firstResultTitle);
    System.out.println("First result: " + firstResult);
    Assert.assertTrue(firstResult.toLowerCase().contains("keyboard") ||
           firstResult.toLowerCase().contains("gaming"), "Results should match search query");

    // Step 4: Open product
    System.out.println("Step 4: Opening product page");
    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Product opened: " + productTitle);

    Assert.assertTrue(productTitle.length() > 0, "Product should have title");
    Assert.assertTrue(product.el(product.addToCartButton).isDisplayed(), "Should have add to cart option");

    // Step 5: Navigate back to home
    System.out.println("Step 5: Navigating back to home page");
    home.el(home.logoLink).click();

    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify we're back on home page
    home = new AmazonHomePage(driver);
    Assert.assertTrue(home.el(home.searchInput).isDisplayed(), "Should be back on home page");
    System.out.println("Successfully navigated back to home page");

    System.out.println("✓ Search and navigation flow test PASSED");
  }

  @Test(groups = {"smoke", "e2e"}, priority = 4)
  @Story("Quick add to cart flow")
  @Owner("QA")
  @Description("Simplified E2E: Search -> Add first result to cart -> Verify")
  public void quickAddToCartFlow() {
    System.out.println("Starting quick add to cart flow");

    // Search
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("water bottle");

    // Open first result
    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String productName = results.text(results.firstResultTitle);
    System.out.println("Adding to cart: " + productName);
    results.openFirstResult();

    // Add to cart
    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.addToCartButton);
    product.click(product.addToCartButton);

    // Handle popup
    try {
      if (product.el(product.noThanksWarranty).isDisplayed()) {
        product.click(product.noThanksWarranty);
      }
    } catch (Exception e) {
      // No popup
    }

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Verify cart
    CartPage cart = new CartPage(driver);
    String cartCount = cart.text(cart.headerCartCount);
    System.out.println("Cart count: " + cartCount);
    Assert.assertTrue(!cartCount.equals("0"), "Cart should have items");

    System.out.println("✓ Quick add to cart flow test PASSED");
  }
}
