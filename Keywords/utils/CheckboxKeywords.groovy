package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil

class CheckboxKeywords {

	@Keyword
	static def verifyCheckboxDisabled(TestObject checkbox, boolean shouldBeDisabled) {
		try {
			// Wait for element
			WebUI.waitForElementPresent(checkbox, 10)

			// Check clickability
			boolean isClickable = WebUI.verifyElementClickable(checkbox, FailureHandling.OPTIONAL)

			// Get WebElement
			def element = WebUI.findWebElement(checkbox)

			// Execute JS safely
			boolean isDisabledAttr = WebUI.executeJavaScript(
					"return arguments[0].disabled;", Arrays.asList(element)
					)

			boolean actualDisabled = !isClickable || isDisabledAttr

			KeywordUtil.logInfo("Checkbox actual disabled: " + actualDisabled)

			// Assertion
			WebUI.verifyEqual(actualDisabled, shouldBeDisabled, FailureHandling.STOP_ON_FAILURE)
		} catch(Exception e) {
			KeywordUtil.markFailed("Error verifying checkbox state: " + e.getMessage())
		}
	}
}