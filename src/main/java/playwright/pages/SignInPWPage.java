package playwright.pages;

import com.microsoft.playwright.Page;

public class SignInPWPage extends BasePWPage {
  public static final String EMAIL_INPUT       = "#ap_email";
  public static final String CONTINUE_BTN      = "#continue";
  public static final String CREATE_ACCOUNT    = "#createAccountSubmit";
  public static final String PASSWORD_INPUT    = "#ap_password";
  public static final String SIGN_IN_SUBMIT    = "#signInSubmit";
  public static final String FORGOT_PASSWORD   = "a[href*='forgotpassword'], a#auth-fpp-link-bottom";
  public static final String ERROR_BOX         = ".a-alert-error";
  public static final String HEADING           = "h1:has-text('Sign in')";

  public SignInPWPage(Page page) {
    super(page);
    navigate("/ap/signin");
  }

  public void submitEmail(String email) {
    fill(EMAIL_INPUT, email);
    click(CONTINUE_BTN);
  }

  public void submitPassword(String password) {
    fill(PASSWORD_INPUT, password);
    click(SIGN_IN_SUBMIT);
  }
}
