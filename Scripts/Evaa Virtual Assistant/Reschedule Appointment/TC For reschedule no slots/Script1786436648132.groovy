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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import org.openqa.selenium.Keys
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import groovy.transform.Field

/* =============================================================================
 * TEST DATA
 * ========================================================================== */



@Field final int SHORT_TIMEOUT = 3
@Field final int MEDIUM_TIMEOUT = 5
@Field final int PAGE_TIMEOUT = 30

// ---- Application URLs ----
@Field String appUrl = 'https://qa5.eyeclinic.ai/'

// ---- Patient details ----
@Field String firstName   = 'Harry'
@Field String lastName    = 'Denver'
@Field String patientAge  = '50yo'
@Field String dob         = '04/24/1982'
@Field String phoneNumber = '111-111-1111'
@Field String otpCode     = '9753'

// ---- Appointment details ----
@Field String location          = 'Katalon Location'
@Field String provider          = 'Katalon Provider'
@Field String providerFirstName = 'Katalon'
@Field String reason             = 'Katalon No Slots'
@Field String reasonText         = 'Katalon Appointment'
@Field String apptTime            = '11:30 AM'

// ---- Reschedule details ----
@Field String rescheduleReasonInput = 'Reschedule Appointment'
@Field String rescheduleApptTime    = '08:30 AM'

// ---- Static / expected copy text ----
@Field String medicalDisclaimerText = 'Online appointment booking is only for routine exam and follow up appointments and ' +
        'should not be used if you have any urgent or concerning medical issues. If experiencing medical issues ' +
        'please call our office during office hours. If outside of office hours please call 911 or visit an ' +
        'urgent care or emergency room for immediate assistance.'
@Field String confirmBookingPromptText = 'Do you want to proceed with booking an appointment? Yes No'
@Field String alreadyConfirmedMessage  = 'This appointment is already confirmed.*'

// ---- Appointment date, calculated dynamically as Today + 1 day ----
Calendar calendar = Calendar.getInstance()
calendar.add(Calendar.DATE, 1)
Date tomorrowDate = calendar.getTime()

String tomorrowDay      = new SimpleDateFormat('d').format(tomorrowDate)
String tomorrowFullDate = new SimpleDateFormat('MM/dd/yyyy').format(tomorrowDate)
@Field int todayDay             = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

// ---- Expected values - all derived from the data above, never re-typed ----
@Field String expectedName            = "Name: ${firstName} ${lastName}"
@Field String expectedLocation        = "Location: ${location}"
@Field String expectedProvider        = "Provider: ${provider} (OD)"
@Field String expectedReason          = "Reason: ${reason}"
String expectedDate            = "Date: ${tomorrowFullDate}"
@Field String expectedTime            = "Time: ${apptTime}"
@Field String expectedRescheduledTime = "Time: ${rescheduleApptTime}"
@Field String expectedConfirmationMsg = 'Your appointment has been booked'
@Field String expectedRescheduleMsg   = 'Your appointment has been rescheduled'
String expectedDateTimeReason         = "${tomorrowFullDate} | ${apptTime} | ${reason}"
String expectedRescheduleDateTimeReason = "${tomorrowFullDate} | ${rescheduleApptTime} | ${reason}"
@Field String expectedPatientLocation = "${providerFirstName}, ${location}"
String expectedSpanText        = "${tomorrowFullDate} | ${reason}"

KeywordUtil.logInfo("Test data ready | Patient: ${firstName} ${lastName} | Appt: ${tomorrowFullDate} ${apptTime}")

/* =============================================================================
 * REUSABLE STEP FUNCTIONS
 * (extracted so the booking flow and the reschedule flow - which repeat the
 *  same UI steps - don't duplicate code)
 * ========================================================================== */

/**
 * Enters the OTP one digit at a time into the otp-0..otp-N inputs.
 */
def enterOtp(String code) {
    KeywordUtil.logInfo("Entering OTP: '${code}'")
    TestObject otpFirstDigit = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')
    WebUI.waitForElementVisible(otpFirstDigit, PAGE_TIMEOUT)

    code.eachWithIndex { String digit, int i ->
        WebUI.sendKeys(findTestObject("Appointment Booking/Chat Bot Appt Book/input_otp-${i}"), digit)
    }
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
    KeywordUtil.logInfo("OTP '${code}' entered and submitted")
}

/**
 * Selects Location, Provider and Reason on the chat bot appointment form
 * and advances to the next step. Used for both the initial booking and
 * the reschedule flow.
 */
def selectLocationProviderReason(String loc, String prov, String rsn) {
    KeywordUtil.logInfo("Selecting Location='${loc}', Provider='${prov}', Reason='${rsn}'")

    WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Location'), loc, false)
    KeywordUtil.logInfo('Location selected successfully')

    WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Provider'), prov, false)
    KeywordUtil.logInfo('Provider selected successfully')

    WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Reason'), rsn, false)
    KeywordUtil.logInfo('Reason selected successfully')

    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
    KeywordUtil.logInfo("Location/Provider/Reason submitted, moved to date selection")
}

/**
 * Validates the calendar widget (past dates disabled / future dates enabled),
 * handles month rollover if "tomorrow" falls in the next month, then picks
 * the target day and advances.
 */
def selectAppointmentDate(String targetDay, String targetFullDate) {
    KeywordUtil.logInfo("Selecting appointment date - Day '${targetDay}' (${targetFullDate})")

    KeywordUtil.logInfo('Verifying past dates are disabled')
    CustomKeywords.'common.CalendarHelper.verifyPastDatesDisabled'(
        findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'))

    KeywordUtil.logInfo('Verifying today and future dates are enabled')
    CustomKeywords.'common.CalendarHelper.verifyAvailableDatesEnabled'(
        findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'))

    if (Integer.parseInt(targetDay) < todayDay) {
        KeywordUtil.logInfo('Month rollover detected - navigating to next month')
        WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
    }

    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22', ['day': targetDay]))
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
    KeywordUtil.logInfo("Date '${targetFullDate}' submitted")
}

/**
 * Picks the appointment time slot and advances.
 */
def selectAppointmentTime(String time) {
    KeywordUtil.logInfo("Selecting appointment time - ${time}")
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_10_30 AM', ['time': time]))
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
}

/**
 * Enters the free-text reason/symptoms, selects Self Pay, and finishes booking.
 */
def enterReasonAndFinishBooking(String freeTextReason) {
    KeywordUtil.logInfo("Entering reason for visit - '${freeTextReason}'")
    WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/textarea_Describe your symptoms or reason for th'), freeTextReason)
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

    KeywordUtil.logInfo('Selecting "Self Pay / No Insurance Available"')
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Self Pay_No Insurance Available'))

    KeywordUtil.logInfo('Finishing booking')
    WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_FINISH BOOKING'))
}

/**
 * Waits for the confirmation screen and verifies every field on it.
 * @param waitObj the object to wait on before asserting (e.g. the name label)
 * @param assertions map of TestObject -> expected text
 */
def verifyConfirmationScreen(TestObject waitObj, LinkedHashMap<TestObject, String> assertions) {
    KeywordUtil.logInfo('Verifying confirmation screen details')
    WebUI.waitForElementVisible(waitObj, PAGE_TIMEOUT)
    assertions.each { TestObject obj, String expectedText ->
        WebUI.assertElementText(obj, expectedText, SHORT_TIMEOUT)
    }
    KeywordUtil.logInfo('Confirmation screen verified successfully')
}

/**
 * Searches for a patient in MaximEyes and verifies the appointment slot
 * text and patient/location text shown in the search results.
 */
def searchPatientAndVerifyAppointment(String fName, String lName, String expectedSlotText) {
    KeywordUtil.logInfo("Searching for patient '${fName} ${lName}'")
    WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_imgFindPatient'))
    WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_First Name_Preferred'), fName)
    WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_Last Name'), lName)
    WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/input_btnSearchPatient'))

    KeywordUtil.logInfo("Verifying appointment slot shows '${expectedSlotText}'")
    TestObject apptSlot = findTestObject('MaximeyesAppt/Page_MaximEyes/div_07_23_2026 _ 09_30 AM _ Katalon Reason')
    WebUI.waitForElementPresent(apptSlot, SHORT_TIMEOUT)
    WebUI.assertElementText(apptSlot, expectedSlotText, SHORT_TIMEOUT)

    String actualLocation = WebUI.getText(findTestObject('MaximeyesAppt/Page_MaximEyes/div_Katalon, Katalon Location')).trim()
    WebUI.verifyEqual(actualLocation, expectedPatientLocation)
    KeywordUtil.logInfo('Patient and appointment slot verified in MaximEyes')
}

/**
 * Navigates to the Schedule module and verifies the appointment span text
 * and patient/location text on the scheduler/calendar view.
 */
def verifyAppointmentOnScheduler(String expectedSpan) {
    KeywordUtil.logInfo('Navigating to Schedule module')
    WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
    WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_Schedule'))

    KeywordUtil.logInfo("Verifying appointment span text matches '${expectedSpan}'")
    WebUI.assertElementText(
        findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
        expectedSpan,
        0)

    KeywordUtil.logInfo("Verifying patient/location span text matches '${expectedPatientLocation}'")
    String actualLocation = WebUI.getText(
        findTestObject('MaximeyesAppt/Page_MaximEyes/span_Katalon, Katalon Location')
    ).trim()
    WebUI.verifyEqual(actualLocation, expectedPatientLocation)
    KeywordUtil.logInfo('Scheduler view verified successfully')
}

/* =============================================================================
 * PART 1: CHAT BOT APPOINTMENT BOOKING
 * ========================================================================== */

// STEP 1: Navigate to the application
KeywordUtil.logInfo('Step 1: Navigating to application URL')
WebUI.navigateToUrl(appUrl)

// STEP 2: Launch chat bot
KeywordUtil.logInfo('Step 2: Launching chat bot via "Push to talk" icon')
TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
WebUI.waitForElementVisible(pushToTalk, PAGE_TIMEOUT)
WebUI.click(pushToTalk)

// STEP 3: Select "Book Appointment"
KeywordUtil.logInfo('Step 3: Selecting "Book Appointment"')
TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
WebUI.waitForElementVisible(bookAppt, PAGE_TIMEOUT)
WebUI.click(bookAppt)

// Verify the medical disclaimer is displayed
KeywordUtil.logInfo('Step 3a: Verifying medical disclaimer is displayed')
String actualDisclaimer = WebUI.getText(
    findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/Medical Disclaimer')
).replaceAll('\\s+', ' ').trim()
WebUI.verifyMatch(actualDisclaimer, medicalDisclaimerText, false)
KeywordUtil.logInfo('Step 3a: Medical disclaimer verified')

// Verify the "Do you want to proceed..." confirmation prompt is displayed
KeywordUtil.logInfo('Step 3b: Verifying booking confirmation prompt is displayed with Yes/No')
String actualConfirming = WebUI.getText(
    findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/div_Do you want to proceed with booking an appoi')
).replaceAll('\\s+', ' ').trim()
WebUI.verifyMatch(actualConfirming, confirmBookingPromptText, false)
KeywordUtil.logInfo('Step 3b: Booking confirmation prompt verified')

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

// STEP 6: Enter OTP
KeywordUtil.logInfo('Step 6: Entering OTP for verification')
enterOtp(otpCode)

// STEP 7: Select Location, Provider, and Reason for visit
KeywordUtil.logInfo('Step 7: Selecting Location, Provider, and Reason')
selectLocationProviderReason(location, provider, reason)

// STEP 8: Select appointment date (Today + 1), handling month rollover
KeywordUtil.logInfo('Step 8: Selecting appointment date')
selectAppointmentDate(tomorrowDay, tomorrowFullDate)

// STEP 9: Select appointment time
KeywordUtil.logInfo('Step 9: Selecting appointment time')
selectAppointmentTime(apptTime)

// STEP 10-12: Enter reason free-text, choose Self Pay, finish booking
KeywordUtil.logInfo('Step 10-12: Entering reason, selecting payment option, and finishing booking')
enterReasonAndFinishBooking(reasonText)

// STEPS 13-19: Verify confirmation screen
KeywordUtil.logInfo('Step 13-19: Verifying booking confirmation screen')
TestObject ptNameLabel = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')
verifyConfirmationScreen(ptNameLabel, [
    (ptNameLabel)                                                                                              : expectedName,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West'))      : expectedLocation,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)'))               : expectedProvider,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason'))                        : expectedReason,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026', ['date': tomorrowFullDate]))  : expectedDate,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM', ['time': apptTime]))            : expectedTime,
    (findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked'))              : expectedConfirmationMsg,
])

KeywordUtil.logInfo('Part 1 complete: Chat Bot Appointment Booking verified successfully')
KeywordUtil.markPassed('Chat Bot Appointment Booking test case passed successfully')

/* =============================================================================
 * PART 2: LOGIN TO MAXIMEYES AND VERIFY THE BOOKED APPOINTMENT
 * ========================================================================== */

// STEP 1: Login to MaximEyes
KeywordUtil.logInfo('Step 1: Logging into MaximEyes')

// Ensure we're at the top-level document, not inside an iframe
WebUI.switchToDefaultContent()

String maxEyesUrl = GlobalVariable.MaxUrlQA5
KeywordUtil.logInfo('Opening URL: ' + maxEyesUrl)
WebUI.executeJavaScript("window.open(arguments[0], '_blank');", [maxEyesUrl])
WebUI.delay(2)
WebUI.switchToWindowIndex(1)

WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

// STEP 1a: Handle OTP verification if MaximEyes prompts for it
TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')
if (WebUI.waitForElementVisible(otpField, 10, FailureHandling.OPTIONAL)) {
    KeywordUtil.logInfo('Step 1a: OTP field detected - fetching verification code from email')
    String maxEyesOtp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(
        GlobalVariable.MyEmail_Id,
        GlobalVariable.Email_Key)
    KeywordUtil.logInfo("Step 1a: OTP retrieved = ${maxEyesOtp}")

    WebUI.setText(otpField, maxEyesOtp)
    WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
    WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), 10)
    KeywordUtil.logInfo('Step 1a: OTP verified successfully, patient list visible')
} else {
    KeywordUtil.logInfo('Step 1a: OTP field not visible - skipping OTP fetch and entry')
}

// STEP 2-3: Search for the patient booked in Part 1 and verify the appointment slot
KeywordUtil.logInfo('Step 2-3: Searching for patient and verifying original appointment slot')
searchPatientAndVerifyAppointment(firstName, lastName, expectedDateTimeReason)

// STEP 4: Navigate to the Schedule module and verify on the scheduler/calendar view
KeywordUtil.logInfo('Step 4: Verifying appointment on the scheduler view')
verifyAppointmentOnScheduler(expectedSpanText)

/* =============================================================================
 * PART 3: RESCHEDULE THE APPOINTMENT VIA THE CHAT BOT
 * ========================================================================== */

// Navigate back to the chat bot window
KeywordUtil.logInfo('Switching back to the chat bot window')
WebUI.switchToWindowIndex(0)

// STEP 1: Ask the bot to reschedule the appointment
KeywordUtil.logInfo('Step 1: Requesting "Reschedule Appointment" via chat input')
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'), 'Reschedule Appointment')
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'), Keys.chord(Keys.ENTER))

WebUI.waitForElementVisible(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'), PAGE_TIMEOUT)
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))

// STEP 2: Open the appointment card action menu
KeywordUtil.logInfo('Step 2: Clicking the Confirm/Cancel/Reschedule action button on the appointment card')
WebUI.click(findTestObject('Appointment Booking/Reschedule Appt/button_Confirm _ Cancel _ Reschedule'))

// STEP 3: Verify the "already confirmed" warning message is displayed
KeywordUtil.logInfo('Step 3: Verifying "already confirmed" message is displayed')
WebUI.assertElementText(
    findTestObject('Appointment Booking/Reschedule Appt/p_This appointment is already confirmed'),
    alreadyConfirmedMessage,
    0)
KeywordUtil.logInfo('Step 3: "Already confirmed" message displayed as expected')

// STEP 4: Click the "Reschedule" button
KeywordUtil.logInfo('Step 4: Clicking the Reschedule button')
WebUI.click(findTestObject('Appointment Booking/Reschedule Appt/button_Reschedule'))

// STEP 5: Re-select Location, Provider, and Reason for the reschedule flow
KeywordUtil.logInfo('Step 5: Re-selecting Location, Provider, and Reason for reschedule')
selectLocationProviderReason(location, provider, reason)


// ---------------------------------------------------------------------
// Test Data / Expected Text (previously hardcoded inline)
// ---------------------------------------------------------------------
String expectedHeadingText = 'Choose Appointment Date'
String expectedNoSlotsMessage = 'We\'re sorry, but no appointment slots are currently available. ' +
		'Please use the back button to modify your search, or call us at +1-232-333-3333 so we can assist you further.'
String expectedLocationLabel = 'Location*'
String expectedRescheduleGreeting = 'Sure! Please bear with me for a second while I pull up the form to reschedule your appointment.'
 
// ---------------------------------------------------------------------
// Test Objects (resolved once and reused, instead of calling
// findTestObject() repeatedly for the same element)
// ---------------------------------------------------------------------
def noSlotsHeading   = findTestObject('Appointment Booking/Reschedule Appt/No Slots/h2_Choose Appointment Date')
def noSlotsMessage   = findTestObject('Appointment Booking/Reschedule Appt/No Slots/p_Were sorry, but no appointment slots are curr')
def backButton       = findTestObject('Appointment Booking/Reschedule Appt/No Slots/button_BACK (1)')
def exitButton       = findTestObject('Appointment Booking/Reschedule Appt/No Slots/button_EXIT')
def locationLabel    = findTestObject('Appointment Booking/Reschedule Appt/No Slots/label_Location (1)')
def locationSelect   = findTestObject('Appointment Booking/Reschedule Appt/No Slots/select_Location (1)')
def rescheduleGreeting = findTestObject('Appointment Booking/Reschedule Appt/No Slots/p_Sure Please bear with me for a second while I')
 
// ---------------------------------------------------------------------
// Step 1: Verify "No Slots" page heading
// ---------------------------------------------------------------------
WebUI.comment('Step 1: Verifying "No Slots" page heading text is displayed correctly')
WebUI.assertElementText(noSlotsHeading, expectedHeadingText, 0)
 
// ---------------------------------------------------------------------
// Step 2: Verify "No Slots" explanatory message
// ---------------------------------------------------------------------
WebUI.comment('Step 2: Verifying the "no appointment slots available" message text')
WebUI.assertElementText(noSlotsMessage, expectedNoSlotsMessage, 0)
 
// ---------------------------------------------------------------------
// Step 3: Verify BACK and EXIT buttons are present
// ---------------------------------------------------------------------
WebUI.comment('Step 3: Verifying BACK button is present on the page')
WebUI.assertElementPresent(backButton, 0)
 
WebUI.comment('Step 4: Verifying EXIT button is present on the page')
WebUI.assertElementPresent(exitButton, 0)
 
// ---------------------------------------------------------------------
// Step 5: Click BACK and verify user returns to the search form
// ---------------------------------------------------------------------
WebUI.comment('Step 5: Clicking BACK button to return to the search form')
WebUI.click(backButton)
 
WebUI.comment('Step 6: Verifying "Location*" label is displayed on the search form')
WebUI.assertElementText(locationLabel, expectedLocationLabel, 0)
 
WebUI.comment('Step 7: Verifying Location dropdown is present on the search form')
WebUI.assertElementPresent(locationSelect, 0)
 
// ---------------------------------------------------------------------
// Step 6: Click EXIT and re-verify the "No Slots" message still displays
// ---------------------------------------------------------------------
WebUI.comment('Step 8: Clicking EXIT button')
WebUI.click(exitButton)
 
WebUI.comment('Step 9: Re-verifying the "no appointment slots available" message text')
WebUI.assertElementText(noSlotsMessage, expectedNoSlotsMessage, 0)
 
// ---------------------------------------------------------------------
// Step 7: Click EXIT again and verify reschedule flow greeting appears
// ---------------------------------------------------------------------
WebUI.comment('Step 10: Clicking EXIT button again to proceed')
WebUI.click(exitButton)
 
WebUI.comment('Step 11: Verifying reschedule flow greeting message is displayed')
WebUI.assertElementText(rescheduleGreeting, expectedRescheduleGreeting, 0)
 
WebUI.comment('Test completed: Reschedule Appointment - No Slots flow validated successfully')


/* =============================================================================
 * PART 2: LOGIN TO MAXIMEYES AND VERIFY THE BOOKED APPOINTMENT
 * ========================================================================== */

// STEP 1: Login to MaximEyes
KeywordUtil.logInfo('Step 1: Logging into MaximEyes')

// Ensure we're at the top-level document, not inside an iframe
WebUI.switchToDefaultContent()

String maxEyesUrl = GlobalVariable.MaxUrlQA5
KeywordUtil.logInfo('Opening URL: ' + maxEyesUrl)
WebUI.executeJavaScript("window.open(arguments[0], '_blank');", [maxEyesUrl])
WebUI.delay(2)
WebUI.switchToWindowIndex(1)

WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

// STEP 1a: Handle OTP verification if MaximEyes prompts for it
TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')
if (WebUI.waitForElementVisible(otpField, 10, FailureHandling.OPTIONAL)) {
	KeywordUtil.logInfo('Step 1a: OTP field detected - fetching verification code from email')
	String maxEyesOtp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(
		GlobalVariable.MyEmail_Id,
		GlobalVariable.Email_Key)
	KeywordUtil.logInfo("Step 1a: OTP retrieved = ${maxEyesOtp}")

	WebUI.setText(otpField, maxEyesOtp)
	WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
	WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), 10)
	KeywordUtil.logInfo('Step 1a: OTP verified successfully, patient list visible')
} else {
	KeywordUtil.logInfo('Step 1a: OTP field not visible - skipping OTP fetch and entry')
}

// STEP 2-3: Search for the patient booked in Part 1 and verify the appointment slot
KeywordUtil.logInfo('Step 2-3: Searching for patient and verifying original appointment slot')
searchPatientAndVerifyAppointment(firstName, lastName, expectedDateTimeReason)

// STEP 4: Navigate to the Schedule module and verify on the scheduler/calendar view
KeywordUtil.logInfo('Step 4: Verifying appointment on the scheduler view')
verifyAppointmentOnScheduler(expectedSpanText)