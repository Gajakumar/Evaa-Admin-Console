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
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.testobject.TestObject as findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import com.kms.katalon.core.testobject.TestObject as findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil

// ---------------------------------------------------------------------------
// TEST DATA (centralized for easy maintenance)
// ---------------------------------------------------------------------------
int DEFAULT_TIMEOUT   = 5
String firstName      = 'QA'
String lastName       = 'Katalon'
String dob             = '01/04/1995'
String phoneNumber     = '111-111-1111'
String otpDigit1       = '9'
String otpDigit2       = '7'
String otpDigit3       = '5'
String otpDigit4       = '3'
String reasonText      = 'Katalon Appointment'
String expectedName     = 'Name: QA Katalon'
String expectedLocation = 'Location: Katalon Location'
String expectedProvider = 'Provider: Katalon Provider (OD)'
String expectedReason   = 'Reason: Katalon Reason'
String expectedDate     = 'Date: 07/22/2026'
String expectedTime     = 'Time: 11:30 AM'
String expectedConfirmationMsg = 'Your appointment has been booked'
String apptTime = '11:30 AM'
String location = 'Katalon Location'
String provider = 'Katalon Provider'
String reason   = 'Katalon Reason'

// ---------------------------------------------------------------------------
// STEP 1: Navigate to the application URL
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 1: Navigating to https://qa5.eyeclinic.ai/')
WebUI.navigateToUrl('https://qa5.eyeclinic.ai/')
KeywordUtil.logInfo('Step 1: Navigation successful')

// ---------------------------------------------------------------------------
// STEP 2: Launch chat bot via "Push to talk" icon
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 2: Clicking "Push to talk" icon to launch chat bot')
TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')

WebUI.waitForElementVisible(pushToTalk, 30)
WebUI.click(pushToTalk)
KeywordUtil.logInfo('Step 2: Chat bot launched successfully')

// ---------------------------------------------------------------------------
// STEP 3: Select "Book Appointment" option from chat bot
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 3: Clicking "Book Appointment" button')
WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment'))
KeywordUtil.logInfo('Step 3: "Book Appointment" option selected successfully')

// ---------------------------------------------------------------------------
// STEP 4: Confirm booking intent by clicking "Yes"
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 4: Clicking "Yes" to confirm booking intent')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))
KeywordUtil.logInfo('Step 4: Booking intent confirmed')

// ---------------------------------------------------------------------------
// STEP 5: Enter patient personal details (First Name, Last Name, DOB, Phone)
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 5: Entering patient personal details')

TestObject firstname = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')

WebUI.waitForElementVisible(firstname, 30)

KeywordUtil.logInfo("Step 5a: Entering First Name - '${firstName}'")
WebUI.setText(firstname, firstName)
KeywordUtil.logInfo('Step 5a: First Name entered successfully')

KeywordUtil.logInfo("Step 5b: Entering Last Name - '${lastName}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name'), lastName)
KeywordUtil.logInfo('Step 5b: Last Name entered successfully')

KeywordUtil.logInfo("Step 5c: Entering Date of Birth - '${dob}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy'), dob)
KeywordUtil.logInfo('Step 5c: Date of Birth entered successfully')

KeywordUtil.logInfo("Step 5d: Entering Phone Number - '${phoneNumber}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX'), phoneNumber)
KeywordUtil.logInfo('Step 5d: Phone Number entered successfully')

KeywordUtil.logInfo('Step 5e: Clicking "NEXT" to proceed to OTP verification')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('Step 5: Patient personal details submitted successfully')

// ---------------------------------------------------------------------------
// STEP 6: Enter OTP for verification
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 6: Entering OTP digits for verification')

TestObject otpScreen = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')

WebUI.waitForElementVisible(otpScreen, 30)
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0'), otpDigit1)
KeywordUtil.logInfo("Step 6a: OTP digit 1 entered - '${otpDigit1}'")

WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-1'), otpDigit2)
KeywordUtil.logInfo("Step 6b: OTP digit 2 entered - '${otpDigit2}'")

WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-2'), otpDigit3)
KeywordUtil.logInfo("Step 6c: OTP digit 3 entered - '${otpDigit3}'")

WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-3'), otpDigit4)
KeywordUtil.logInfo("Step 6d: OTP digit 4 entered - '${otpDigit4}'")

KeywordUtil.logInfo('Step 6e: Clicking "NEXT" to verify OTP')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('Step 6: OTP verified successfully')

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
KeywordUtil.logInfo('Step 7: Location, Provider, and Reason submitted successfully')

// ---------------------------------------------------------------------------
// STEP 8: Select Appointment Date
// ---------------------------------------------------------------------------
//KeywordUtil.logInfo('Step 8: Selecting appointment date (22nd)')
//WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22'))
//KeywordUtil.logInfo('Step 8: Date selected successfully')

// ---------------------------------------------------------------------------
// STEP 8: Select Appointment Date (dynamically = Today + 1 day)
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 8: Calculating appointment date as Today + 1 day')

// Calculate tomorrow's date
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.DATE, 1)
Date tomorrowDate = calendar.getTime()

// Day number to match against calendar cell text (e.g., "22")
SimpleDateFormat dayFormat = new SimpleDateFormat('d')
String tomorrowDay = dayFormat.format(tomorrowDate)

// Full date for logging/assertions later (e.g., "07/22/2026")
SimpleDateFormat fullDateFormat = new SimpleDateFormat('MM/dd/yyyy')
String tomorrowFullDate = fullDateFormat.format(tomorrowDate)

KeywordUtil.logInfo("Step 8a: Target day calculated - Day: '${tomorrowDay}', Full Date: '${tomorrowFullDate}'")

// ---------------------------------------------------------------------------
// STEP 8b: Handle month rollover BEFORE clicking, if tomorrow's day number
//          is smaller than today's (e.g., today is 31st, tomorrow is 1st)
// ---------------------------------------------------------------------------
Calendar todayCalendar = Calendar.getInstance()
int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)

if (Integer.parseInt(tomorrowDay) < todayDay) {
	KeywordUtil.logInfo('Step 8b: Month rollover detected - clicking "Next Month" navigation')
	WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
	KeywordUtil.logInfo('Step 8b: Navigated to next month successfully')
}

// ---------------------------------------------------------------------------
// STEP 8c: Click the calendar day using the dynamic Test Object + variation
// ---------------------------------------------------------------------------
KeywordUtil.logInfo("Step 8c: Clicking calendar date for day '${tomorrowDay}'")
WebUI.click(findTestObject(
	'Appointment Booking/Chat Bot Appt Book/button_22',
	['day': tomorrowDay]
))
KeywordUtil.logInfo("Step 8: Appointment date selected successfully - '${tomorrowFullDate}'")

KeywordUtil.logInfo('Step 8a: Clicking "NEXT" to proceed to time selection')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('Step 8: Date submitted successfully')

// ---------------------------------------------------------------------------
// STEP 9: Select Appointment Time (11:30 AM)
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 9: Selecting appointment time - 11:30 AM')
WebUI.click(findTestObject(
	'Appointment Booking/Chat Bot Appt Book/button_10_30 AM',
	['time': apptTime]
))
KeywordUtil.logInfo('Step 9: Time slot selected successfully')

KeywordUtil.logInfo('Step 9a: Clicking "NEXT" to proceed to symptoms/reason entry')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('Step 9: Time submitted successfully')

// ---------------------------------------------------------------------------
// STEP 10: Enter symptoms/reason for visit in free-text field
// ---------------------------------------------------------------------------
KeywordUtil.logInfo("Step 10: Entering symptoms/reason for visit - '${reasonText}'")
WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/textarea_Describe your symptoms or reason for th'), reasonText)
KeywordUtil.logInfo('Step 10: Symptoms/reason entered successfully')

KeywordUtil.logInfo('Step 10a: Clicking "NEXT" to proceed to payment selection')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
KeywordUtil.logInfo('Step 10: Symptoms/reason submitted successfully')

// ---------------------------------------------------------------------------
// STEP 11: Select payment option - Self Pay / No Insurance
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 11: Selecting "Self Pay / No Insurance Available" option')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Self Pay_No Insurance Available'))
KeywordUtil.logInfo('Step 11: Payment option selected successfully')

// ---------------------------------------------------------------------------
// STEP 12: Finish booking
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 12: Clicking "FINISH BOOKING" to complete the appointment')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_FINISH BOOKING'))
KeywordUtil.logInfo('Step 12: Booking finished successfully')

// ---------------------------------------------------------------------------
// STEP 13: Verify confirmation screen - Patient Name
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 13: Verifying Patient Name on confirmation screen')
TestObject PtName = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')

WebUI.waitForElementVisible(PtName, 30)
WebUI.assertElementText(PtName, expectedName, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 13: Patient Name verified successfully - '${expectedName}'")

// ---------------------------------------------------------------------------
// STEP 14: Verify confirmation screen - Location
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 14: Verifying Location on confirmation screen')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West'), expectedLocation, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 14: Location verified successfully - '${expectedLocation}'")

// ---------------------------------------------------------------------------
// STEP 15: Verify confirmation screen - Provider
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 15: Verifying Provider on confirmation screen')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)'), expectedProvider, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 15: Provider verified successfully - '${expectedProvider}'")

// ---------------------------------------------------------------------------
// STEP 16: Verify confirmation screen - Reason
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 16: Verifying Reason on confirmation screen')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason'), expectedReason, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 16: Reason verified successfully - '${expectedReason}'")

// ---------------------------------------------------------------------------
// STEP 17: Verify confirmation screen - Date
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 17: Verifying Date on confirmation screen')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026'), "Date: "+tomorrowFullDate, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 17: Date verified successfully - 'Date: ${tomorrowFullDate}'")

// ---------------------------------------------------------------------------
// STEP 18: Verify confirmation screen - Time
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 18: Verifying Time on confirmation screen')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM'), expectedTime, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 18: Time verified successfully - '${expectedTime}'")

// ---------------------------------------------------------------------------
// STEP 19: Verify final booking confirmation message
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Step 19: Verifying final booking confirmation message')
WebUI.assertElementText(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked'), expectedConfirmationMsg, DEFAULT_TIMEOUT)
KeywordUtil.logInfo("Step 19: Booking confirmation message verified successfully - '${expectedConfirmationMsg}'")

// ---------------------------------------------------------------------------
// TEST COMPLETE
// ---------------------------------------------------------------------------
KeywordUtil.logInfo('Test Case Completed: Chat Bot Appointment Booking - all steps executed and confirmation details verified successfully')
KeywordUtil.markPassed('Chat Bot Appointment Booking test case passed successfully')


/*
 * Test Case: MaximEyes - Search Patient, Verify & Update Appointment Details
 * Description:
 *   1. Logs into MaximEyes application
 *   2. Searches for patient "QA Katalon"
 *   3. Verifies appointment slot details (date, time, reason, location) on the scheduler
 *   4. Opens the appointment popup and verifies dropdown/field values
 *   5. Verifies appointment details after closing the popup (calendar/day view)
 */

//Max expected
String expectedDateTimeReason = "${tomorrowFullDate} | ${apptTime} | ${reason}"

                                     
             
// ---------------------------------------------------------------------------
// STEP 1: Login to MaximEyes application
// ---------------------------------------------------------------------------
WebUI.comment('STEP 1: Navigate to MaximEyes QA5 URL')
WebUI.navigateToUrl(GlobalVariable.MaxUrlQA5)

WebUI.comment('STEP 2: Enter QA5 username')
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)

WebUI.comment('STEP 3: Enter QA5 password')
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)

WebUI.comment('STEP 4: Click Login button')
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

// ---------------------------------------------------------------------------
// STEP 2: Search for the patient "QA Katalon"
// ---------------------------------------------------------------------------
WebUI.comment('STEP 5: Click "Find Patient" icon')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_imgFindPatient'))

WebUI.comment('STEP 6: Enter patient First Name - "QA"')
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_First Name_Preferred'), 'QA')

WebUI.comment('STEP 7: Enter patient Last Name - "Katalon"')
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_Last Name'), 'Katalon')

WebUI.comment('STEP 8: Click "Search Patient" button')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/input_btnSearchPatient'))

// ---------------------------------------------------------------------------
// STEP 3: Verify appointment slot details on the search/results view
// ---------------------------------------------------------------------------
WebUI.comment("STEP 10: Verify appointment slot text matches '${expectedDateTimeReason}'")
WebUI.assertElementText(
	findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason'),
	expectedDateTimeReason,
	5)

WebUI.comment('STEP 12: Verify patient/location text matches "Katalon, Katalon Location"')
String actualLocationOV = WebUI.getText(
	findTestObject('MaximeyesAppt/Page_MaximEyes/div_Katalon, Katalon Location')
).trim()

WebUI.verifyEqual(actualLocationOV, 'Katalon, Katalon Location')

// ---------------------------------------------------------------------------
// STEP 4: Navigate to the Schedule module
// ---------------------------------------------------------------------------
WebUI.comment('STEP 13: Open recent modules dropdown menu')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))

WebUI.comment('STEP 14: Click "Schedule" menu item')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))

WebUI.comment('STEP 15: Click scheduler date navigator icon')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/img_scheduler_viewNavigatorBlock_ctl00_BG_GTDBII'))

WebUI.comment('STEP 16: Select date "23" on the scheduler calendar')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/td_23'))

// ---------------------------------------------------------------------------
// STEP 5: Open the appointment popup for "QA Katalon"
// ---------------------------------------------------------------------------
WebUI.comment('STEP 17: Click appointment block "QA Katalon (31yo) - Katalon Reason - Confirm"')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/div_QA Katalon (31yo) _ Katalon Reason _ Confirm'))

WebUI.comment("STEP 18: Double-click '${reason}' appointment block to open appointment details popup")
//WebUI.doubleClick(findTestObject('MaximeyesAppt/Page_MaximEyes/div_QA Katalon (31yo) _ Katalon Reason _ Confirm'))
CustomKeywords.'SchedulerKeywords.doubleClickTimeSlotFromString'("${apptTime}")

// ---------------------------------------------------------------------------
// STEP 6: Verify appointment popup field/dropdown values
// ---------------------------------------------------------------------------
WebUI.comment("STEP 19: Verify Reason dropdown contains expected options' ${reason}")
WebUI.verifyOptionSelectedByLabel(
	findTestObject('MaximeyesAppt/Page_MaximEyes/select_ReasonId'),
	"${reason} : ${reason}",
	false,
	10
)


WebUI.comment('STEP 21: Verify "Chief Complaint" textarea contains "Katalon Appointment"')
WebUI.assertElementText(findTestObject('MaximeyesAppt/Page_MaximEyes/textarea_ChiefComplaint'), "${reasonText}", 0)

WebUI.comment('STEP 22: Verify "Appointment Type" dropdown contains expected options')
WebUI.verifyOptionSelectedByLabel(
	findTestObject('MaximeyesAppt/Page_MaximEyes/select_TypeID'),
	'15 Min',
	false,
	10
)

WebUI.comment('STEP 23: Verify "Appointment Status" dropdown contains expected options')
WebUI.verifyOptionSelectedByLabel(
	findTestObject('MaximeyesAppt/Page_MaximEyes/select_drdnAppointmentStatus'),
	'Confirmed',
	false,
	10
)


WebUI.comment('STEP 25: Verify "9:30 AM" time label text')
WebUI.assertElementText(findTestObject('MaximeyesAppt/Page_MaximEyes/label_9_30 AM'), "${apptTime}", 5)

// ---------------------------------------------------------------------------
// STEP 7: Close the appointment popup
// ---------------------------------------------------------------------------
WebUI.comment('STEP 26: Click "Close" button to close appointment popup')
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/button_Close'))

// ---------------------------------------------------------------------------
// STEP 8: Verify appointment details on the scheduler day/calendar view
// ---------------------------------------------------------------------------
WebUI.comment('STEP 28: Verify appointment span text matches "07/23/2026 | Katalon Reason"')
WebUI.assertElementText(
	findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
	"${tomorrowFullDate} | ${reason}",
	5)

WebUI.comment('STEP 30: Verify patient/location span text matches "Katalon, Katalon Location"')
String actualLocation = WebUI.getText(
	findTestObject('MaximeyesAppt/Page_MaximEyes/span_Katalon, Katalon Location')
).trim()

WebUI.verifyEqual(actualLocation, 'Katalon, Katalon Location')

WebUI.comment('TEST COMPLETE: All appointment detail verifications for patient "QA Katalon" passed')
