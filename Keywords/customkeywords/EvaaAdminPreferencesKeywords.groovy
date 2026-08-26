package customkeywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.WebElement

import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * Keywords for logging into EVAA Admin and managing the
 * "Appointment Rescheduling" preference under Setup > Preferences.
 */
class EvaaAdminPreferencesKeywords {

	/**
	 * Logs into MaximEyes Identity if the "MaximEyes" login button is visible.
	 * Some environments skip straight past this screen, so presence is
	 * checked first rather than assuming it's always there.
	 */
	@Keyword
	def loginToMaximEyesIfVisible(int timeout) {
		TestObject maximEyesButton = findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_MaximEyes')

		if (WebUI.verifyElementPresent(maximEyesButton, 5, FailureHandling.OPTIONAL)) {
			WebUI.waitForElementClickable(maximEyesButton, timeout)
			WebUI.click(maximEyesButton)

			WebUI.waitForElementVisible(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Username_Email'), timeout)
			WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Username_Email'), 'QA_User')
			WebUI.setEncryptedText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Password'), 'V35d/XPbheASJTEPzyNXhQ==')
			WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter URL'), 'qa5')
			WebUI.click(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_Login'))

			WebUI.comment('MaximEyes login completed.')
		} else {
			WebUI.comment('MaximEyes button not visible. Skipping login.')
		}
	}

	/**
	 * Navigates to EVAA Admin, logs in, switches to the Virtual Assistant
	 * view, and opens Setup > Preferences.
	 */
	@Keyword
	def navigateToPreferences(int defaultTimeout, int pageTimeout) {
		WebUI.callTestCase(findTestCase('Test Cases/Common/Evaa VA/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)
		WebUI.comment('Navigated to EVAA Admin page.')

		WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'), defaultTimeout)
		WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'))
		WebUI.comment('Clicked "Login" button on EVAA AI page.')

		loginToMaximEyesIfVisible(pageTimeout)

		WebUI.waitForElementPresent(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), defaultTimeout)
		WebUI.verifyElementAttributeValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), 'alt', 'EVAA Logo', defaultTimeout)
		WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h1_Welcome qa5 - Admin Overview'), 'Welcome qa5 - Admin Overview', 5)
		WebUI.comment('Verified successful login and landing on Admin Overview page.')

		WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'), defaultTimeout)
		WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'))
		WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'), defaultTimeout)
		WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'))
		WebUI.comment('Switched to Virtual Assistant view.')

		WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'), defaultTimeout)
		WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'))
		WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'))
		WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
		WebUI.comment('Navigated to Setup > Preferences page.')
	}

	/** Returns whether the "Appointment Canceling" (rescheduling) checkbox is currently checked. */
	@Keyword
	boolean isReschedulingCheckboxChecked(TestObject checkbox) {
		boolean isChecked = WebUI.executeJavaScript(
				"return arguments[0].checked;",
				Arrays.asList(WebUI.findWebElement(checkbox, 5)))
		KeywordUtil.logInfo('Rescheduling checkbox state: ' + (isChecked ? 'CHECKED' : 'UNCHECKED'))
		return isChecked
	}

	/**
	 * Sets the rescheduling checkbox to the desired state and, if it changed,
	 * verifies the "Saved successfully" confirmation appears.
	 */
	@Keyword
	def setReschedulingCheckbox(TestObject checkbox, boolean shouldBeChecked) {
		boolean currentlyChecked = isReschedulingCheckboxChecked(checkbox)

		if (currentlyChecked == shouldBeChecked) {
			KeywordUtil.logInfo('Rescheduling checkbox already in desired state (' + shouldBeChecked + '). No action taken.')
			return
		}

		WebUI.click(checkbox)
		WebUI.assertElementText(findTestObject('Maximeyes Evaa Login/Page_Evaa AI/p_Saved successfully'), 'Saved successfully', 5)

		if (shouldBeChecked) {
			WebUI.verifyElementChecked(checkbox, 5)
		} else {
			WebUI.verifyElementNotChecked(checkbox, 5)
		}
		KeywordUtil.logInfo('Rescheduling checkbox set to ' + shouldBeChecked + ' and change saved.')
	}

	/**
	 * Reads the text content of any given message TestObject (e.g. the admin
	 * "rescheduling not allowed" message) and normalizes any phone number
	 * inside it for comparison purposes.
	 */
	@Keyword
	String captureMessageText(TestObject messageObject, int timeout = 5) {
		String message = WebUI.executeJavaScript(
				"return arguments[0].textContent;",
				[WebUI.findWebElement(messageObject, timeout)])

		message = normalizePhoneNumber(message)
		KeywordUtil.logInfo('Captured message text: ' + message)
		return message
	}

	/** Replaces any dynamic phone number in a message with a common "<PHONE>" placeholder for comparison. */
	@Keyword
	String normalizePhoneNumber(String message) {
		return message.replaceFirst(/(<number>|XXX-XXX-XXXX|\d{3}-\d{3}-\d{4})/, '<PHONE>')
	}

	@Keyword
	def goToDashboard() {
		WebUI.click(findTestObject('Maximeyes Evaa Login/Page_Evaa AI/a_Dashboard'))
		KeywordUtil.logInfo('Navigated back to Dashboard.')
	}

	/** Restores the rescheduling checkbox to a previously captured state (true = checked). */
	@Keyword
	def restoreReschedulingCheckbox(TestObject checkbox, boolean originalState) {
		WebUI.waitForElementVisible(checkbox, 10)
		WebElement cb = WebUiCommonHelper.findWebElement(checkbox, 10)

		if (cb.isSelected() != originalState) {
			WebUI.executeJavaScript("arguments[0].click();", Arrays.asList(cb))
		}

		if (originalState) {
			WebUI.verifyElementChecked(checkbox, 5)
		} else {
			WebUI.verifyElementNotChecked(checkbox, 5)
		}
		KeywordUtil.logInfo('Rescheduling checkbox restored to original state: ' + (originalState ? 'CHECKED' : 'UNCHECKED'))
	}
}