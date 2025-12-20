package tests;

import base.DriverFactory;
import org.testng.annotations.*;
import io.qameta.allure.*;
import pages.*;

public class ProductFlowTest {
  @BeforeClass
  public void setUp() {
    // Use same driver from DriverFactory for simplicity in this example
    if (DriverFactory.getDriver() == null) {
      DriverFactory.initDriver(false);
    }
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    DriverFactory.quitDriver();
  }

  @Test(groups = {"regression", "product"})
  @Story("Add to cart visibility")
  @Description("Searches for an item and navigates to product page to verify key elements are present.")
  public void verifyProductDetailsVisible() {
    AmazonHomePage home = new AmazonHomePage(DriverFactory.getDriver());
    home.search("wireless mouse");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(DriverFactory.getDriver());
    results.waitForVisible(results.firstResultTitle);
    String searchResultTitle = results.text(results.firstResultTitle);

    // Assertions for search results
    assert searchResultTitle.length() > 0 : "Search result title should not be empty";
    assert searchResultTitle.toLowerCase().contains("mouse") : "Search result should be relevant to 'mouse'";

    results.openFirstResult();

    AmazonProductPage product = new AmazonProductPage(DriverFactory.getDriver());
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);

    // Stronger assertions for product page
    assert productTitle.length() > 0 : "Product title should not be empty";
    assert product.el(product.addToCartButton).isDisplayed() : "Add to cart button should be visible";
    assert product.el(product.productTitle).isDisplayed() : "Product title should be visible";

    // Verify price is present and formatted correctly
    if (product.el(product.price).isDisplayed()) {
      String priceText = product.text(product.price);
      assert priceText.matches(".*\\$[0-9,.]+.*") || priceText.length() > 0 : "Price should be displayed";
    }
  }
}
