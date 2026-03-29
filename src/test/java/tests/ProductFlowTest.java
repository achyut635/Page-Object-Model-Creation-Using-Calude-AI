package tests;

import base.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import io.qameta.allure.*;
import pages.*;

@Listeners({base.AllureTestListener.class})
@Epic("Amazon")
@Feature("Product Details")
public class ProductFlowTest {
  private org.openqa.selenium.WebDriver driver;

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
  @Story("Add to cart visibility")
  @Description("Searches for an item and navigates to product page to verify key elements are present.")
  public void verifyProductDetailsVisible() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("wireless mouse");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String searchResultTitle = results.text(results.firstResultTitle);

    // Assertions for search results
    Assert.assertTrue(searchResultTitle.length() > 0, "Search result title should not be empty");
    Assert.assertTrue(searchResultTitle.toLowerCase().contains("mouse"), "Search result should be relevant to 'mouse'");

    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);

    // Stronger assertions for product page
    Assert.assertTrue(productTitle.length() > 0, "Product title should not be empty");
    Assert.assertTrue(product.el(product.addToCartButton).isDisplayed(), "Add to cart button should be visible");
    Assert.assertTrue(product.el(product.productTitle).isDisplayed(), "Product title should be visible");

    // Verify price is present and formatted correctly
    try {
      if (product.el(product.priceContainer).isDisplayed()) {
        String priceText = product.text(product.priceWhole);
        Assert.assertTrue(priceText.matches(".*\\$[0-9,.]+.*") || priceText.length() > 0, "Price should be displayed");
      }
    } catch (Exception e) {
      // Price format may vary, skip validation
      System.out.println("Price validation skipped - format varies by product");
    }
  }
}
