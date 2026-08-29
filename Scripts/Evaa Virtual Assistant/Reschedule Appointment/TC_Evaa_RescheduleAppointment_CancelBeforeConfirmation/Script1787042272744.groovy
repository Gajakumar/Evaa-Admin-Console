import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys
import groovy.transform.Field
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import appointment.AppointmentKeywords

/* =============================================================================
 * TEST DATA
 * ========================================================================== */

// ---- Timeouts (named instead of magic numbers scattered through the script) ----
@Field final int NO_WAIT             = 5
@Field final int SHORT_TIMEOUT       = 3
@Field final int MEDIUM_TIMEOUT      = 15
@Field final int OTP_TIMEOUT         = 10
@Field final int PAGE_TIMEOUT        = 30
@Field final int WINDOW_SWITCH_DELAY = 2

// ---- Application URLs ----
@Field String appUrl = 'https://qa5.eyeclinic.ai/'

// ---- Patient details ----
@Field String firstName   = 'Ronny'
@Field String lastName    = 'Kevin'
@Field String dob         = '01/21/1978'
@Field String phoneNumber = '111-111-1111'
@Field String otpCode     = '9753'

// ---- Appointment details ----
@Field String location          = 'Katalon Location'
@Field String provider          = 'Katalon Provider'
@Field String providerFirstName = 'Katalon'
@Field String reason            = 'Katalon Reason'
@Field String reasonText        = 'Katalon Appointment'
@Field String apptTime          = '10:45 AM'

// ---- Reschedule details ----
@Field String rescheduleReasonInput = 'Reschedule Appointment'

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
@Field int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

// ---- Expected values - all derived from the data above, never re-typed ----
@Field String expectedName            = "Name: ${firstName} ${lastName}"
@Field String expectedLocation        = "Location: ${location}"
@Field String expectedProvider        = "Provider: ${provider} (OD)"
@Field String expectedReason          = "Reason: ${reason}"
String expectedDate                   = "Date: ${tomorrowFullDate}"
@Field String expectedTime            = "Time: ${apptTime}"
@Field String expectedConfirmationMsg = 'Your appointment has been booked'
String expectedDateTimeReason         = "${tomorrowFullDate} | ${apptTime} | ${reason}"
@Field String expectedPatientLocation = "${providerFirstName}, ${location}"
String expectedSpanText               = "${tomorrowFullDate} | ${reason}"

@Field TestObject loadingIcon = findTestObject('Appointment Booking/Reschedule Appt/Loading Icon')
TestObject rescheduleGreeting = findTestObject('Appointment Booking/Reschedule Appt/No Slots/p_Sure Please bear with me for a second while I')

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
	KeywordUtil.logInfo('Location/Provider/Reason submitted, moved to date selection')
}

/**
 * Validates the calendar widget (past dates disabled / future dates enabled),
 * handles month rollover if "tomorrow" falls in the next month, then picks
 * the target day and advances.
 */
def selectAppointmentDate(String targetDay, String targetFullDate) {
	KeywordUtil.logInfo("Selecting appointment date - Day '${targetDay}' (${targetFullDate})")
	WebUI.waitForElementNotVisible(loadingIcon, MEDIUM_TIMEOUT)

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
	WebUI.waitForElementNotVisible(findTestObject('Maximeye.com/Busy Indicator'), 30)

	KeywordUtil.logInfo("Verifying appointment span text matches '${expectedSpan}'")
	WebUI.assertElementText(
		findTestObject('MaximeyesAppt/Page_MaximEyes/span_07_23_2026 _ Katalon Reason'),
		expectedSpan,
		NO_WAIT)

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

// STEPS 10-12: Enter reason free-text, choose Self Pay, finish booking
KeywordUtil.logInfo('Step 10-12: Entering reason, selecting payment option, and finishing booking')
enterReasonAndFinishBooking(reasonText)

// STEPS 13-19: Verify confirmation screen
KeywordUtil.logInfo('Step 13-19: Verifying booking confirmation screen')
TestObject ptNameLabel = findTestObject('Appointment Booking/Chat Bot Appt Book/p_Name_ QA Katalon')
verifyConfirmationScreen(ptNameLabel, [
	(ptNameLabel)                                                                                          : expectedName,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Location_ MaximEyes Family Eye Care West'))  : expectedLocation,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Provider_ Katalon Provider (OD)'))           : expectedProvider,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Reason_ Katalon Reason'))                    : expectedReason,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Date_ 07_22_2026', ['date': tomorrowFullDate])): expectedDate,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Time_ 10_30 AM', ['time': apptTime]))        : expectedTime,
	(findTestObject('Appointment Booking/Chat Bot Appt Book/p_Your appointment has been booked'))          : expectedConfirmationMsg,
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
WebUI.delay(WINDOW_SWITCH_DELAY)
WebUI.switchToWindowIndex(1)

WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

// STEP 1a: Handle OTP verification if MaximEyes prompts for it
TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')
if (WebUI.waitForElementVisible(otpField, OTP_TIMEOUT, FailureHandling.OPTIONAL)) {
	KeywordUtil.logInfo('Step 1a: OTP field detected - fetching verification code from email')
	String maxEyesOtp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(
		GlobalVariable.MyEmail_Id,
		GlobalVariable.Email_Key)
	KeywordUtil.logInfo("Step 1a: OTP retrieved = ${maxEyesOtp}")

	WebUI.setText(otpField, maxEyesOtp)
	WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
	WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), OTP_TIMEOUT)
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

KeywordUtil.logInfo('Part 2 complete: Appointment verified successfully in MaximEyes')

/* =============================================================================
 * PART 3: RESCHEDULE THE APPOINTMENT VIA THE CHAT BOT
 * ========================================================================== */

// Navigate back to the chat bot window
KeywordUtil.logInfo('Switching back to the chat bot window')
WebUI.switchToWindowIndex(0)

// STEP 1: Ask the bot to reschedule the appointment
KeywordUtil.logInfo('Step 1: Requesting reschedule via chat input')
WebUI.sendKeys(findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'), rescheduleReasonInput)
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
	NO_WAIT)
KeywordUtil.logInfo('Step 3: "Already confirmed" message displayed as expected')

// STEP 4: Click the "Reschedule" button
KeywordUtil.logInfo('Step 4: Clicking the Reschedule button')
WebUI.click(findTestObject('Appointment Booking/Reschedule Appt/button_Reschedule'))

// STEP 5: Re-select Location, Provider, and Reason for the reschedule flow
KeywordUtil.logInfo('Step 5: Re-selecting Location, Provider, and Reason for reschedule')
selectLocationProviderReason(location, provider, reason)
WebUI.waitForElementNotVisible(loadingIcon, MEDIUM_TIMEOUT)

// STEP 6: Abandon the reschedule (Cancel -> Leave)
KeywordUtil.logInfo('Step 6: Cancelling out of the reschedule flow')
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/Cancel Button on chat'))
WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Leave'))

KeywordUtil.logInfo('Verifying reschedule flow greeting message is displayed')
WebUI.assertElementText(rescheduleGreeting, medicalDisclaimerText, NO_WAIT)

/* =============================================================================
 * PART 4: RE-VERIFY THE ORIGINAL APPOINTMENT WAS NOT MODIFIED
 * ========================================================================== */

WebUI.delay(WINDOW_SWITCH_DELAY)
WebUI.switchToWindowIndex(1)

// STEP 1: Re-search for the patient and re-verify the appointment slot is unchanged
KeywordUtil.logInfo('Step 1: Re-searching for patient and verifying appointment slot is unchanged')
searchPatientAndVerifyAppointment(firstName, lastName, expectedDateTimeReason)

// STEP 2: Re-verify on the scheduler/calendar view
KeywordUtil.logInfo('Step 2: Re-verifying appointment on the scheduler view')
verifyAppointmentOnScheduler(expectedSpanText)

// STEP 3: Open the Appointment Actions dropdown
KeywordUtil.logInfo('Step 3: Clicking Appointment Actions dropdown')
TestObject appointmentDropdown = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-dropdown fg-skyblue')
WebUI.waitForElementClickable(appointmentDropdown, MEDIUM_TIMEOUT)
WebUI.click(appointmentDropdown)
KeywordUtil.logInfo('Appointment Actions dropdown opened successfully')

// STEP 4: Select 'Cancel Appt (Office Request)'
KeywordUtil.logInfo("Step 4: Selecting 'Cancel Appt (Office Request)'")
TestObject cancelAppointment = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/div_Cancel Appt (Office Request)')
WebUI.waitForElementClickable(cancelAppointment, MEDIUM_TIMEOUT)
WebUI.click(cancelAppointment)
KeywordUtil.logInfo("'Cancel Appt (Office Request)' selected successfully")

// STEP 5: Navigate to Settings (OA)
KeywordUtil.logInfo('Step 5: Clicking Settings icon (OA)')
TestObject settingsIcon = findTestObject('Maximeyes Evaa Login/Page_MaximEyes/span_mif-cog font20 head-icon-shadow fg-white')
WebUI.waitForElementClickable(settingsIcon, MEDIUM_TIMEOUT)
WebUI.click(settingsIcon)
KeywordUtil.logInfo('OA page opened successfully')

WebUI.comment('TEST COMPLETE: All appointment detail verifications for patient "QA Katalon" passed')
KeywordUtil.logInfo('Part 4 complete: Confirmed original appointment was NOT modified after failed reschedule attempt')
