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
import com.kms.katalon.core.util.KeywordUtil
import java.text.SimpleDateFormat

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
// TEST OBJECTS
// ============================================================================

TestObject pushToTalk =
		findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')

TestObject chatTextArea =
		findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area')

TestObject firstNameField =
		findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')

TestObject lastNameField =
		findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name')

TestObject dobField =
		findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy')

TestObject phoneField =
		findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX')

//TestObject nextButton =
//		findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT')

TestObject otpFirstDigit =
		findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')


// Reschedule / No Slots page objects

TestObject upcomingAppointmentsHeader =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/h2_Upcoming Appointments')

TestObject patientName =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/p_Ralph Wiggum')

TestObject noUpcomingAppointmentsMessage =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/p_You have no upcoming appointments scheduled')

TestObject backButton =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/button_BACK')

TestObject bookNewAppointmentButton =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/button_Book A New Appt')

TestObject locationLabel =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/label_Location')

TestObject providerLabel =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/label_Provider')

TestObject reasonLabel =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/label_Reason')

TestObject locationDropdown =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/select_Location')

TestObject providerDropdown =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/select_Provider')

TestObject reasonDropdown =
		findTestObject('Appointment Booking/Reschedule Appt/No Slots/select_Reason')
		
		// ============================================================================
		// EXPECTED UI TEXT
		// ============================================================================
		
		String expectedUpcomingAppointmentsText =
				'Upcoming Appointments'
		
		String expectedNoUpcomingAppointmentsText =
				'You have no upcoming appointments scheduled.'
		
		String expectedLocationLabel =
				'Location*'
		
		String expectedProviderLabel =
				'Provider*'
		
		String expectedReasonLabel =
				'Reason*'

// ============================================================================
// PART 1: CHAT BOT APPOINTMENT BOOKING
// ============================================================================

KeywordUtil.logInfo('Step 1: Navigating to application URL')
CustomKeywords.'common.ChatBotBookingFlow.navigateToApp'()

KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
CustomKeywords.'common.ChatBotBookingFlow.launchChatBot'(PAGE_TIMEOUT)

KeywordUtil.logInfo("Step 3: Cancel Appointment")
CustomKeywords.'common.ChatBotBookingFlow.sendChatBotMessage'('Cancel Appointment')
WebUI.waitForElementVisible(nextButton, PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 4: Entering patient personal details')
CustomKeywords.'common.ChatBotBookingFlow.enterPatientDetails'(firstName, lastName, dob, phoneNumber, PAGE_TIMEOUT)

KeywordUtil.logInfo('Step 5: Entering OTP for verification')
CustomKeywords.'common.ChatBotBookingFlow.enterOtpAndSubmit'(otpCode, MEDIUM_TIMEOUT)
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

// ============================================================================
// REUSABLE METHOD - VERIFY NO UPCOMING APPOINTMENTS
// ============================================================================

def verifyNoUpcomingAppointments = {

	KeywordUtil.logInfo(
			'Step: Verifying Upcoming Appointments page'
	)

	WebUI.waitForElementVisible(
			upcomingAppointmentsHeader,
			PAGE_TIMEOUT
	)

	WebUI.verifyElementText(
			upcomingAppointmentsHeader,
			expectedUpcomingAppointmentsText
	)

	KeywordUtil.logInfo(
			"Verified header: '${expectedUpcomingAppointmentsText}'"
	)


	WebUI.verifyElementText(
			patientName,
			expectedName
	)

	KeywordUtil.logInfo(
			"Verified patient name: '${expectedName}'"
	)


	WebUI.verifyElementText(
			noUpcomingAppointmentsMessage,
			expectedNoUpcomingAppointmentsText
	)

	KeywordUtil.logInfo(
			"Verified message: '${expectedNoUpcomingAppointmentsText}'"
	)


	WebUI.verifyElementPresent(
			backButton,
			SHORT_TIMEOUT
	)

	KeywordUtil.logInfo(
			'Verified BACK button is displayed'
	)


	WebUI.verifyElementPresent(
			bookNewAppointmentButton,
			SHORT_TIMEOUT
	)

	KeywordUtil.logInfo(
			'Verified Book A New Appointment button is displayed'
	)
}


// ============================================================================
// PART 1: CHAT BOT APPOINTMENT BOOKING / RESCHEDULING
// ============================================================================


// ----------------------------------------------------------------------------
// STEP 1: Navigate to application
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 1: Navigate to application =========='
)

KeywordUtil.logInfo(
		"Navigating to: ${applicationUrl}"
)

WebUI.navigateToUrl(applicationUrl)

KeywordUtil.logInfo(
		'Application URL opened successfully'
)


// ----------------------------------------------------------------------------
// STEP 2: Launch Chat Bot
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 2: Launch Chat Bot =========='
)

WebUI.waitForElementVisible(
		pushToTalk,
		PAGE_TIMEOUT
)

KeywordUtil.logInfo(
		'Push to talk icon is visible'
)

WebUI.click(pushToTalk)

KeywordUtil.logInfo(
		'Chat bot launched successfully'
)


// ----------------------------------------------------------------------------
// STEP 3: Select Reschedule Appointment
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 3: Select Reschedule Appointment =========='
)

WebUI.waitForElementVisible(
		chatTextArea,
		PAGE_TIMEOUT
)

KeywordUtil.logInfo(
		'Chat input field is visible'
)

WebUI.setText(
		chatTextArea,
		'Reschedule Appointment'
)

KeywordUtil.logInfo(
		"Entered chat request: 'Reschedule Appointment'"
)

WebUI.sendKeys(
		chatTextArea,
		Keys.chord(Keys.ENTER)
)

KeywordUtil.logInfo(
		'Submitted Reschedule Appointment request'
)


// ----------------------------------------------------------------------------
// STEP 4: Enter Patient Personal Details
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 4: Enter Patient Personal Details =========='
)

WebUI.waitForElementVisible(
		firstNameField,
		PAGE_TIMEOUT
)

KeywordUtil.logInfo(
		"Entering First Name: ${firstName}"
)

WebUI.setText(
		firstNameField,
		firstName
)


KeywordUtil.logInfo(
		"Entering Last Name: ${lastName}"
)

WebUI.setText(
		lastNameField,
		lastName
)


KeywordUtil.logInfo(
		"Entering DOB: ${dob}"
)

WebUI.setText(
		dobField,
		dob
)

WebUI.sendKeys(
		dobField,
		Keys.chord(Keys.ESCAPE)
)

KeywordUtil.logInfo(
		"Entering Phone Number: ${phoneNumber}"
)

WebUI.setText(
		phoneField,
		phoneNumber
)

KeywordUtil.logInfo(
		'Patient details entered successfully'
)


// ----------------------------------------------------------------------------
// STEP 5: Submit Patient Details
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 5: Submit Patient Details =========='
)

WebUI.waitForElementClickable(
		nextButton,
		SHORT_TIMEOUT
)

WebUI.click(
		nextButton
)

KeywordUtil.logInfo(
		"Patient details submitted for ${expectedName}"
)


// ----------------------------------------------------------------------------
// STEP 6: Enter OTP
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 6: Enter OTP =========='
)

WebUI.waitForElementVisible(
		otpFirstDigit,
		PAGE_TIMEOUT
)

enterOtp()
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

// ----------------------------------------------------------------------------
// STEP 7: Verify No Upcoming Appointments
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 7: Verify No Upcoming Appointments =========='
)

verifyNoUpcomingAppointments()


// ----------------------------------------------------------------------------
// STEP 8: Click BACK
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 8: Click BACK =========='
)

WebUI.waitForElementClickable(
		backButton,
		SHORT_TIMEOUT
)

WebUI.click(
		backButton
)

KeywordUtil.logInfo(
		'BACK button clicked successfully'
)


// ----------------------------------------------------------------------------
// STEP 9: Re-enter OTP
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 9: Re-enter OTP =========='
)

WebUI.waitForElementVisible(
		otpFirstDigit,
		PAGE_TIMEOUT
)

KeywordUtil.logInfo(
		'OTP fields are available again'
)

enterOtp()
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

// ----------------------------------------------------------------------------
// STEP 10: Verify No Upcoming Appointments Again
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 10: Verify No Upcoming Appointments Again =========='
)

verifyNoUpcomingAppointments()


// ----------------------------------------------------------------------------
// STEP 11: Click Book A New Appointment
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 11: Click Book A New Appointment =========='
)

WebUI.waitForElementClickable(
		bookNewAppointmentButton,
		SHORT_TIMEOUT
)

WebUI.click(
		bookNewAppointmentButton
)

KeywordUtil.logInfo(
		'Book A New Appointment button clicked successfully'
)


// ----------------------------------------------------------------------------
// STEP 12: Verify Appointment Search Fields
// ----------------------------------------------------------------------------

KeywordUtil.logInfo(
		'========== STEP 12: Verify Appointment Search Fields =========='
)


// Location
KeywordUtil.logInfo(
		"Verifying Location label: '${expectedLocationLabel}'"
)

WebUI.verifyElementText(
		locationLabel,
		expectedLocationLabel
)

WebUI.verifyElementPresent(
		locationDropdown,
		SHORT_TIMEOUT
)

KeywordUtil.logInfo(
		'Location field verified successfully'
)


// Provider
KeywordUtil.logInfo(
		"Verifying Provider label: '${expectedProviderLabel}'"
)

WebUI.verifyElementText(
		providerLabel,
		expectedProviderLabel
)

WebUI.verifyElementPresent(
		providerDropdown,
		SHORT_TIMEOUT
)

KeywordUtil.logInfo(
		'Provider field verified successfully'
)


// Reason
KeywordUtil.logInfo(
		"Verifying Reason label: '${expectedReasonLabel}'"
)

WebUI.verifyElementText(
		reasonLabel,
		expectedReasonLabel
)

WebUI.verifyElementPresent(
		reasonDropdown,
		SHORT_TIMEOUT
)

KeywordUtil.logInfo(
		'Reason field verified successfully'
)


// ============================================================================
// TEST COMPLETION
// ============================================================================

KeywordUtil.logInfo(
		'============================================================'
)

KeywordUtil.logInfo(
		"TEST COMPLETED SUCCESSFULLY FOR PATIENT: ${expectedName}"
)

KeywordUtil.logInfo(
		'Verified: Reschedule Appointment -> OTP -> No Upcoming Appointments'
)

KeywordUtil.logInfo(
		'Verified: BACK -> OTP -> No Upcoming Appointments'
)

KeywordUtil.logInfo(
		'Verified: Book A New Appointment -> Location / Provider / Reason'
)

KeywordUtil.logInfo(
		'============================================================'
)





