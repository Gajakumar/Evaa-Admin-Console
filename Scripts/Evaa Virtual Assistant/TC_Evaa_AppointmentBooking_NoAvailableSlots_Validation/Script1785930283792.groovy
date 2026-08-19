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

// ------------
// TEST DATA
// ------------

// Timeouts
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int PAGE_TIMEOUT   = 15

// Patient details
String firstName   = 'QA'
String lastName    = 'Katalon'
String patientAge  = '31yo'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '9753'

// Appointment details
String location         = 'Katalon Location'
String provider         = 'Katalon Provider'
String reason           = 'Katalon No Slots'

String expectedNoSlotsText = "We're sorry, but no appointment slots are currently available. Please use the back button to modify your search, or call us at +1-232-333-3333 so we can assist you further."

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

KeywordUtil.logInfo('Step 7d: Clicking "NEXT" to proceed to date selection')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()
KeywordUtil.logInfo("Step 7: Selected Location='${location}', Provider='${provider}', Reason='${reason}'")

// ==========================================================
// Verify "No Appointment Slots Available" message
// ==========================================================

WebUI.comment("Verifying the 'No Appointment Slots Available' message...")

String actualNoSlotsText = WebUI.getText(
	findTestObject('Appointment Booking/No Slots/p_Were sorry, but no appointment slots are curr')
).replaceAll("\\s+", " ").trim()


WebUI.verifyEqual(actualNoSlotsText, expectedNoSlotsText)

WebUI.comment("Verified: 'No Appointment Slots Available' message is displayed correctly.")


// ==========================================================
// Click Back and verify Appointment Details page
// ==========================================================

WebUI.comment("Clicking on BACK button...")

WebUI.click(findTestObject('Appointment Booking/No Slots/button_BACK'))

WebUI.comment("Verifying 'Appointment Details' screen is displayed...")

WebUI.verifyElementText(
	findTestObject('Appointment Booking/No Slots/h2_Appointment Details'),
	'Appointment Details'
)

WebUI.comment("Verified: Appointment Details page is displayed.")


// ==========================================================
// Click Next and verify No Slots message is displayed again
// ==========================================================

WebUI.comment("Clicking on NEXT button...")

WebUI.click(findTestObject('Appointment Booking/No Slots/button_NEXT'))
CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'()

WebUI.comment("Verifying 'No Appointment Slots Available' message after clicking NEXT...")

String actualNoSlotsTextAfterNext = WebUI.getText(
	findTestObject('Appointment Booking/No Slots/p_Were sorry, but no appointment slots are curr')
).replaceAll("\\s+", " ").trim()

WebUI.verifyEqual(actualNoSlotsTextAfterNext, expectedNoSlotsText)

WebUI.comment("Verified: 'No Appointment Slots Available' message is displayed again.")


// ==========================================================
// Click Next again and verify chatbot continues booking flow
// ==========================================================

WebUI.comment("Clicking NEXT to continue the chatbot workflow...")

WebUI.click(findTestObject('Appointment Booking/No Slots/button_NEXT'))

WebUI.comment("Verifying chatbot displays the appointment booking message...")

WebUI.verifyElementText(
	findTestObject('Appointment Booking/No Slots/p_Sure Please bear with me for a second while I'),
	'Sure! Please bear with me for a second while I pull up the form to book an appointment.'
)

WebUI.comment("Verified: Chatbot displays the appointment booking message successfully.")




















