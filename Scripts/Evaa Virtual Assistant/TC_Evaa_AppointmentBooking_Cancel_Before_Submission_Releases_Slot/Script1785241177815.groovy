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
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import org.openqa.selenium.WebElement
import appointment.AppointmentKeywords

// ============================================================================
// TEST DATA
// ============================================================================

// --- Timeouts ---
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int LONG_TIMEOUT = 30
int PAGE_TIMEOUT   = 15
// --- Patient details ---
String firstName   = 'QA'
String lastName    = 'Katalon'
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
WebUI.waitForElementVisible(pushToTalk, LONG_TIMEOUT)
WebUI.click(pushToTalk)
KeywordUtil.logInfo('STEP 2: Chat bot launched successfully')

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('STEP 3: Selecting "Book Appointment"')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(bookAppt, LONG_TIMEOUT)
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
WebUI.waitForElementVisible(otpFirstDigit, PAGE_TIMEOUT)

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
KeywordUtil.logInfo("STEP 7: Selected Location='${location}', Provider='${provider}', Reason='${reason}'")

// ---------------------------------------------------------------------------
// STEP 8: Select appointment date (Today + 1), handling month rollover
// ---------------------------------------------------------------------------
KeywordUtil.logInfo("STEP 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")

TestObject calendarIframe = findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId')
// Wait for the calendar iframe itself instead of a blind sleep - faster & more reliable
//WebUI.waitForElementVisible(calendarIframe, PAGE_TIMEOUT)
WebUI.delay(10)

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
	WebUI.waitForElementClickable(deleteInsuranceBtn, 5)
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
WebUI.waitForElementVisible(continueBtn, PAGE_TIMEOUT)

WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), cardScannedTitleText, MEDIUM_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_We filled in your insurance details from the c'), cardScannedBodyText, 0)
KeywordUtil.logInfo('STEP 14: Insurance card scan confirmation verified')

WebUI.click(continueBtn)
KeywordUtil.logInfo('STEP 14: Continued to review insurance details')

// STEP 15: Verify OCR-filled insurance details, then select Gender
KeywordUtil.logInfo('STEP 15: Verifying insurance details auto-filled from the scanned card')

WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insurance Name'), 'defaultValue', insuranceName, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insured ID'), 'defaultValue', insuredId, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_First Name'), 'defaultValue', firstName, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Last Name'), 'defaultValue', lastName, MEDIUM_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_mm_dd_yyyy'), 'defaultValue', dob, MEDIUM_TIMEOUT)
KeywordUtil.logInfo('STEP 15: Insurance details verified against expected patient data')

KeywordUtil.logInfo("STEP 15a: Selecting Gender - '${gender}'")
WebUI.selectOptionByValue(findTestObject('Book Appt With Ins/EVAA.AI React/select_Gender'), gender, false)
WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/select_Gender'), 'value', gender, MEDIUM_TIMEOUT)
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
WebUI.click(coverageStartDate)
WebUI.sendKeys(coverageStartDate, coverageStartDay)
WebUI.sendKeys(coverageStartDate, coverageStartMonth)
WebUI.sendKeys(coverageStartDate, coverageStartYear)
KeywordUtil.logInfo('STEP 17: Coverage Start Date entered successfully')

KeywordUtil.logInfo("STEP 17a: Entering Coverage End Date (${coverageEndMonth}/${coverageEndDay}/${coverageEndYear})")
TestObject coverageEndDate = findTestObject('Book Appt With Ins/EVAA.AI React/input_Coverage End Date')
WebUI.click(coverageEndDate)
WebUI.sendKeys(coverageEndDate, coverageEndDay)
WebUI.sendKeys(coverageEndDate, coverageEndMonth)
WebUI.sendKeys(coverageEndDate, coverageEndYear)
KeywordUtil.logInfo('STEP 17a: Coverage End Date entered successfully')

WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
KeywordUtil.logInfo('STEP 17: Insurance coverage details submitted')

// STEP 18: Verify insurance details saved successfully
KeywordUtil.logInfo('STEP 18: Verifying insurance details were saved successfully')
WebUI.waitForElementVisible(continueBtn, PAGE_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), insuranceSavedTitleText, MEDIUM_TIMEOUT)
WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/button_View _ Add Insurances'), viewAddInsurancesText, MEDIUM_TIMEOUT)
KeywordUtil.logInfo('STEP 18: Insurance details save confirmed')



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


// STEP 3: Verify appointment not displayed
KeywordUtil.logInfo("Step 3: Verifying appointment is NOT present in MaximEyes")

WebUI.verifyElementNotPresent(
	findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason'),
	SHORT_TIMEOUT
)



// STEP 4: Navigate to the Schedule module
KeywordUtil.logInfo('Step 4: Navigating to Schedule module')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))
WebUI.waitForElementNotVisible(findTestObject('Maximeye.com/Busy Indicator'), 30)

// ---------------------------------------------------------------------------
// Verify appointment not displayed
// ---------------------------------------------------------------------------

KeywordUtil.logInfo("Step 3: Verifying appointment is NOT present in MaximEyes Schedule Page")

WebUI.comment('STEP 28: Verify the appointment is NOT present')

WebUI.verifyElementNotPresent(
    findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
    SHORT_TIMEOUT
)

WebUI.comment('STEP 30: Verify the patient/location is NOT present')

WebUI.verifyElementNotPresent(
    findTestObject('MaximeyesAppt/Page_MaximEyes/span_Katalon, Katalon Location'),
    SHORT_TIMEOUT
)


//==========

// STEP 1: Navigate to the application
KeywordUtil.logInfo('STEP 1: Navigating to application URL')
WebUI.navigateToUrl('https://qa5.eyeclinic.ai/')
KeywordUtil.logInfo('STEP 1: Application URL loaded successfully')

// STEP 2: Launch chat bot via "Push to talk" icon
KeywordUtil.logInfo('STEP 2: Launching chat bot via "Push to talk" icon')
WebUI.waitForElementVisible(pushToTalk, LONG_TIMEOUT)
WebUI.click(pushToTalk)
KeywordUtil.logInfo('STEP 2: Chat bot launched successfully')

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('STEP 3: Selecting "Book Appointment"')
WebUI.waitForElementVisible(bookAppt, LONG_TIMEOUT)
WebUI.click(bookAppt)
KeywordUtil.logInfo('STEP 3: "Book Appointment" option selected')


// STEP 4: Confirm booking intent
KeywordUtil.logInfo('STEP 4: Confirming booking intent by clicking "Yes"')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))
KeywordUtil.logInfo('STEP 4: Booking intent confirmed')

// STEP 5: Enter patient personal details
KeywordUtil.logInfo('STEP 5: Entering patient personal details')
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
WebUI.waitForElementVisible(otpFirstDigit, PAGE_TIMEOUT)

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
KeywordUtil.logInfo("STEP 7: Selected Location='${location}', Provider='${provider}', Reason='${reason}'")
//
// ---------------------------------------------------------------------------
// STEP 8: Select appointment date (Today + 1), handling month rollover
// ---------------------------------------------------------------------------
KeywordUtil.logInfo("STEP 8: Selecting appointment date - Day '${tomorrowDay}' (${tomorrowFullDate})")
WebUI.delay(10)


if (Integer.parseInt(tomorrowDay) < todayDay) {
	KeywordUtil.logInfo('STEP 8c: Month rollover detected - navigating to next month')
	WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
	KeywordUtil.logInfo('STEP 8c: Navigated to next month')
}

WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22', ['day': tomorrowDay]))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 8: Date '${tomorrowFullDate}' submitted")

//Verify cancelled slot is available for appt booking
WebUI.waitForElementVisible(
    findTestObject('Appointment Booking/Chat Bot Appt Book/Appointment Time Slots'),
    20
)

WebUI.delay(2)

TestObject timeSlots = findTestObject('Appointment Booking/Chat Bot Appt Book/Appointment Time Slots')

// Manually switch into the iframe, since WebUiCommonHelper doesn't respect "Parent iframe"
WebUI.switchToFrame(findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'), 20)

List<WebElement> slotElements = WebUiCommonHelper.findWebElements(timeSlots, 20)

println("No. of Slots = ${slotElements.size()}")

List<String> availableSlots = slotElements.collect {
    it.getText().replaceAll("\\s+", " ").trim()
}

println("Expected Time : ${apptTime}")
println("Available Slots: ${availableSlots}")

boolean isAvailable = availableSlots.contains(apptTime)

//switch back before continuing with anything outside this iframe
WebUI.switchToDefaultContent()

assert isAvailable : "Time slot '${apptTime}' is unavailable."



