package common

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import CustomKeywords
import internal.GlobalVariable as GlobalVariable

/**
 * MaximEyesPatientHelper
 * ----------------------------------------------------------------------------
 * Centralizes the "login to MaximEyes -> find a patient -> do something with
 * their appointment" flow that was previously copy-pasted (with only the
 * patient's first/last name changing) into almost every test script under
 * "Scripts/Evaa Virtual Assistant".
 *
 * WHY THIS EXISTS
 * Several test cases (new patient booking, insurance booking, self-pay
 * booking, reschedule flows, OTP flows, etc.) all end with the same block:
 *   1. Log into MaximEyes (with optional email-OTP verification)
 *   2. Search for the patient that was booked earlier in the test
 *   3. Do something with the appointment (cancel it / verify it exists /
 *      verify it does NOT exist) on both the search-results view and the
 *      Schedule module view.
 * That block was duplicated ~10+ times. If a locator changes (e.g. the
 * "Find Patient" icon, the login fields, the OTP field), every copy had to
 * be updated by hand. Now it only needs to change here.
 *
 * HOW TO USE FROM A TEST SCRIPT
 * Test scripts already have `firstName` / `lastName` variables for the
 * patient under test. Just pass them straight through, e.g.:
 *
 *   CustomKeywords.'common.MaximEyesPatientHelper.loginToMaximEyes'()
 *   CustomKeywords.'common.MaximEyesPatientHelper.searchPatient'(firstName, lastName)
 *
 * or, for the common "login + search + cancel appointment" sequence used at
 * the end of the booking test cases, in one call:
 *
 *   CustomKeywords.'common.MaximEyesPatientHelper.loginSearchAndCancelAppointment'(firstName, lastName)
 *
 * All timeouts below default to the values that were hard-coded in the
 * original scripts (10s for OTP / element waits) so behavior is unchanged;
 * override them via the optional parameters if a specific test needs to.
 */
class MaximEyesPatientHelper {

	private static final int DEFAULT_OTP_TIMEOUT     = 10
	private static final int DEFAULT_ELEMENT_TIMEOUT  = 10

	/**
	 * Logs into MaximEyes using the QA5 credentials from GlobalVariable, and
	 * transparently handles the email-OTP verification step if MaximEyes
	 * prompts for it (it doesn't always, hence the OPTIONAL wait).
	 *
	 * @param otpTimeout how long (seconds) to wait to see if an OTP field appears
	 */
	@Keyword
	def loginToMaximEyes(int otpTimeout = DEFAULT_OTP_TIMEOUT) {
		KeywordUtil.logInfo('MaximEyesPatientHelper: Logging into MaximEyes')

		WebUI.navigateToUrl(GlobalVariable.MaxUrlQA5)
		WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
		WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
		WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

		TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')

		if (WebUI.waitForElementVisible(otpField, otpTimeout, FailureHandling.OPTIONAL)) {
			KeywordUtil.logInfo('MaximEyesPatientHelper: OTP field detected - fetching verification code from email')

			String otp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(GlobalVariable.MyEmail_Id,
				GlobalVariable.Email_Key)
			KeywordUtil.logInfo("MaximEyesPatientHelper: OTP retrieved = ${otp}")

			WebUI.setText(otpField, otp)
			WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
			WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), otpTimeout)

			KeywordUtil.logInfo('MaximEyesPatientHelper: OTP verified, patient list visible')
		} else {
			KeywordUtil.logInfo('MaximEyesPatientHelper: OTP field not visible - skipping OTP fetch and entry')
		}
	}

	/**
	 * Searches for a patient by first/last name using the "Find Patient"
	 * search in MaximEyes. This is the piece that used to be re-typed in
	 * every test with only firstName/lastName swapped out.
	 */
	@Keyword
	def searchPatient(String firstName, String lastName) {
		KeywordUtil.logInfo("MaximEyesPatientHelper: Searching for patient '${firstName} ${lastName}'")

		WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_imgFindPatient'))
		WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_First Name_Preferred'), firstName)
		WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_Last Name'), lastName)
		WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/input_btnSearchPatient'))
	}

	/**
	 * Navigates from wherever MaximEyes currently is to the Schedule module,
	 * via the "recent module" dropdown.
	 */
	@Keyword
	def navigateToSchedule() {
		KeywordUtil.logInfo('MaximEyesPatientHelper: Navigating to Schedule module')

		WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
		WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))
	}

	/**
	 * Cancels the currently-open appointment via the "Cancel Appt (Office
	 * Request)" option, then opens the OA settings page. This mirrors the
	 * exact tail-end sequence that was duplicated across the booking test
	 * cases after a patient's appointment was located.
	 */
	@Keyword
	def cancelAppointmentViaOfficeRequest(int elementTimeout = DEFAULT_ELEMENT_TIMEOUT) {
		KeywordUtil.logInfo('MaximEyesPatientHelper: Opening Appointment Actions dropdown')
		def appointmentDropdown = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-dropdown fg-skyblue')
		WebUI.waitForElementClickable(appointmentDropdown, elementTimeout)
		WebUI.click(appointmentDropdown)

		KeywordUtil.logInfo("MaximEyesPatientHelper: Selecting 'Cancel Appt (Office Request)'")
		def cancelAppointment = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/div_Cancel Appt (Office Request)')
		WebUI.waitForElementClickable(cancelAppointment, elementTimeout)
		WebUI.click(cancelAppointment)

		KeywordUtil.logInfo('MaximEyesPatientHelper: Opening OA settings page')
		def settingsIcon = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-cog font20 head-icon-shadow fg-white')
		WebUI.waitForElementClickable(settingsIcon, elementTimeout)
		WebUI.click(settingsIcon)

		KeywordUtil.logInfo('MaximEyesPatientHelper: Appointment cancelled and OA page opened successfully')
	}

	/**
	 * Convenience "all-in-one" keyword: login -> search for patient ->
	 * navigate to Schedule -> cancel their appointment (Office Request) ->
	 * open OA settings. This is exactly the "Delete Booked Appointment from
	 * MaximEyes" block that used to be pasted at the bottom of the booking
	 * test scripts. Call it with the patient's name from the test case:
	 *
	 *   CustomKeywords.'common.MaximEyesPatientHelper.loginSearchAndCancelAppointment'(firstName, lastName)
	 */
	@Keyword
	def loginSearchAndCancelAppointment(String firstName, String lastName) {
		loginToMaximEyes()
		searchPatient(firstName, lastName)
		navigateToSchedule()
		cancelAppointmentViaOfficeRequest()
	}

	/**
	 * Verifies that a patient's appointment slot is present in the search
	 * results view (right after searchPatient) with the given text, and
	 * that the patient/location line matches too.
	 */
	@Keyword
	def verifyAppointmentPresentInSearchResults(TestObject appointmentSlotObject, String expectedSlotText,
			TestObject locationObject, String expectedLocationText, int timeout = 3) {
		KeywordUtil.logInfo("MaximEyesPatientHelper: Verifying appointment slot shows '${expectedSlotText}'")
		WebUI.waitForElementPresent(appointmentSlotObject, timeout)
		WebUI.assertElementText(appointmentSlotObject, expectedSlotText, timeout)

		String actualLocation = WebUI.getText(locationObject).trim()
		WebUI.verifyEqual(actualLocation, expectedLocationText)
	}

	/**
	 * Verifies that a patient's appointment slot is NOT present - used by
	 * tests that cancel a booking (or never complete one) and then confirm
	 * nothing was left behind in MaximEyes.
	 */
	@Keyword
	def verifyAppointmentNotPresent(TestObject appointmentObject, int timeout = 3) {
		KeywordUtil.logInfo('MaximEyesPatientHelper: Verifying appointment is NOT present')
		WebUI.verifyElementNotPresent(appointmentObject, timeout)
	}

	/**
	 * Verifies a patient's appointment on the Schedule module's day/calendar
	 * view: the date|reason span text, and the patient-name/location span
	 * text. This is the "Step 8/28/30"-style block that was duplicated after
	 * navigateToSchedule() in the booking test cases.
	 */
	@Keyword
	def verifyAppointmentOnScheduler(TestObject spanTextObject, String expectedSpanText,
			TestObject locationObject, String expectedLocationText, int timeout = 0) {
		KeywordUtil.logInfo("MaximEyesPatientHelper: Verifying scheduler span text matches '${expectedSpanText}'")
		WebUI.assertElementText(spanTextObject, expectedSpanText, timeout)

		KeywordUtil.logInfo("MaximEyesPatientHelper: Verifying scheduler patient/location text matches '${expectedLocationText}'")
		String actualLocation = WebUI.getText(locationObject).trim()
		WebUI.verifyEqual(actualLocation, expectedLocationText)
	}
}
