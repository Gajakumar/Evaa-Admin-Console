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
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import java.text.SimpleDateFormat
import customkeywords.EvaaAdminPreferencesKeywords
import customkeywords.ChatBotBookingKeywords
import customkeywords.InsuranceCardUploadKeywords

// ============================================================================
// CONFIG
// ============================================================================
int DEFAULT_TIMEOUT = 10   // seconds - short waits for UI elements
int PAGE_TIMEOUT    = 30   // seconds - longer waits for page loads / navigation
int LONG_TIMEOUT   = 15
String APP_URL       = 'https://qa5.eyeclinic.ai/'

EvaaAdminPreferencesKeywords admin = new EvaaAdminPreferencesKeywords()
ChatBotBookingKeywords chatBot     = new ChatBotBookingKeywords()

TestObject patientViewEditInsuranceCheckbox = findTestObject('Book Appt With Ins/Page_Evaa AI/input_Patient ViewEdit Insurance')

// ============================================================================
// PART 1: INS VIEW EDIT CHECKBOX (ADMIN SIDE)
// ============================================================================
admin.navigateToPreferences(DEFAULT_TIMEOUT, PAGE_TIMEOUT)

boolean originalCheckboxState = admin.isReschedulingCheckboxChecked(patientViewEditInsuranceCheckbox)
admin.setReschedulingCheckbox(patientViewEditInsuranceCheckbox, true)

admin.goToDashboard()

// ------------
// TEST DATA
// ------------

int SHORT_TIMEOUT = 3
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


KeywordUtil.logInfo("Step 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")
int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentDate'(tomorrowDay, tomorrowFullDate, todayDay)

KeywordUtil.logInfo("Step 9: Selecting appointment time - ${apptTime}")
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentTime'(apptTime)

KeywordUtil.logInfo("Step 10-12: Entering reason for booking")
CustomKeywords.'common.ChatBotBookingFlow.enterReasonForVisit'(reasonText)



// --- Insurance details ---
String insuranceName         = 'Aetna'
String insuredId             = '12345678'
String patientRelationship   = 'Self'
String insuranceGroupName    = 'Auto'
String insuranceEmployerName = 'Automation'
String frontCardFileName     = 'Ins Card Front Side.png'
String backCardFileName      = 'Ins Card Back Side.png'
String gender                = 'Male'

// --- Static UI copy (kept as variables so nothing is re-typed / hardcoded inline) ---
String medicalDisclaimerText     = "Online appointment booking is only for routine exam and follow up appointments and should not be used if you have any urgent or concerning medical issues. If experiencing medical issues please call our office during office hours. If outside of office hours please call 911 or visit an urgent care or emergency room for immediate assistance."
String confirmBookingPromptText  = "Do you want to proceed with booking an appointment? Yes No"
String insuranceCardPhotoText    = 'Insurance Card Photo'
String insuranceNameLabelText    = 'Insurance Name'
String frontSideLabelText        = 'Front Side'
String uploadFrontHintText       = 'Upload or take a photo of the front of your insurance card'
String backSideLabelText         = 'Back Side'
String uploadBackHintText        = 'Upload or take a photo of the back of your insurance card'
String insuranceNoteText         = 'Note: Please add clear image of card. Image size must be less than 5 MB.'
String cardScannedTitleText      = 'Insurance card scanned'
String cardScannedBodyText       = 'We filled in your insurance details from the card. Please review them on the next screen.'
String insuranceSavedTitleText   = 'Insurance Details saved successfully'
String viewAddInsurancesText     = 'View / Add Insurances'
String anythingElseHelpText      = 'Is there anything else I can help with?'
String shareFeedbackTitleText    = 'Share Your Feedback'
String restartChatText           = 'Restart Chat'

// --- Insurance coverage dates: Today -> Today + 15 days ---
Date today       = new Date()
Date futureDate  = today + 15

String todayStr = today.format("MM/dd/yyyy")
String futureDateStr = futureDate.format("MM/dd/yyyy")

InsuranceCardUploadKeywords insurance = new InsuranceCardUploadKeywords()

insurance.deleteInsuranceIfPresent()


insurance.enterInsuranceName(insuranceName)
insurance.uploadInsuranceCardImages(frontCardFileName, backCardFileName)

insurance.verifyAndContinueFromCardScanned(cardScannedTitleText, cardScannedBodyText, LONG_TIMEOUT, MEDIUM_TIMEOUT)

insurance.verifyOcrFilledInsuranceDetails(insuranceName, insuredId, firstName, lastName, dob, MEDIUM_TIMEOUT)
insurance.selectGender(gender, MEDIUM_TIMEOUT)
insurance.clickNext()

insurance.enterInsuranceCoverageDetails(patientRelationship, insuranceGroupName, insuranceEmployerName)
insurance.enterCoverageDates(todayStr, futureDateStr)

insurance.verifyAndContinueFromInsuranceSaved(insuranceSavedTitleText, viewAddInsurancesText, LONG_TIMEOUT, MEDIUM_TIMEOUT)

// ============================================================================
// PART 1: INS VIEW EDIT CHECKBOX (ADMIN SIDE)
// ============================================================================
admin.navigateToPreferences(DEFAULT_TIMEOUT, PAGE_TIMEOUT)

boolean originalCheckboxState = admin.isReschedulingCheckboxChecked(patientViewEditInsuranceCheckbox)
admin.setReschedulingCheckbox(patientViewEditInsuranceCheckbox, false)

admin.goToDashboard()

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


KeywordUtil.logInfo("Step 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")
int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentDate'(tomorrowDay, tomorrowFullDate, todayDay)

KeywordUtil.logInfo("Step 9: Selecting appointment time - ${apptTime}")
CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentTime'(apptTime)

KeywordUtil.logInfo("Step 10-12: Entering reason for booking")
CustomKeywords.'common.ChatBotBookingFlow.enterReasonForVisit'(reasonText)

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


