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

// ============================================================================
// TEST DATA
// ============================================================================

// --- Timeouts ---
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int LONG_TIMEOUT   = 15
int PAGE_TIMEOUT   = 30

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
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(pushToTalk)
KeywordUtil.logInfo('STEP 2: Chat bot launched successfully')

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('STEP 3: Selecting "Book Appointment"')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(bookAppt, PAGE_TIMEOUT)
WebUI.click(bookAppt)
KeywordUtil.logInfo('STEP 3: "Book Appointment" option selected')

// STEP 3a: Verify Medical Disclaimer is displayed
KeywordUtil.logInfo('STEP 3a: Verifying Medical Disclaimer is displayed')
String actualDisclaimer = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/Medical Disclaimer')
).replaceAll("\\s+", " ").trim()
WebUI.verifyMatch(actualDisclaimer, medicalDisclaimerText, false)
KeywordUtil.logInfo('STEP 3a: Medical Disclaimer verified successfully')

// STEP 3b: Verify confirming message is displayed with Yes and No options
KeywordUtil.logInfo('STEP 3b: Verifying booking confirmation prompt is displayed with Yes/No options')
String actualConfirming = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/div_Do you want to proceed with booking an appoi')
).replaceAll("\\s+", " ").trim()
WebUI.verifyMatch(actualConfirming, confirmBookingPromptText, false)
KeywordUtil.logInfo('STEP 3b: Booking confirmation prompt verified successfully')

// STEP 4: Confirm booking intent
KeywordUtil.logInfo('STEP 4: Confirming booking intent by clicking "Yes"')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))
KeywordUtil.logInfo('STEP 4: Booking intent confirmed')

// STEP 5: Enter patient personal details
KeywordUtil.logInfo('STEP 5: Entering patient personal details')
TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
WebUI.waitForElementVisible(firstNameField, PAGE_TIMEOUT)

WebUI.setText(firstNameField, firstName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name'), lastName)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), dob)
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), Keys.chord(Keys.ESCAPE))
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX'), phoneNumber)
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Email_Id'), GlobalVariable.MyEmail_Id)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo("STEP 5: Submitted - ${firstName} ${lastName}, DOB ${dob}, Phone ${phoneNumber}")

//Get Otp
String otp = CustomKeywords.'utils.GmailOTPReaderChatBot.getOTP'(
	GlobalVariable.MyEmail_Id,
	GlobalVariable.Email_Key
)

println("OTP = " + otp)

// ---------------------------------------------------------------
// Step 4: Wait until the OTP expires (3 minutes = 180 seconds)
// ---------------------------------------------------------------
int otpExpirySeconds = 180   // 3 minutes, per configured expiration time
int bufferSeconds = 5        // small buffer to ensure OTP has definitely expired


WebUI.delay(otpExpirySeconds + bufferSeconds)
WebUI.comment("Wait complete. OTP should now be expired.")

//KeywordUtil.logInfo('STEP 5: Entering OTP for verification')
//TestObject otpFirstDigit = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')
//WebUI.waitForElementVisible(otpFirstDigit, PAGE_TIMEOUT)
//
//otp.eachWithIndex { String digit, int i ->
//	WebUI.sendKeys(findTestObject("Appointment Booking/Chat Bot Appt Book/input_otp-${i}"), digit)
//}
//
//// >>>>>>>>>>>>>Next Button is disabled after 3 min>>>>>>> Issue
//WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
//KeywordUtil.logInfo("STEP 6: OTP '${otp}' entered and submitted")
//
////Verify it is expired
//WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Invalid One-Time Password (OTP). Please check'),
//	'Invalid One-Time Password (OTP). Please check and try again or request a new one.', 5)
