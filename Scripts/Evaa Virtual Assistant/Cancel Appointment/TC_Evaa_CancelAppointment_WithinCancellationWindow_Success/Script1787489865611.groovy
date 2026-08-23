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

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/*
 * ============================================================================
 * TEST SUITE: Chat Bot Appointment Booking + MaximEyes Verification
 * ----------------------------------------------------------------------------
 * Part 1 - Books an appointment through the patient-facing chat bot
 * Part 2 - Logs into MaximEyes and verifies the appointment booked in Part 1
 *
 * The repeated chat-bot journey and MaximEyes login/search/cancel steps now
 * live in Keywords/common/ChatBotBookingFlow.groovy and
 * Keywords/common/MaximEyesPatientHelper.groovy - this script only supplies
 * its own test data and the verification values specific to this scenario.
 * ============================================================================
 */

// ------------
// TEST DATA
// ------------

int SHORT_TIMEOUT = 3
int PAGE_TIMEOUT  = 30
int MEDIUM_TIMEOUT  = 10

// Patient details
String firstName   = 'QA'
String lastName    = 'Katalon'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '9753'

// Appointment details
String location          = 'Katalon Location'
String provider          = 'Katalon Provider'
String providerFirstName = 'Katalon'
String reason            = 'Katalon Reason'
String reasonText        = 'Katalon Appointment'
String apptTime          = '08:30 AM'

// Appointment date, calculated dynamically as Today + 1 day
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.DATE, 1)
Date tomorrowDate = calendar.getTime()

String tomorrowDay      = new SimpleDateFormat('d').format(tomorrowDate)
String tomorrowFullDate = new SimpleDateFormat('MM/dd/yyyy').format(tomorrowDate)

// Expected values, all derived from the data above - never re-typed
String expectedName           = "Name: ${firstName} ${lastName}"
String expectedLocation       = "Location: ${location}"
String expectedProvider       = "Provider: ${provider} (OD)"
String expectedReason         = "Reason: ${reason}"
String expectedDate           = "Date: ${tomorrowFullDate}"
String expectedTime           = "Time: ${apptTime}"
String expectedConfirmationMsg = 'Your appointment has been booked'
String expectedCancelApptMsg   = 'Your appointment has been Canceled.'
String expectedDateTimeReason  = "${tomorrowFullDate} | ${apptTime} | ${reason}"
String expectedSpanText        = "${tomorrowFullDate} | ${reason}"
String expectedPatientLocation = "${providerFirstName}, ${location}"
String anythingElseHelpText      = 'Is there anything else I can help with?'
String shareFeedbackTitleText    = 'Share Your Feedback'
String restartChatText           = 'Restart Chat'
String removeLeadingZero(String time) {
	return time.replaceFirst(/^0/, '')
}

String apptTimeA = removeLeadingZero(apptTime)
String expectedTimeA =  "Time: ${apptTimeA}"
KeywordUtil.logInfo("Test data ready | Patient: ${firstName} ${lastName} | Appt: ${tomorrowFullDate} ${apptTime}")

TestObject nextButton = findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT')
TestObject tcActionButton = findTestObject('Appointment Booking/Reschedule Appt/button_Confirm _ Cancel _ Reschedule')
TestObject cancelApptBtn = findTestObject('Appointment Booking/Reschedule Appt/button_Cancel Appt')

// ============================================================================
// PART 1: CHAT BOT APPOINTMENT BOOKING
// ============================================================================

KeywordUtil.logInfo('Step 1: Navigating to application URL')
CustomKeywords.'common.ChatBotBookingFlow.navigateToApp'()

KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
CustomKeywords.'common.ChatBotBookingFlow.launchChatBot'(PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 3: Selecting "Book Appointment"')
CustomKeywords.'common.ChatBotBookingFlow.selectBookAppointment'(PAGE_TIMEOUT)

CustomKeywords.'common.ChatBotBookingFlow.verifyMedicalDisclaimer'()
CustomKeywords.'common.ChatBotBookingFlow.verifyBookingConfirmationPrompt'()

KeywordUtil.logInfo('Step 4: Confirming booking intent')
CustomKeywords.'common.ChatBotBookingFlow.confirmBookingIntent'(true)

KeywordUtil.logInfo('Step 5: Entering patient personal details')
CustomKeywords.'common.ChatBotBookingFlow.enterPatientDetails'(firstName, lastName, dob, phoneNumber, PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 6: Entering OTP for verification')
CustomKeywords.'common.ChatBotBookingFlow.enterOtpAndSubmit'(otpCode, MEDIUM_TIMEOUT)
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

KeywordUtil.logInfo('Step 7: Selecting Location, Provider, and Reason')
CustomKeywords.'common.ChatBotBookingFlow.selectLocationProviderReason'(location, provider, reason)
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

KeywordUtil.logInfo("Step 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")
int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentDate'(tomorrowDay, tomorrowFullDate, todayDay)

KeywordUtil.logInfo("Step 9: Selecting appointment time - ${apptTime}")
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentTime'(apptTime)

KeywordUtil.logInfo("Step 10-12: Entering reason, selecting Self Pay, and finishing booking")
CustomKeywords.'common.ChatBotBookingFlow.enterReasonAndFinishBooking'(reasonText)

// STEPS 13-19: Verify confirmation screen
KeywordUtil.logInfo('Step 13-19: Verifying confirmation screen details')
TestObject ptNameLabel = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')

CustomKeywords.'common.ChatBotBookingFlow.verifyConfirmationScreen'(
	ptNameLabel,
	[
		(ptNameLabel): expectedName,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West')): expectedLocation,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)')): expectedProvider,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason')): expectedReason,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026', ['date': tomorrowFullDate])): expectedDate,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM', ['time': apptTime])): expectedTime,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked')): expectedConfirmationMsg,
	],
	PAGE_TIMEOUT,
	SHORT_TIMEOUT
)

KeywordUtil.logInfo('Part 1 complete: Chat Bot Appointment Booking verified successfully')

KeywordUtil.logInfo("Step 2: Cancel Appointment")
CustomKeywords.'common.ChatBotBookingFlow.sendChatBotMessage'('Cancel Appointment')
WebUI.waitForElementVisible(nextButton, PAGE_TIMEOUT)
WebUI.click(nextButton)

// Step 5: Click the action button (Confirm / Cancel / Reschedule)
KeywordUtil.logInfo('Step 5: Clicking the Confirm/Cancel/Reschedule action button')
WebUI.waitForElementVisible(tcActionButton, PAGE_TIMEOUT)
WebUI.click(tcActionButton)
KeywordUtil.logInfo('Step 5 passed: Action button clicked')

// Step 6: Click  Cancel
KeywordUtil.logInfo('Step 6: Clicking the Confirm/Cancel/Reschedule action button')
WebUI.waitForElementVisible(cancelApptBtn, PAGE_TIMEOUT)
WebUI.click(cancelApptBtn)
KeywordUtil.logInfo('Step 6 passed: Cancel Appt button clicked')

def headlineText = 'Your appointment has been Canceled.'
// or headlineText = 'Your appointment has been booked'

CustomKeywords.'common.ChatBotBookingFlow.verifyConfirmationScreen'(
	ptNameLabel,
	[
		(ptNameLabel): expectedName,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_Dynamic', ['headlineText': headlineText])): expectedLocation,
//		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_Dynamic', ['headlineText': headlineText])): expectedProvider,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_Dynamic', ['headlineText': headlineText])): expectedReason,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_Dynamic', ['headlineText': headlineText, 'date': tomorrowFullDate])): expectedDate,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_Dynamic', ['headlineText': headlineText, 'time': apptTime])): expectedTimeA,
		(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Headline_Dynamic', ['headlineText': headlineText])): headlineText,
	],
	PAGE_TIMEOUT,
	SHORT_TIMEOUT
)


TestObject anythingElsePrompt = findTestObject(
	'Book Appt With Ins/EVAA.AI React/p_Is there anything else I can help with'
)

WebUI.waitForElementVisible(anythingElsePrompt, SHORT_TIMEOUT)
WebUI.verifyElementText(
	anythingElsePrompt,
	anythingElseHelpText
)


KeywordUtil.logInfo('Step 1: Navigating to application URL')
CustomKeywords.'common.ChatBotBookingFlow.navigateToApp'()

KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
CustomKeywords.'common.ChatBotBookingFlow.launchChatBot'(PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 3: Selecting "Book Appointment"')
CustomKeywords.'common.ChatBotBookingFlow.selectBookAppointment'(PAGE_TIMEOUT)

CustomKeywords.'common.ChatBotBookingFlow.verifyMedicalDisclaimer'()
CustomKeywords.'common.ChatBotBookingFlow.verifyBookingConfirmationPrompt'()

KeywordUtil.logInfo('Step 4: Confirming booking intent')
CustomKeywords.'common.ChatBotBookingFlow.confirmBookingIntent'(true)

KeywordUtil.logInfo('Step 5: Entering patient personal details')
CustomKeywords.'common.ChatBotBookingFlow.enterPatientDetails'(firstName, lastName, dob, phoneNumber, PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 6: Entering OTP for verification')
CustomKeywords.'common.ChatBotBookingFlow.enterOtpAndSubmit'(otpCode, MEDIUM_TIMEOUT)
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

KeywordUtil.logInfo('Step 7: Selecting Location, Provider, and Reason')
CustomKeywords.'common.ChatBotBookingFlow.selectLocationProviderReason'(location, provider, reason)
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

KeywordUtil.logInfo("Step 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentDate'(tomorrowDay, tomorrowFullDate, todayDay)


CustomKeywords.'common.ChatBotBookingFlow.verifyAppointmentTimeSlot'(
	apptTime,
	true
)


// ============================================================================
// PART 2: MAXIMEYES - SEARCH PATIENT, VERIFY & CANCEL APPOINTMENT
// ----------------------------------------------------------------------------
// 1. Logs into MaximEyes
// 2. Searches for the patient booked in Part 1
// 3. Verifies appointment slot details (date, time, reason, location) on the
//    search-results view and again on the scheduler day view
// 4. Cancels the appointment (Office Request) and opens OA settings
// ============================================================================

//KeywordUtil.logInfo('Step 1: Logging into MaximEyes')
//CustomKeywords.'common.MaximEyesPatientHelper.loginToMaximEyes'()
//
//KeywordUtil.logInfo("Step 2: Searching for patient '${firstName} ${lastName}'")
//CustomKeywords.'common.MaximEyesPatientHelper.searchPatient'(firstName, lastName)
//
//KeywordUtil.logInfo("Step 3: Verifying appointment slot shows '${expectedDateTimeReason}'")
//CustomKeywords.'common.MaximEyesPatientHelper.verifyAppointmentPresentInSearchResults'(
//	findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason'),
//	expectedDateTimeReason,
//	findTestObject('MaximeyesAppt/Page_MaximEyes/div_Katalon, Katalon Location'),
//	expectedPatientLocation,
//	SHORT_TIMEOUT
//)
//
//KeywordUtil.logInfo('Step 4: Navigating to Schedule module')
//CustomKeywords.'common.MaximEyesPatientHelper.navigateToSchedule'()
//
//KeywordUtil.logInfo("Step 5: Verifying appointment on scheduler shows '${expectedSpanText}'")
//CustomKeywords.'common.MaximEyesPatientHelper.verifyAppointmentOnScheduler'(
//	findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
//	expectedSpanText,
//	findTestObject('MaximeyesAppt/Page_MaximEyes/span_Katalon, Katalon Location'),
//	expectedPatientLocation
//)
//
//KeywordUtil.logInfo('Step 6: Cancelling the appointment (Office Request)')
//CustomKeywords.'common.MaximEyesPatientHelper.cancelAppointmentViaOfficeRequest'()
//
//WebUI.comment('TEST COMPLETE: All appointment detail verifications for patient "QA Katalon" passed')