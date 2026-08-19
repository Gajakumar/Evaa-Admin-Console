import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.Keys as Keys

// ============================================================================
// CONFIG
// ============================================================================
int DEFAULT_TIMEOUT = 10   // seconds - short waits for UI elements
int PAGE_TIMEOUT    = 30   // seconds - longer waits for page loads / navigation

// ============================================================================
// HELPER FUNCTIONS
// ============================================================================

/**
 * Logs into MaximEyes Identity if the "MaximEyes" login button is visible.
 * Some environments skip straight past this screen, so presence is checked
 * first rather than assuming it's always there.
 */
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
 * Navigates to EVAA Admin, logs in, switches to the Virtual Assistant view,
 * and opens Setup > Preferences. Extracted here because the original script
 * repeated this exact sequence twice (once to disable rescheduling, once to
 * restore it) — this keeps both usages in sync.
 */
def navigateToPreferences(int defaultTimeout, int pageTimeout) {
	// STEP 1: Navigate to EVAA Admin and log in via MaximEyes Identity
	WebUI.callTestCase(findTestCase('Test Cases/Common/Evaa VA/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)
	WebUI.comment('STEP 1.1: Navigated to EVAA Admin page.')

	WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'), defaultTimeout)
	WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'))
	WebUI.comment('STEP 1.2: Clicked "Login" button on EVAA AI page.')

	loginToMaximEyesIfVisible(pageTimeout)

	// STEP 2: Verify successful login and landing on Admin Overview page
	WebUI.waitForElementPresent(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), defaultTimeout)
	WebUI.verifyElementAttributeValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), 'alt', 'EVAA Logo', defaultTimeout)
	WebUI.comment('STEP 2.1: Verified EVAA logo is present with alt text "EVAA Logo".')

	WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h1_Welcome qa5 - Admin Overview'), 'Welcome qa5 - Admin Overview', 5)
	WebUI.comment('STEP 2.2: Verified "Welcome qa5 - Admin Overview" header text.')

	// STEP 3: Navigate to Virtual Assistant view
	WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'), defaultTimeout)
	WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'))
	WebUI.comment('STEP 3.1: Opened "Select an Assistant" menu.')

	WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'), defaultTimeout)
	WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'))
	WebUI.comment('STEP 3.2: Selected "Virtual Assistant".')

	// STEP 9: Navigate to Setup > Preferences
	WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'), defaultTimeout)
	WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'))
	WebUI.comment('STEP 9.1: Opened "Setup" menu.')

	WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'))
	WebUI.comment('STEP 9.2: Navigated to "Preferences" page.')

	WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
	WebUI.comment('STEP 9.3: Verified "Virtual Assistant Name" section is visible on Preferences page.')
}

// ============================================================================
// PART 1: DISABLE RESCHEDULING & CAPTURE EXPECTED MESSAGE (ADMIN SIDE)
// ============================================================================

navigateToPreferences(DEFAULT_TIMEOUT, PAGE_TIMEOUT)

TestObject checkbox = findTestObject('Maximeyes Evaa Login/Page_Evaa AI/input_h-4 w-4 shrink-0 rounded border-gray-30')

boolean isChecked = WebUI.verifyElementChecked(checkbox, 5, FailureHandling.OPTIONAL)
KeywordUtil.logInfo('Rescheduling checkbox initial state: ' + (isChecked ? 'CHECKED' : 'UNCHECKED'))

if (isChecked) {
	// Checkbox is checked -> uncheck it to disable rescheduling for this test
	WebUI.click(checkbox)
	WebUI.verifyElementNotChecked(checkbox, 5)
	WebUI.assertElementText(findTestObject('Maximeyes Evaa Login/Page_Evaa AI/p_Saved successfully'), 'Saved successfully', 5)
	KeywordUtil.logInfo('Rescheduling checkbox unchecked and change saved.')
}

// Capture the "rescheduling not allowed" message shown in Admin preferences
TestObject reschedulingNotAllowed = findTestObject('Maximeyes Evaa Login/Page_Evaa AI/textarea_Appointment rescheduling is not availab')
String reschedulingNotAllowedMessage = WebUI.executeJavaScript(
	"return arguments[0].textContent;",
	[WebUI.findWebElement(reschedulingNotAllowed, 5)]
)
KeywordUtil.logInfo('Admin "rescheduling not allowed" message: ' + reschedulingNotAllowedMessage)

// Replace the dynamic phone number with a common placeholder for comparison
reschedulingNotAllowedMessage = reschedulingNotAllowedMessage.replaceFirst(
	/(<number>|XXX-XXX-XXXX|\d{3}-\d{3}-\d{4})/,
	'<PHONE>'
)

WebUI.click(findTestObject('Maximeyes Evaa Login/Page_Evaa AI/a_Dashboard'))
KeywordUtil.logInfo('Navigated back to Dashboard.')

// ============================================================================
// PART 2: CHAT BOT APPOINTMENT BOOKING (PATIENT-FACING SITE)
// ============================================================================

// STEP 1: Navigate to the application
KeywordUtil.logInfo('Step 1: Navigating to application URL')
WebUI.navigateToUrl('https://qa5.eyeclinic.ai/')

// STEP 2: Launch chat bot
KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(pushToTalk)

// STEP 3: Request rescheduling and verify it is blocked with the expected message
KeywordUtil.logInfo('Step 3: Requesting "Reschedule Appointment" via chat')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(bookAppt, PAGE_TIMEOUT)

WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'), 'Reschedule Appointment')
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'), Keys.chord(Keys.ENTER))

String expectedReschedulingNotAllowedMessage = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/p_Appointment rescheduling is not available onli')
)
expectedReschedulingNotAllowedMessage = expectedReschedulingNotAllowedMessage.replaceFirst(
	/(<number>|XXX-XXX-XXXX|\d{3}-\d{3}-\d{4})/,
	'<PHONE>'
)
KeywordUtil.logInfo('Chat bot "rescheduling not allowed" message: ' + expectedReschedulingNotAllowedMessage)

// Compare admin-configured message against what the chat bot actually shows
WebUI.verifyEqual(reschedulingNotAllowedMessage, expectedReschedulingNotAllowedMessage)
KeywordUtil.logInfo('Verified admin message matches chat bot message.')

// STEP 4: Confirm booking intent and continue the booking flow
KeywordUtil.logInfo('Step 4: Confirming booking intent')
WebUI.click(bookAppt)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))

// STEP 5: Enter patient personal details
KeywordUtil.logInfo('Step 5: Entering patient personal details')
TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
WebUI.waitForElementVisible(firstNameField, PAGE_TIMEOUT)
// NOTE: script ends here in the original source — no fields are actually
// filled in or submitted after this point.

// ============================================================================
// PART 3: RESTORE RESCHEDULING SETTING (ADMIN SIDE - TEARDOWN)
// ============================================================================

navigateToPreferences(DEFAULT_TIMEOUT, PAGE_TIMEOUT)

// NOTE (kept as-is from the original logic): this only re-checks the box when
// it started out UNCHECKED. If it started out CHECKED, Part 1 unchecked it
// above and it is intentionally left unchecked here — i.e. the setting is
// NOT restored to its original state in that case. Flagging this in case it
// wasn't intentional; let me know if you'd like it changed to always restore
// the original state (isChecked) instead.
if (!isChecked) {
	WebUI.click(checkbox)
	WebUI.verifyElementChecked(checkbox, 5)
	WebUI.assertElementText(findTestObject('Maximeyes Evaa Login/Page_Evaa AI/p_Saved successfully'), 'Saved successfully', 5)
	KeywordUtil.logInfo('Rescheduling checkbox restored to checked and change saved.')
}
WebUI.verifyElementChecked(checkbox, 5)
