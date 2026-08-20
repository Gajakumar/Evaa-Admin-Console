import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable
import java.text.SimpleDateFormat

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/*
 * ============================================================================
 * TEST SUITE: Chat Bot Appointment Booking + MaximEyes Verification
 * ----------------------------------------------------------------------------
 * Part 1 - Books an appointment through the patient-facing chat bot
 * Part 2 - Logs into MaximEyes and verifies the appointment booked in Part 1
 * ============================================================================
 */

// ------------
// TEST DATA 
// ------------

// Timeouts
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int PAGE_TIMEOUT   = 15

// Patient details
String firstName   = 'Jennifer'
String lastName    = 'Lewis'
String patientAge  = '31yo'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '9753'

// Appointment details
String location         = 'Katalon Location'
String provider         = 'Katalon Provider'
String providerFirstName = 'Katalon'
String reason            = 'Katalon Reason'
String reasonText = 'Katalon Appointment'
String apptTime   = '11:30 AM'

// Appointment date, calculated dynamically as Today + 1 day
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.DATE, 1)
Date tomorrowDate = calendar.getTime()

String tomorrowDay      = new SimpleDateFormat('d').format(tomorrowDate)
String tomorrowFullDate = new SimpleDateFormat('MM/dd/yyyy').format(tomorrowDate)

// Expected values, all derived from the data above - never re-typed
String expectedName            = "Name: ${firstName} ${lastName}"
String expectedLocation         = "Location: ${location}"
String expectedProvider         = "Provider: ${provider} (OD)"
String expectedReason           = "Reason: ${reason}"
String expectedDate             = "Date: ${tomorrowFullDate}"
String expectedTime             = "Time: ${apptTime}"
String expectedConfirmationMsg  = 'Your appointment has been booked'
String expectedDateTimeReason   = "${tomorrowFullDate} | ${apptTime} | ${reason}"
String expectedPatientLocation  = "${providerFirstName}, ${location}"

KeywordUtil.logInfo("Test data ready | Patient: ${firstName} ${lastName} | Appt: ${tomorrowFullDate} ${apptTime}")

// ============================================================================
// PART 1: CHAT BOT APPOINTMENT BOOKING
// ============================================================================

// STEP 1: Navigate to the application
KeywordUtil.logInfo('Step 1: Navigating to application URL')
WebUI.navigateToUrl('https://qa5.eyeclinic.ai/')

// STEP 2: Launch chat bot
KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(pushToTalk)

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('Step 3: Selecting "Book Appointment"')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(bookAppt)

// Verify Medical Disclaimer is displayed
KeywordUtil.logInfo('Verifing Medical Disclaimer is displayed')
String  MedicalDisclaimer = "Online appointment booking is only for routine exam and follow up appointments and should not be used if you have any urgent or concerning medical issues. If experiencing medical issues please call our office during office hours. If outside of office hours please call 911 or visit an urgent care or emergency room for immediate assistance."

String actualDisclaimer = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/Medical Disclaimer')
).replaceAll("\\s+", " ").trim()

WebUI.verifyMatch(actualDisclaimer, MedicalDisclaimer, false)

// Verify confirming message is displayed with Yes and No
KeywordUtil.logInfo('Verifing confirming message is displayed with Yes and No')
String expectedConfirming = "Do you want to proceed with booking an appointment? Yes No"

String actualConfirming = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/div_Do you want to proceed with booking an appoi')
).replaceAll("\\s+", " ").trim()

WebUI.verifyMatch(actualConfirming, expectedConfirming, false)

// STEP 4: Confirm booking intent
KeywordUtil.logInfo('Step 4: Confirming booking intent')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))

// STEP 5: Enter patient personal details
KeywordUtil.logInfo('Step 5: Entering patient personal details')
TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
WebUI.waitForElementVisible(firstNameField, PAGE_TIMEOUT)

WebUI.setText(firstNameField, firstName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name'), lastName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), dob)
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), Keys.chord(Keys.ESCAPE))
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX'), phoneNumber)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("Step 5: Submitted - ${firstName} ${lastName}, DOB ${dob}, Phone ${phoneNumber}")

// STEP 6: Enter OTP (looped instead of 4 near-identical calls)
KeywordUtil.logInfo('Step 6: Entering OTP for verification')
TestObject otpFirstDigit = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')
WebUI.waitForElementVisible(otpFirstDigit, PAGE_TIMEOUT)

otpCode.eachWithIndex { String digit, int i ->
    WebUI.sendKeys(findTestObject("Appointment Booking/Chat Bot Appt Book/input_otp-${i}"), digit)
}
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()
KeywordUtil.logInfo("Step 6: OTP '${otpCode}' entered and submitted")

// ---------------------------------------------------------------------------
// STEP 7: Select Location, Provider, and Reason for visit
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 7: Selecting Location, Provider, and Reason')

KeywordUtil.logInfo('Step 7a: Selecting Location by visible text Katalon Location')
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Location'), location, false)
KeywordUtil.logInfo('Step 7a: Location selected successfully')

KeywordUtil.logInfo('Step 7b: Selecting Provider Katalon Provider')
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Provider'), provider, false)
KeywordUtil.logInfo('Step 7b: Provider selected successfully')

KeywordUtil.logInfo('Step 7c: Selecting Reason Katalon Reason')
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Reason'), reason, false)
KeywordUtil.logInfo('Step 7c: Reason selected successfully')

//KeywordUtil.logInfo('Step 7: Selecting Location, Provider, and Reason')
//
//KeywordUtil.logInfo('Step 7a: Selecting Location by visible text Katalon Location')
//CustomKeywords.'common.DropdownHelper.selectIfMultiple'(
//	findTestObject('Appointment Booking/Chat Bot Appt Book/select_Location'),
//	location,
//	'Location'
//)
//
//KeywordUtil.logInfo('Step 7b: Selecting Provider Katalon Provider')
//CustomKeywords.'common.DropdownHelper.selectIfMultiple'(
//	findTestObject('Appointment Booking/Chat Bot Appt Book/select_Provider'),
//	provider,
//	'Provider'
//)
//
//KeywordUtil.logInfo('Step 7c: Selecting Reason Katalon Reason')
//CustomKeywords.'common.DropdownHelper.selectIfMultiple'(
//	findTestObject('Appointment Booking/Chat Bot Appt Book/select_Reason'),
//	reason,
//	'Appointment Reason'
//)

KeywordUtil.logInfo('Step 7d: Clicking "NEXT" to proceed to date selection')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()
KeywordUtil.logInfo("Step 7: Selected Location='${location}', Provider='${provider}', Reason='${reason}'")

// STEP 8: Select appointment date (Today + 1), handling month rollover
KeywordUtil.logInfo("Step 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")

//Verify Past dates are disbaled
KeywordUtil.logInfo('Step 8a: Verifing Past dates are disbaled')
CustomKeywords.'common.CalendarHelper.verifyPastDatesDisabled'(
    findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId')
)

//Verify todays date and future dates are enabled
KeywordUtil.logInfo('Step 8b: Verifing todays date and future dates are enabled')
CustomKeywords.'common.CalendarHelper.verifyAvailableDatesEnabled'(
    findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId')
)

int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
if (Integer.parseInt(tomorrowDay) < todayDay) {
    KeywordUtil.logInfo('Step 8: Month rollover detected - navigating to next month')
    WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
}

WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22', ['day': tomorrowDay]))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("Step 8: Date '${tomorrowFullDate}' submitted")

// STEP 9: Select appointment time
KeywordUtil.logInfo("Step 9: Selecting appointment time - ${apptTime}")
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_10_30 AM', ['time': apptTime]))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

// STEP 10: Enter reason/symptoms free-text
KeywordUtil.logInfo("Step 10: Entering reason for visit - '${reasonText}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/textarea_Describe your symptoms or reason for th'), reasonText)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

// STEP 11: Select payment option
KeywordUtil.logInfo('Step 11: Selecting "Self Pay / No Insurance Available"')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Self Pay_No Insurance Available'))

// STEP 12: Finish booking
KeywordUtil.logInfo('Step 12: Finishing booking')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_FINISH BOOKING'))

// STEPS 13-19: Verify confirmation screen
KeywordUtil.logInfo('Step 13-19: Verifying confirmation screen details')
TestObject ptNameLabel = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')
WebUI.waitForElementVisible(ptNameLabel, PAGE_TIMEOUT)

WebUI.assertElementText(ptNameLabel, expectedName, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West'), expectedLocation, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)'), expectedProvider, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason'), expectedReason, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026', ['date': tomorrowFullDate]), expectedDate, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM', ['time': apptTime]), expectedTime, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked'), expectedConfirmationMsg, SHORT_TIMEOUT)

KeywordUtil.logInfo('Part 1 complete: Chat Bot Appointment Booking verified successfully')
KeywordUtil.markPassed('Chat Bot Appointment Booking test case passed successfully')


// ============================================================================
// PART 2: MAXIMEYES - SEARCH PATIENT, VERIFY & UPDATE APPOINTMENT DETAILS
// ----------------------------------------------------------------------------
// 1. Logs into MaximEyes
// 2. Searches for the patient booked in Part 1
// 3. Verifies appointment slot details (date, time, reason, location) on the scheduler
// 4. Opens the appointment popup and verifies dropdown/field values
// 5. Verifies appointment details after closing the popup (calendar/day view)
// ============================================================================

// STEP 1: Login to MaximEyes
KeywordUtil.logInfo('Step 1: Logging into MaximEyes')
WebUI.navigateToUrl(GlobalVariable.MaxUrlQA5)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')

// Wait for OTP field to appear (max 10 seconds)
if (WebUI.waitForElementVisible(otpField, 10, FailureHandling.OPTIONAL)) {

	String otp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(
		GlobalVariable.MyEmail_Id,
		GlobalVariable.Email_Key
	)

	println("OTP = " + otp)

	WebUI.setText(otpField, otp)
	WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
	
	WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), 10)

} else {
	println("OTP field is not visible. Skipping OTP fetch and entry.")
}

// STEP 2: Search for the patient booked in Part 1
KeywordUtil.logInfo("Step 2: Searching for patient '${firstName} ${lastName}'")
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_imgFindPatient'))
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_First Name_Preferred'), firstName)
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_Last Name'), lastName)
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/input_btnSearchPatient'))

// STEP 3: Verify appointment slot details on the search/results view
KeywordUtil.logInfo("Step 3: Verifying appointment slot shows '${expectedDateTimeReason}'")

WebUI.waitForElementPresent(
	findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason'),
	SHORT_TIMEOUT)

WebUI.assertElementText(
    findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason'),
    expectedDateTimeReason,
    SHORT_TIMEOUT)

String actualLocationOV = WebUI.getText(findTestObject('MaximeyesAppt/Page_MaximEyes/div_Katalon, Katalon Location')).trim()
WebUI.verifyEqual(actualLocationOV, expectedPatientLocation)

// STEP 4: Navigate to the Schedule module
KeywordUtil.logInfo('Step 4: Navigating to Schedule module')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))


// ---------------------------------------------------------------------------
// STEP 8: Verify appointment details on the scheduler day/calendar view
// ---------------------------------------------------------------------------
String expectedSpanText = "${tomorrowFullDate} | ${reason}"

WebUI.comment('STEP 28: Verify appointment span text matches "07/23/2026 | Katalon Reason"')
WebUI.assertElementText(
	findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
	expectedSpanText,
	0)

WebUI.comment('STEP 30: Verify patient/location span text matches "Katalon, Katalon Location"')
String actualLocation = WebUI.getText(
    findTestObject('MaximeyesAppt/Page_MaximEyes/span_Katalon, Katalon Location')
).trim()

WebUI.verifyEqual(actualLocation, expectedPatientLocation)

//=========================
// Open Appointment Actions Dropdown
//=========================
KeywordUtil.logInfo("Step 1: Clicking Appointment Actions dropdown.")

def appointmentDropdown = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-dropdown fg-skyblue')

WebUI.waitForElementClickable(appointmentDropdown, 10)
WebUI.click(appointmentDropdown)

KeywordUtil.logInfo("Appointment Actions dropdown opened successfully.")


//=========================
// Select 'Cancel Appt (Office Request)'
//=========================
KeywordUtil.logInfo("Step 2: Selecting 'Cancel Appt (Office Request)'.")

def cancelAppointment = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/div_Cancel Appt (Office Request)')

WebUI.waitForElementClickable(cancelAppointment, 10)
WebUI.click(cancelAppointment)

KeywordUtil.logInfo("'Cancel Appt (Office Request)' selected successfully.")


//=========================
// Navigate to OA
//=========================
KeywordUtil.logInfo("Step 3: Clicking Settings icon OA.")

def settingsIcon = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-cog font20 head-icon-shadow fg-white')

WebUI.waitForElementClickable(settingsIcon, 10)
WebUI.click(settingsIcon)

KeywordUtil.logInfo("OA page opened successfully.")

WebUI.comment('TEST COMPLETE: All appointment detail verifications for patient "QA Katalon" passed')