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
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.util.KeywordUtil
import java.text.SimpleDateFormat
import com.kms.katalon.core.configuration.RunConfiguration
import org.openqa.selenium.Keys
import java.text.SimpleDateFormat
import appointment.AppointmentKeywords

// ============================================================================
// TEST DATA
// ============================================================================

// --- Timeouts ---
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int LONG_TIMEOUT   = 15
int PAGE_TIMEOUT   = 30

// --- Patient details ---
String firstName   = 'Daisy'
String lastName    = 'Brown'
String patientAge  = '31yo'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '9753'
String gender      = 'Male'

// --- Appointment details ---
String location          = 'Katalon Location'
String provider          = 'Katalon Provider'
String providerFirstName = 'Katalon'
String reason            = 'Katalon Reason'
String reasonText        = 'Katalon Appointment'
String apptTime          = '11:30 AM'

// --- Insurance details ---
String insuranceName         = 'Aetna'
String insuredId             = '12345678'
String patientRelationship   = 'Self'
String insuranceGroupName    = 'Auto'
String insuranceEmployerName = 'Automation'
String frontCardFileName     = 'Ins Card Front Side.png'
String backCardFileName      = 'Ins Card Back Side.png'

// --- Appointment date, calculated dynamically as Today + 1 day ---
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.DATE, 1)
Date tomorrowDate = calendar.getTime()

String tomorrowDay      = new SimpleDateFormat('d').format(tomorrowDate)
String tomorrowFullDate = new SimpleDateFormat('MM/dd/yyyy').format(tomorrowDate)

// --- Insurance coverage dates: Today -> Today + 15 days ---
Date today       = new Date()
Date futureDate  = today + 15

String todayStr = today.format("MM/dd/yyyy")
String futureDateStr = futureDate.format("MM/dd/yyyy")

String coverageStartDay   = new SimpleDateFormat('dd').format(today)
String coverageStartMonth = new SimpleDateFormat('MM').format(today)
String coverageStartYear  = new SimpleDateFormat('yyyy').format(today)

String coverageEndDay     = new SimpleDateFormat('dd').format(futureDate)
String coverageEndMonth   = new SimpleDateFormat('MM').format(futureDate)
String coverageEndYear    = new SimpleDateFormat('yyyy').format(futureDate)

// --- Expected values, all derived from the data above - never re-typed ---
String expectedName           = "Name: ${firstName} ${lastName}"
String expectedLocation        = "Location: ${location}"
String expectedProvider        = "Provider: ${provider} (OD)"
String expectedReason          = "Reason: ${reason}"
String expectedDate            = "Date: ${tomorrowFullDate}"
String expectedTime            = "Time: ${apptTime}"
String expectedConfirmationMsg = 'Your appointment has been booked'
String expectedDateTimeReason  = "${tomorrowFullDate} | ${apptTime} | ${reason}"
String expectedPatientLocation = "${providerFirstName}, ${location}"

// --- Static UI copy (kept as variables so nothing is re-typed / hardcoded inline) ---

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

KeywordUtil.logInfo("Test data ready | Patient: ${firstName} ${lastName} | Appt: ${tomorrowFullDate} ${apptTime}")

// ============================================================================
// PART 1: CHAT BOT APPOINTMENT BOOKING
// ============================================================================

// STEP 1: Navigate to the application
KeywordUtil.logInfo('STEP 1: Navigating to application URL')
WebUI.navigateToUrl('https://qa5.eyeclinic.ai/')
KeywordUtil.logInfo('STEP 1: Application URL loaded successfully')

// STEP 2: Launch chat bot via "Push to talk" icon
KeywordUtil.logInfo('STEP 2: Launching chat bot via "Push to talk" icon')
TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(pushToTalk)
KeywordUtil.logInfo('STEP 2: Chat bot launched successfully')

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('STEP 3: Selecting "Book Appointment"')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(bookAppt, PAGE_TIMEOUT)
WebUI.click(bookAppt)
KeywordUtil.logInfo('STEP 3: "Book Appointment" option selected')

//Verify Disclaimer And Confirmation Prompt
AppointmentKeywords appointmentKeywords = new AppointmentKeywords()
appointmentKeywords.verifyDisclaimerAndConfirmationPrompt()

// STEP 4: Confirm booking intent
KeywordUtil.logInfo('STEP 4: Confirming booking intent by clicking "Yes"')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))
KeywordUtil.logInfo('STEP 4: Booking intent confirmed')

// STEP 5: Enter patient personal details
KeywordUtil.logInfo('STEP 5: Entering patient personal details')
TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
WebUI.waitForElementVisible(firstNameField, LONG_TIMEOUT)

WebUI.setText(firstNameField, firstName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name'), lastName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), dob)
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), Keys.chord(Keys.ESCAPE))
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX'), phoneNumber)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 5: Submitted - ${firstName} ${lastName}, DOB ${dob}, Phone ${phoneNumber}")

// STEP 6: Enter OTP (looped instead of repeating near-identical calls)
KeywordUtil.logInfo('STEP 6: Entering OTP for verification')
TestObject otpFirstDigit = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')
WebUI.waitForElementVisible(otpFirstDigit, LONG_TIMEOUT)

otpCode.eachWithIndex { String digit, int i ->
	WebUI.sendKeys(findTestObject("Appointment Booking/Chat Bot Appt Book/input_otp-${i}"), digit)
}
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 6: OTP '${otpCode}' entered and submitted")

// ---------------------------------------------------------------------------
// STEP 7: Select Location, Provider, and Reason for visit
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('STEP 7: Selecting Location, Provider, and Reason')

KeywordUtil.logInfo("STEP 7a: Selecting Location '${location}'")
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Location'), location, false)
KeywordUtil.logInfo('STEP 7a: Location selected successfully')

KeywordUtil.logInfo("STEP 7b: Selecting Provider '${provider}'")
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Provider'), provider, false)
KeywordUtil.logInfo('STEP 7b: Provider selected successfully')

KeywordUtil.logInfo("STEP 7c: Selecting Reason '${reason}'")
WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Reason'), reason, false)
KeywordUtil.logInfo('STEP 7c: Reason selected successfully')

KeywordUtil.logInfo('STEP 7d: Clicking "NEXT" to proceed to date selection')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'(LONG_TIMEOUT)
KeywordUtil.logInfo("STEP 7: Selected Location='${location}', Provider='${provider}', Reason='${reason}'")

// ---------------------------------------------------------------------------
// STEP 8: Select appointment date (Today + 1), handling month rollover
// ---------------------------------------------------------------------------
KeywordUtil.logInfo("STEP 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")

TestObject calendarIframe = findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId')
// Wait for the calendar iframe itself instead of a blind sleep - faster & more reliable
//WebUI.waitForElementVisible(calendarIframe, LONG_TIMEOUT)


// STEP 8a: Verify past dates are disabled
KeywordUtil.logInfo('STEP 8a: Verifying past dates are disabled')
CustomKeywords.'common.CalendarHelper.verifyPastDatesDisabled'(calendarIframe)
KeywordUtil.logInfo('STEP 8a: Past dates confirmed disabled')

// STEP 8b: Verify today's date and future dates are enabled
KeywordUtil.logInfo("STEP 8b: Verifying today's date and future dates are enabled")
CustomKeywords.'common.CalendarHelper.verifyAvailableDatesEnabled'(calendarIframe)
KeywordUtil.logInfo('STEP 8b: Available dates confirmed enabled')

int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
if (Integer.parseInt(tomorrowDay) < todayDay) {
	KeywordUtil.logInfo('STEP 8c: Month rollover detected - navigating to next month')
	WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
	KeywordUtil.logInfo('STEP 8c: Navigated to next month')
}

WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22', ['day': tomorrowDay]))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 8: Date '${tomorrowFullDate}' submitted")

// STEP 9: Select appointment time
KeywordUtil.logInfo("STEP 9: Selecting appointment time - ${apptTime}")
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_10_30 AM', ['time': apptTime]))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 9: Time '${apptTime}' submitted")

// STEP 10: Enter reason/symptoms free-text
KeywordUtil.logInfo("STEP 10: Entering reason for visit - '${reasonText}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/textarea_Describe your symptoms or reason for th'), reasonText)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('STEP 10: Reason for visit submitted')

// ============================================================================
// PART 2: INSURANCE CARD UPLOAD & DETAILS
// ============================================================================

//Delete Insurance if already available
TestObject deleteInsuranceBtn = findTestObject('Appointment Booking/Delete Ins/Delete Added Ins/button_Delete insurance')

if (WebUI.verifyElementVisible(deleteInsuranceBtn, FailureHandling.OPTIONAL)) {
	WebUI.waitForElementClickable(deleteInsuranceBtn, 10)
	WebUI.click(deleteInsuranceBtn)
}

// STEP 11: Verify insurance card upload screen elements
KeywordUtil.logInfo('STEP 11: Verifying insurance card upload screen elements')
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Insurance Card Photo'), insuranceCardPhotoText, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/label_Insurance Name'), insuranceNameLabelText, 0)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Front Side'), frontSideLabelText, 0)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Upload or take a photo of the front of your in'), uploadFrontHintText, 0)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Back Side'), backSideLabelText, 0)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Upload or take a photo of the back of your ins'), uploadBackHintText, 0)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Note_ Please add clear image of card. Image si'), insuranceNoteText, 0)
KeywordUtil.logInfo('STEP 11: Insurance card upload screen elements verified successfully')

// STEP 12: Enter insurance name
KeywordUtil.logInfo("STEP 12: Entering Insurance Name - '${insuranceName}'")
WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter Insurance Name'), insuranceName)
KeywordUtil.logInfo('STEP 12: Insurance Name entered successfully')

// STEP 13: Upload insurance card images (front & back)
KeywordUtil.logInfo('STEP 13: Uploading insurance card images (front & back)')
String projectDir = RunConfiguration.getProjectDir()
File baseDir = new File(projectDir, 'Include/TestFiles')

def uploadFileTestCloud(TestObject uploadObj, File baseDir, String fileName) {
	assert uploadObj != null : '❌ Upload input TestObject is NULL'

	File fileToUpload = new File(baseDir, fileName)
	assert fileToUpload.exists() && fileToUpload.isFile() :
			"❌ Upload file not found: ${fileToUpload.absolutePath}"

	KeywordUtil.logInfo("☁ TestCloud uploading: ${fileToUpload.absolutePath}")

	CustomKeywords.'com.katalon.testcloud.FileExecutor.uploadFileToWeb'(
		uploadObj,
		fileToUpload.absolutePath
	)
}

// Upload both card images in one pass instead of two near-duplicate calls
[
	1: frontCardFileName,
	2: backCardFileName
].each { int inputIndex, String fileName ->
	TestObject uploadInput = findTestObject('Appointment Booking/Chat Bot Appt Book/Upload Input', ['index': inputIndex])
	uploadFileTestCloud(uploadInput, baseDir, fileName)
	KeywordUtil.logInfo("STEP 13: Uploaded file '${fileName}' successfully")
}

WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
KeywordUtil.logInfo('STEP 13: Insurance card images submitted')

// STEP 14: Verify insurance card scan confirmation
KeywordUtil.logInfo('STEP 14: Verifying insurance card was scanned successfully')
TestObject continueBtn = findTestObject('Book Appt With Ins/EVAA.AI React/button_Continue')
WebUI.waitForElementVisible(continueBtn, LONG_TIMEOUT)

WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), cardScannedTitleText, MEDIUM_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_We filled in your insurance details from the c'), cardScannedBodyText, 0)
KeywordUtil.logInfo('STEP 14: Insurance card scan confirmation verified')

WebUI.click(continueBtn)
KeywordUtil.logInfo('STEP 14: Continued to review insurance details')

// STEP 15: Verify OCR-filled insurance details, then select Gender

String firstNameA = 'QA'
String lastNameA = 'Katalon'
KeywordUtil.logInfo('STEP 15: Verifying insurance details auto-filled from the scanned card')

WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insurance Name'), 'defaultValue', insuranceName, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insured ID'), 'defaultValue', insuredId, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_First Name'), 'defaultValue', firstNameA, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Last Name'), 'defaultValue', lastNameA, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_mm_dd_yyyy'), 'defaultValue', dob, MEDIUM_TIMEOUT)
KeywordUtil.logInfo('STEP 15: Insurance details verified against expected patient data')

KeywordUtil.logInfo("STEP 15a: Selecting Gender - '${gender}'")
WebUI.selectOptionByLabel(
    findTestObject("Book Appt With Ins/EVAA.AI React/select_Gender"),
    gender,
    false
)
WebUI.verifyOptionSelectedByLabel(findTestObject("Book Appt With Ins/EVAA.AI React/select_Gender"), gender, false, MEDIUM_TIMEOUT)

KeywordUtil.logInfo('STEP 15a: Gender selected and verified successfully')

WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
KeywordUtil.logInfo('STEP 15: Proceeding to insurance coverage details')

// STEP 16: Enter insurance coverage details
KeywordUtil.logInfo("STEP 16: Selecting Patient Relationship to insured - '${patientRelationship}'")
WebUI.selectOptionByValue(findTestObject('Book Appt With Ins/EVAA.AI React/select_Patient Relationship to insured'), patientRelationship, false)
KeywordUtil.logInfo('STEP 16: Patient Relationship selected successfully')

KeywordUtil.logInfo("STEP 16a: Entering Insurance Group Name - '${insuranceGroupName}'")
WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter'), insuranceGroupName)

KeywordUtil.logInfo("STEP 16b: Entering Insurance Employer Name - '${insuranceEmployerName}'")
WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter_1'), insuranceEmployerName)
KeywordUtil.logInfo('STEP 16: Insurance group/employer details entered successfully')

// STEP 17: Enter Coverage Start & End Dates (Today -> Today + 15 days)
KeywordUtil.logInfo("STEP 17: Entering Coverage Start Date (${coverageStartMonth}/${coverageStartDay}/${coverageStartYear})")



TestObject coverageStartDate = findTestObject('Book Appt With Ins/EVAA.AI React/input_Coverage Start Date')

WebUI.setText(coverageStartDate, todayStr)
WebUI.sendKeys(coverageStartDate, Keys.chord(Keys.ESCAPE))
KeywordUtil.logInfo('STEP 17: Coverage Start Date entered successfully')

KeywordUtil.logInfo("STEP 17a: Entering Coverage End Date (${coverageEndMonth}/${coverageEndDay}/${coverageEndYear})")
TestObject coverageEndDate = findTestObject('Book Appt With Ins/EVAA.AI React/input_Coverage End Date')
WebUI.setText(coverageEndDate, futureDateStr)
WebUI.sendKeys(coverageEndDate, Keys.chord(Keys.ESCAPE))
KeywordUtil.logInfo('STEP 17a: Coverage End Date entered successfully')

WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
KeywordUtil.logInfo('STEP 17: Insurance coverage details submitted')

// STEP 18: Verify insurance details saved successfully
KeywordUtil.logInfo('STEP 18: Verifying insurance details were saved successfully')
WebUI.waitForElementVisible(continueBtn, LONG_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), insuranceSavedTitleText, MEDIUM_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/button_View _ Add Insurances'), viewAddInsurancesText, MEDIUM_TIMEOUT)
KeywordUtil.logInfo('STEP 18: Insurance details save confirmed')

WebUI.click(continueBtn)
KeywordUtil.logInfo('STEP 18: Continued to appointment confirmation screen')

// ============================================================================
// PART 3: CONFIRMATION & CLOSEOUT
// ============================================================================

// STEP 19: Verify appointment confirmation screen details
KeywordUtil.logInfo('STEP 19: Verifying appointment confirmation screen details')
TestObject ptNameLabel = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')
WebUI.waitForElementVisible(ptNameLabel, LONG_TIMEOUT)

WebUI.assertElementText(ptNameLabel, expectedName, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West'), expectedLocation, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)'), expectedProvider, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason'), expectedReason, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026', ['date': tomorrowFullDate]), expectedDate, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM', ['time': apptTime]), expectedTime, SHORT_TIMEOUT)
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked'), expectedConfirmationMsg, SHORT_TIMEOUT)
KeywordUtil.logInfo('STEP 19: Appointment confirmation details verified successfully')

// STEP 20: Decline further assistance
KeywordUtil.logInfo('STEP 20: Verifying "anything else" prompt and declining further assistance')
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Is there anything else I can help with'), anythingElseHelpText, MEDIUM_TIMEOUT)

TestObject noButton = findTestObject('Book Appt With Ins/EVAA.AI React/button_No')
WebUI.waitForElementVisible(noButton, LONG_TIMEOUT)
WebUI.click(noButton)
KeywordUtil.logInfo('STEP 20: Declined further assistance')

// STEP 21: Skip feedback survey
KeywordUtil.logInfo('STEP 21: Verifying Feedback screen is displayed and skipping it')
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h2_Share Your Feedback'), shareFeedbackTitleText, MEDIUM_TIMEOUT)
WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_Skip'))
KeywordUtil.logInfo('STEP 21: Feedback survey skipped')

// STEP 22: Verify chat session ended and can be restarted
KeywordUtil.logInfo('STEP 22: Verifying "Restart Chat" option is available at end of flow')
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/button_Restart Chat'), restartChatText, MEDIUM_TIMEOUT)
KeywordUtil.logInfo('STEP 22: Test flow completed successfully - "Restart Chat" is available')

//Delete Booked Appointment from MaximEyes

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


// STEP 3: Navigate to the Schedule module
KeywordUtil.logInfo('Step 4: Navigating to Schedule module')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))
WebUI.waitForElementNotVisible(findTestObject('Maximeye.com/Busy Indicator'), 30)


//=========================
// Open Appointment Actions Dropdown
//=========================
KeywordUtil.logInfo("Step 1: Clicking Appointment Actions dropdown.")

def appointmentDropdown = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-dropdown fg-skyblue')

WebUI.waitForElementClickable(appointmentDropdown, 10)
WebUI.click(appointmentDropdown)

KeywordUtil.logInfo("Appointment Actions dropdown opened successfully.")

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
