package playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.List;

public class SearchResultsPWPage extends BasePWPage {
  public static final String RESULTS_CONTAINER = "div.s-main-slot.s-result-list";
  public static final String RESULT_ITEMS      =
      "div.s-main-slot.s-result-list > div[data-component-type='s-search-result']";
  public static final String FIRST_TITLE       =
      "div.s-main-slot.s-result-list > div[data-component-type='s-search-result']:nth-of-type(1) h2 a span";
  public static final String FIRST_LINK        =
      "div.s-main-slot.s-result-list > div[data-component-type='s-search-result']:nth-of-type(1) h2 a";
  public static final String FIRST_PRICE       =
      "div.s-main-slot.s-result-list > div[data-component-type='s-search-result']:nth-of-type(1) .a-price .a-offscreen";
  public static final String SORT_DROPDOWN     = "#s-result-sort-select";
  public static final String PAGINATION_NEXT   =
      "a.s-pagination-next, .s-pagination-item.s-pagination-next";

  public SearchResultsPWPage(Page page) {
    super(page);
  }

  public void openFirstResult() {
    click(FIRST_LINK);
  }

  public List<Locator> allItems() {
    return page.locator(RESULT_ITEMS).all();
  }

  public int resultCount() {
    return (int) page.locator(RESULT_ITEMS).count();
  }
}
