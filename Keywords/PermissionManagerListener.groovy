

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import com.kms.katalon.core.configuration.RunConfiguration

class PermissionManagerListener {

	static void enableBrowserPermissions() {

		String browser = RunConfiguration.getExecutionProperties().get("execution.default.executionConfiguration")

		switch(browser.toLowerCase()) {

			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions()
				chromeOptions.setExperimentalOption("prefs", getPermissionPrefs())
				DriverFactory.changeWebDriver(new ChromeDriver(chromeOptions))
				break

			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions()
				edgeOptions.setExperimentalOption("prefs", getPermissionPrefs())
				DriverFactory.changeWebDriver(new EdgeDriver(edgeOptions))
				break

			case "firefox":
				FirefoxProfile profile = new FirefoxProfile()
				profile.setPreference("permissions.default.camera", 1)
				profile.setPreference("permissions.default.microphone", 1)
				profile.setPreference("permissions.default.geo", 1)
				profile.setPreference("media.navigator.permission.disabled", true)
				profile.setPreference("media.navigator.streams.fake", true)

				FirefoxOptions fOptions = new FirefoxOptions()
				fOptions.setProfile(profile)

				DriverFactory.changeWebDriver(new FirefoxDriver(fOptions))
				break
		}
	}

	private static Map<String, Object> getPermissionPrefs() {
		Map<String, Object> prefs = new HashMap<>()

		prefs.put("profile.default_content_setting_values.media_stream_camera", 1)
		prefs.put("profile.default_content_setting_values.media_stream_mic", 1)
		prefs.put("profile.default_content_setting_values.notifications", 1)
		prefs.put("profile.default_content_setting_values.geolocation", 1)
		prefs.put("profile.default_content_setting_values.automatic_downloads", 1)
		prefs.put("profile.default_content_setting_values.popups", 1)
		prefs.put("profile.default_content_setting_values.clipboard", 1)
		prefs.put("profile.default_content_setting_values.background_sync", 1)

		return prefs
	}
}