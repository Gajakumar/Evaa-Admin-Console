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
import appointment.AppointmentKeywords

// ------------
// TEST DATA 
// ------------

// Timeouts
int SHORT_TIMEOUT  = 3
int MEDIUM_TIMEOUT = 5
int PAGE_TIMEOUT   = 30


String expectedAssitMsg = "Is there anything else I can assist you with today?"

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

// STEP 4: Decline booking intent
KeywordUtil.logInfo('Step 4: Decline booking intent')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_No'))

//Step 5 : Verify Is there anything else I can assist you with today message id displayed
WebUI.delay(5)
String actualAssitMsg = WebUI.getText(
	findTestObject('Appointment Booking/Chat Bot Appt Book/p_Is there anything else I can assist you with today')
).replaceAll("\\s+", " ").trim()

WebUI.verifyMatch(actualAssitMsg, expectedAssitMsg, false)


