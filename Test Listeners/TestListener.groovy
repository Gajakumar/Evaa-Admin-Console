import com.kms.katalon.core.annotation.*
import com.kms.katalon.core.context.*
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.model.FailureHandling

class FailureScreenshotListener {

	/*
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		println testCaseContext.getTestCaseId()
		println testCaseContext.getTestCaseVariables()
		
		// ---------------------------
		// Chrome Arguments
		// ---------------------------
		List<String> args = new ArrayList<>()

		args.add("--start-maximized")
		args.add("--disable-notifications")
		args.add("--disable-infobars")
		args.add("--disable-popup-blocking")
		args.add("--no-sandbox")
		args.add("--disable-dev-shm-usage")
		args.add("--disable-gpu")
		args.add("--remote-allow-origins=*")

		RunConfiguration.setWebDriverPreferencesProperty("args", args)

		// ---------------------------
		// Chrome Preferences
		// ---------------------------
		Map<String, Object> prefs = new HashMap<>()

		prefs.put("credentials_enable_service", false)
		prefs.put("profile.password_manager_enabled", false)
		prefs.put("autofill.profile_enabled", false)
		prefs.put("autofill.credit_card_enabled", false)
		prefs.put("profile.default_content_setting_values.notifications", 2)

		RunConfiguration.setWebDriverPreferencesProperty("prefs", prefs)

		WebUI.openBrowser('')

		'Maximize the window'
		WebUI.maximizeWindow()

	}
    /**
     * Runs AFTER every test case
     */
    @AfterTestCase
    def afterTestCase(TestCaseContext testCaseContext) {

        println "⏹ Finished Test Case : " + testCaseContext.getTestCaseId()
        println "📌 Status            : " + testCaseContext.getTestCaseStatus()

        if (testCaseContext.getTestCaseStatus() != 'PASS') {

            try {
                def driver = DriverFactory.getWebDriver()

                // ✅ SAFETY CHECK
                if (driver == null || driver.getSessionId() == null) {
                    println "⚠ Browser session not available. Screenshot skipped."
                    return
                }

                String projectDir = RunConfiguration.getProjectDir()
                String failedFolder = projectDir + "/Screenshots/FAILED"
                new File(failedFolder).mkdirs()

                String testCaseName = testCaseContext.getTestCaseId()
                        .replaceAll('[^a-zA-Z0-9_]', '_')

                String timeStamp = new Date().format("yyyyMMdd_HHmmss")

                String screenshotPath =
                        failedFolder + "/" + testCaseName + "_" + timeStamp + ".png"

                WebUI.takeScreenshot(screenshotPath, FailureHandling.OPTIONAL)

                println "📸 Screenshot saved at:"
                println screenshotPath

            } catch (Exception e) {
                println "❌ Screenshot capture failed: " + e.getMessage()
            }
        }

        // Close browser safely
        try {
            if (DriverFactory.getWebDriver() != null) {
                WebUI.closeBrowser()
                println "🧹 Browser closed"
            }
        } catch (Exception e) {
            println "⚠ Browser already closed"
        }
    }

    /**
     * Runs BEFORE test suite
     */
    @BeforeTestSuite
    def beforeTestSuite(TestSuiteContext testSuiteContext) {
        println "🚀 Starting Test Suite : " + testSuiteContext.getTestSuiteId()
    }

    /**
     * Runs AFTER test suite
     */
    @AfterTestSuite
    def afterTestSuite(TestSuiteContext testSuiteContext) {
        println "🏁 Finished Test Suite : " + testSuiteContext.getTestSuiteId()
    }
}
