package tests;

import io.qameta.allure.*;

import base.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;

import pages.AmazonHomePage;
import pages.AmazonSearchResultsPage;
import pages.AmazonProductPage;
import pages.SignInPage;
import pages.CartPage;

@Listeners({base.AllureTestListener.class})
@Epic("Amazon")
@Feature("Shopping")
public class TestCases {
  private WebDriver driver;

  @BeforeClass
  @Parameters({"headed"}) 
  public void setUp(@Optional("false")  String headed) {
    DriverFactory.initDriver(Boolean.parseBoolean(headed));
    driver = DriverFactory.getDriver();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    DriverFactory.quitDriver();
  }

  @Test(groups = {"smoke", "regression"})
  @Story("Search and view product")
  @Owner("QA")
  @Description("Searches for a product and opens the first result, then verifies product page loads.")
  public void searchAndOpenFirstResult() {
    AmazonHomePage home = new AmazonHomePage(driver);
    home.search("mechanical keyboard");

    AmazonSearchResultsPage results = new AmazonSearchResultsPage(driver);
    results.waitForVisible(results.firstResultTitle);
    String firstResultTitle = results.text(results.firstResultTitle);
    System.out.println("First result: " + firstResultTitle);

    // Assertions for search results
    Assert.assertTrue(firstResultTitle.length() > 0, "First result title should not be empty");
    Assert.assertTrue(firstResultTitle.toLowerCase().contains("keyboard"), "First result should be relevant to search term");
    Assert.assertTrue(results.allItems().size() > 0, "Search results should contain at least one item");

    results.openFirstResult();
    AmazonProductPage product = new AmazonProductPage(driver);
    product.waitForVisible(product.productTitle);
    String productTitle = product.text(product.productTitle);
    System.out.println("Product: " + productTitle);

    // Assertions for product page
    Assert.assertTrue(productTitle.length() > 0, "Product title should not be empty");
    Assert.assertTrue(product.el(product.productTitle).isDisplayed(), "Product title should be visible");
    Assert.assertTrue(product.el(product.addToCartButton).isDisplayed(), "Add to cart button should be visible");
  }

  @Test(groups = {"smoke"})
  @Story("Auth and Cart visibility")
  @Owner("QA")
  @Description("Navigates to Sign In page and Cart page to demonstrate element references and Allure attachments.")
  public void signInAndCartScaffolding() {
    SignInPage signin = new SignInPage(driver);
    signin.gotoPath("/ap/signin");
    signin.waitForVisible(signin.emailInput);

    // Assertions for sign-in page
    Assert.assertTrue(signin.el(signin.emailInput).isDisplayed(), "Email input should be visible");
    Assert.assertTrue(signin.el(signin.continueButton).isDisplayed(), "Continue button should be visible");
    Assert.assertTrue(signin.el(signin.createAccountButton).isDisplayed(), "Create account button should be visible");

    CartPage cart = new CartPage(driver);
    cart.open();
    cart.waitForVisible(cart.cartSubTotalLabel);
    String subtotalLabel = cart.text(cart.cartSubTotalLabel);
    System.out.println("Cart subtotal label: " + subtotalLabel);

    // Assertions for cart page
    Assert.assertTrue(subtotalLabel.length() > 0, "Cart subtotal label should not be empty");
    Assert.assertTrue(cart.el(cart.headerCartLink).isDisplayed(), "Cart link should be visible");
  }
}
