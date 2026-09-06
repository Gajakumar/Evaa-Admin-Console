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

// ------------
// TEST DATA
// ------------

int SHORT_TIMEOUT = 3
int PAGE_TIMEOUT  = 30


// Patient details
String firstName   = 'QA'
String lastName    = 'Katalon'
String dob         = '01/04/1995'
String phoneNumber = '111-111-1111'
String otpCode     = '9753'


// ============================================================================
// PART 1: Chat Bot Appointment Booking
// ============================================================================

// Launch application and start booking flow
CustomKeywords.'common.ChatBotBookingFlow.navigateToApp'()

CustomKeywords.'common.ChatBotBookingFlow.launchChatBot'(PAGE_TIMEOUT)

CustomKeywords.'common.ChatBotBookingFlow.selectBookAppointment'(PAGE_TIMEOUT)

CustomKeywords.'common.ChatBotBookingFlow.confirmBookingIntent'(true)

// Enter existing patient details
CustomKeywords.'common.ChatBotBookingFlow.enterPatientDetails'(
    firstName,
    lastName,
    dob,
    phoneNumber,
    PAGE_TIMEOUT
)

// ============================================================================
// PART 2: Verify OTP Session Timeout
// ============================================================================

// Wait for OTP to expire (3 minutes + 5 second buffer)
int otpExpirySeconds = 180
int bufferSeconds = 5

WebUI.delay(otpExpirySeconds + bufferSeconds)
WebUI.comment('Wait complete. OTP should now be expired.')

//WebUI.switchToDefaultContent()

// Verify timeout popup
TestObject sessionTimeout = findTestObject('Appointment Booking/OTP/h3_Your session has timed out')

//WebUI.waitForElementVisible(sessionTimeout, 30)

WebUI.verifyElementText(
	sessionTimeout,
	'Your session has timed out.'
)

WebUI.verifyElementText(
    findTestObject('Appointment Booking/OTP/p_Please generate a new One-Time Password (OTP)'),
    'Please generate a new One-Time Password (OTP).'
)

// Click OK on timeout dialog
WebUI.verifyElementPresent(
    findTestObject('Appointment Booking/OTP/button_OK'),
    SHORT_TIMEOUT
)

WebUI.click(findTestObject('Appointment Booking/OTP/button_OK'))

// Verify NEXT button is disabled
WebUI.verifyElementHasAttribute(
    findTestObject('Appointment Booking/OTP/button_NEXT'),
    'disabled',
    SHORT_TIMEOUT
)

