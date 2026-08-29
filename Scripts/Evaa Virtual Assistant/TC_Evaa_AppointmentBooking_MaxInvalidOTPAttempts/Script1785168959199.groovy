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
import org.openqa.selenium.Keys
import appointment.AppointmentKeywords

// ------------
// TEST DATA
// ------------

// Timeouts
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int PAGE_TIMEOUT   = 30

// Patient details
String firstName   = 'QA'
String lastName    = 'Katalon'
String patientAge  = '31yo'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '0000'
String otpCode1    = '1111'
String otpCode2    = '2222'


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

//Verify Disclaimer And Confirmation Prompt
AppointmentKeywords appointmentKeywords = new AppointmentKeywords()
appointmentKeywords.verifyDisclaimerAndConfirmationPrompt()

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


def enterOTP(String otp) {
    otp.eachWithIndex { String digit, int i ->

        TestObject otpField = findTestObject(
            "Appointment Booking/Chat Bot Appt Book/input_otp-${i}"
        )

//        WebUI.click(otpField)

        // Clear existing value
        WebUI.sendKeys(otpField, Keys.chord(Keys.CONTROL, "a"))
        WebUI.sendKeys(otpField, Keys.chord(Keys.DELETE))

        // Enter digit
        WebUI.setText(otpField, digit)
    }
}


enterOTP(otpCode)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Invalid One-Time Password (OTP). Please check'),
	'Invalid One-Time Password (OTP). Please check and try again or request a new one.', 5)

enterOTP(otpCode1)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Invalid One-Time Password (OTP). Please check'),
	'Invalid One-Time Password (OTP). Please check and try again or request a new one.', 5)

enterOTP(otpCode2)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Invalid One-Time Password (OTP). Please check'),
	'Too many incorrect attempts. Please try again later.', 5)


// Verify OTP field is disabled
WebUI.verifyElementHasAttribute(
	findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0'),
	'disabled',
	5
)
KeywordUtil.logInfo('OTP Field is Disabled')

// Verify Resend OTP button is disabled
WebUI.verifyElementHasAttribute(
	findTestObject('Appointment Booking/Chat Bot Appt Book/button_Resend OTP'),
	'disabled',
	5
)

KeywordUtil.logInfo('Resend button is Disabled')

// Verify NEXT button is disabled
WebUI.verifyElementHasAttribute(
	findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT (1)'),
	'disabled',
	5
)

KeywordUtil.logInfo('Next button is Disabled')
