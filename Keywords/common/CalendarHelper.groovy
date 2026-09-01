package common

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class CalendarHelper {

//    @Keyword
//    def verifyPastDatesDisabled(TestObject frame) {
//
//        // Switch to chatbot iframe
//        WebUI.waitForElementPresent(frame, 30)
//		WebUI.waitForElementVisible(frame, 30)
//        WebUI.switchToFrame(frame, 30)
//
//        WebDriver driver = DriverFactory.getWebDriver()
//
//        List<WebElement> disabledDates = driver.findElements(
//                By.xpath("//button[contains(@class,'wizard-calendar-day--disabled')]")
//        )
//
//        assert disabledDates.size() > 0 : "No disabled dates found."
//
//        disabledDates.each { WebElement date ->
//            assert date.getAttribute("disabled") != null
//            KeywordUtil.logInfo("Verified past date ${date.getText()} is disabled.")
//        }
//
//        KeywordUtil.markPassed("Verified all past dates are disabled.")
//
//        // Return to main page
//        WebUI.switchToDefaultContent()
//    }
//
//    @Keyword
//    def verifyAvailableDatesEnabled(TestObject frame) {
//
//        // Switch to chatbot iframe
//        WebUI.waitForElementPresent(frame, 20)
//		WebUI.waitForElementVisible(frame, 20)
//        WebUI.switchToFrame(frame, 10)
//
//        WebDriver driver = DriverFactory.getWebDriver()
//
//        List<WebElement> availableDates = driver.findElements(
//                By.xpath("//button[contains(@class,'wizard-calendar-day--available')]")
//        )
//
//        assert availableDates.size() > 0 : "No available dates found."
//
//        availableDates.each { WebElement date ->
//            assert date.getAttribute("disabled") == null
//            KeywordUtil.logInfo("Verified available date ${date.getText()} is enabled.")
//        }
//
//        KeywordUtil.markPassed("Verified all available dates are enabled.")
//
//        // Return to main page
//        WebUI.switchToDefaultContent()
//    }
	
	@Keyword
	def verifyPastDatesDisabled(TestObject frame) {
	
		// Switch to chatbot iframe
		WebUI.waitForElementPresent(frame, 30)
		WebUI.waitForElementVisible(frame, 30)
		WebUI.switchToFrame(frame, 30)
	
		WebDriver driver = DriverFactory.getWebDriver()
	
		List<WebElement> disabledDates = driver.findElements(
				By.xpath("//button[contains(@class,'wizard-calendar-day--disabled')]")
		)
	
		boolean navigatedBack = false
	
		if (disabledDates.isEmpty()) {
			KeywordUtil.logInfo("No disabled dates in current month view (likely day 1) — navigating to previous month to verify.")
	
			WebElement prevButton = driver.findElement(By.xpath("//button[@aria-label='Previous month']"))
			prevButton.click()
			navigatedBack = true
	
			// Re-query after navigation
			disabledDates = driver.findElements(
					By.xpath("//button[contains(@class,'wizard-calendar-day--disabled')]")
			)
		}
	
		assert disabledDates.size() > 0 : "No disabled dates found even after checking previous month."
	
		disabledDates.each { WebElement date ->
			assert date.getAttribute("disabled") != null
			KeywordUtil.logInfo("Verified past date ${date.getText()} is disabled.")
		}
	
		KeywordUtil.markPassed("Verified all past dates are disabled.")
	
		// Return to the original month if we navigated away
		if (navigatedBack) {
			WebElement nextButton = driver.findElement(By.xpath("//button[@aria-label='Next month']"))
			nextButton.click()
			KeywordUtil.logInfo("Navigated back to current month after verification.")
		}
	
		// Return to main page
		WebUI.switchToDefaultContent()
	}
	
	    @Keyword
	    def verifyAvailableDatesEnabled(TestObject frame) {
	
	        // Switch to chatbot iframe
	        WebUI.waitForElementPresent(frame, 20)
			WebUI.waitForElementVisible(frame, 20)
	        WebUI.switchToFrame(frame, 10)
	
	        WebDriver driver = DriverFactory.getWebDriver()
	
	        List<WebElement> availableDates = driver.findElements(
	                By.xpath("//button[contains(@class,'wizard-calendar-day--available')]")
	        )
	
	        assert availableDates.size() > 0 : "No available dates found."
	
	        availableDates.each { WebElement date ->
	            assert date.getAttribute("disabled") == null
	            KeywordUtil.logInfo("Verified available date ${date.getText()} is enabled.")
	        }
	
	        KeywordUtil.markPassed("Verified all available dates are enabled.")
	
	        // Return to main page
	        WebUI.switchToDefaultContent()
	    }
}