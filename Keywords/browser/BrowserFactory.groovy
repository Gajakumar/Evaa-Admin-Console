package browser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import com.kms.katalon.core.webui.driver.DriverFactory

class BrowserFactory {

    static void startCleanChrome(Boolean headless = false) {

        ChromeOptions options = new ChromeOptions()

        // =============================
        // Stability & Performance
        // =============================
        options.addArguments("--start-maximized")
        options.addArguments("--disable-infobars")
        options.addArguments("--disable-notifications")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-extensions")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-gpu")
        options.addArguments("--remote-allow-origins=*")

        if (headless) {
            options.addArguments("--headless=new")
            options.addArguments("--window-size=1920,1080")
        }

        // =============================
        // Disable Autofill & Save Prompts
        // =============================
        options.setExperimentalOption("prefs", [
                "credentials_enable_service": false,
                "profile.password_manager_enabled": false,
                "autofill.profile_enabled": false,
                "autofill.credit_card_enabled": false,
                "profile.default_content_setting_values.notifications": 2
        ])

        WebDriver driver = new ChromeDriver(options)
        DriverFactory.changeWebDriver(driver)
    }
}
