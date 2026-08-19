package common

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import CustomKeywords

import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webui.common.WebUiCommonHelper


/**
 * ChatBotBookingFlow
 * ----------------------------------------------------------------------------
 * Centralizes the EVAA chat-bot appointment booking journey that is repeated
 * (with only small variations) across every script in
 * "Scripts/Evaa Virtual Assistant":
 *
 *   navigate -> push-to-talk -> Book Appointment -> disclaimer/confirm prompt
 *   -> patient details -> OTP -> location/provider/reason -> date -> time
 *   -> reason/finish -> confirmation screen
 *
 * Each step is its own @Keyword so a test can call only the steps it needs
 * (e.g. TC_Evaa_Verify_BookingNotStarted_WhenUserSelectsNo only needs steps
 * 1-4 plus the decline path) and stop wherever its scenario diverges from
 * the happy path (invalid OTP, no available slots, disabled reschedule,
 * etc). A `bookAppointmentHappyPath(...)` convenience method is also
 * provided for tests that just want to get a normal appointment booked
 * quickly, e.g. as setup for a MaximEyes-side verification.
 *
 * HOW TO USE FROM A TEST SCRIPT
 *   CustomKeywords.'common.ChatBotBookingFlow.navigateToApp'()
 *   CustomKeywords.'common.ChatBotBookingFlow.launchChatBot'()
 *   CustomKeywords.'common.ChatBotBookingFlow.selectBookAppointment'()
 *   CustomKeywords.'common.ChatBotBookingFlow.verifyMedicalDisclaimer'()
 *   CustomKeywords.'common.ChatBotBookingFlow.verifyBookingConfirmationPrompt'()
 *   CustomKeywords.'common.ChatBotBookingFlow.confirmBookingIntent'(true)
 *   CustomKeywords.'common.ChatBotBookingFlow.enterPatientDetails'(firstName, lastName, dob, phoneNumber)
 *   CustomKeywords.'common.ChatBotBookingFlow.enterOtpAndSubmit'(otpCode)
 *   CustomKeywords.'common.ChatBotBookingFlow.selectLocationProviderReason'(location, provider, reason)
 *   CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentDate'(targetDay, targetFullDate, todayDay)
 *   CustomKeywords.'common.ChatBotBookingFlow.selectAppointmentTime'(apptTime)
 *   CustomKeywords.'common.ChatBotBookingFlow.enterReasonAndFinishBooking'(reasonText)
 *   CustomKeywords.'common.ChatBotBookingFlow.verifyConfirmationScreen'(assertionsMap)
 *
 * If a locator ever changes (e.g. the "Push to talk" icon, the disclaimer
 * copy, the OTP inputs), it now only needs to change here instead of in
 * every one of the 17 test scripts.
 */
class ChatBotBookingFlow {

	private static final int DEFAULT_PAGE_TIMEOUT    = 30
	private static final int DEFAULT_SHORT_TIMEOUT   = 5
	private static final String DEFAULT_APP_URL      = 'https://qa5.eyeclinic.ai/'

	static final String MEDICAL_DISCLAIMER_TEXT =
		"Online appointment booking is only for routine exam and follow up appointments and should not be used " +
		"if you have any urgent or concerning medical issues. If experiencing medical issues please call our " +
		"office during office hours. If outside of office hours please call 911 or visit an urgent care or " +
		"emergency room for immediate assistance."

	static final String BOOKING_CONFIRM_PROMPT_TEXT = 'Do you want to proceed with booking an appointment? Yes No'
	TestObject loadingIcon = findTestObject('Appointment Booking/Reschedule Appt/Loading Icon')

	// ------------------------------------------------------------------
	// STEP 1-3: Navigate, launch chat bot, select "Book Appointment"
	// ------------------------------------------------------------------
	
	@Keyword
	def waitForLoadingIconToDisappear(int timeout = 15) {
		KeywordUtil.logInfo('Waiting for loading spinner (wizard-loading-icon) to disappear')
 
		TestObject loadingIcon = new TestObject('wizard_loading_icon')
		loadingIcon.addProperty('xpath', com.kms.katalon.core.testobject.ConditionType.EQUALS,
			"//*[contains(concat(' ', normalize-space(@class), ' '), ' wizard-loading-icon ')]")
 
		boolean disappeared = WebUI.waitForElementNotVisible(loadingIcon, timeout)
 
		if (disappeared) {
			KeywordUtil.logInfo('Loading spinner is no longer visible')
		} else {
			KeywordUtil.markWarning('Loading spinner was still visible after ' + timeout + ' seconds')
		}
		return disappeared
	}

	@Keyword
	def navigateToApp(String url = DEFAULT_APP_URL) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Navigating to ${url}")
		WebUI.navigateToUrl(url)
	}

	@Keyword
	def launchChatBot(int timeout = DEFAULT_PAGE_TIMEOUT) {
		KeywordUtil.logInfo('ChatBotBookingFlow: Launching chat bot via "Push to talk" icon')
		TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
		WebUI.waitForElementVisible(pushToTalk, timeout)
		WebUI.click(pushToTalk)
	}

	@Keyword
	def selectBookAppointment(int timeout = DEFAULT_PAGE_TIMEOUT) {
		KeywordUtil.logInfo('ChatBotBookingFlow: Selecting "Book Appointment"')
		TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
		WebUI.waitForElementVisible(bookAppt, timeout)
		WebUI.click(bookAppt)
	}

	// ------------------------------------------------------------------
	// STEP 3a-3b: Disclaimer + confirmation prompt verification
	// ------------------------------------------------------------------

	@Keyword
	def verifyMedicalDisclaimer() {
		KeywordUtil.logInfo('ChatBotBookingFlow: Verifying Medical Disclaimer is displayed')
		String actual = WebUI.getText(
			findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/Medical Disclaimer')
		).replaceAll("\\s+", " ").trim()
		WebUI.verifyMatch(actual, MEDICAL_DISCLAIMER_TEXT, false)
	}

	@Keyword
	def verifyBookingConfirmationPrompt() {
		KeywordUtil.logInfo('ChatBotBookingFlow: Verifying booking confirmation prompt is displayed with Yes/No options')
		String actual = WebUI.getText(
			findTestObject('Appointment Booking/Chat Bot Appt Book/EVAA.AI React/div_Do you want to proceed with booking an appoi')
		).replaceAll("\\s+", " ").trim()
		WebUI.verifyMatch(actual, BOOKING_CONFIRM_PROMPT_TEXT, false)
	}

	/**
	 * Clicks "Yes" to proceed with booking, or "No" to decline.
	 */
	@Keyword
	def confirmBookingIntent(boolean proceed = true) {
		String button = proceed ? 'Yes' : 'No'
		KeywordUtil.logInfo("ChatBotBookingFlow: Clicking \"${button}\" on booking confirmation prompt")
		WebUI.click(findTestObject("Appointment Booking/Chat Bot Appt Book/button_${button}"))
	}

	/**
	 * Verifies the "Is there anything else I can assist you with today?"
	 * message shown after declining to book (or after the bot finishes
	 * helping with something).
	 */
	@Keyword
	def verifyAnythingElseAssistMessage(String expectedText = 'Is there anything else I can assist you with today?') {
		KeywordUtil.logInfo('ChatBotBookingFlow: Verifying "anything else" assist message is displayed')
		String actual = WebUI.getText(
			findTestObject('Appointment Booking/Chat Bot Appt Book/p_Is there anything else I can assist you with today')
		).replaceAll("\\s+", " ").trim()
		WebUI.verifyMatch(actual, expectedText, false)
	}

	// ------------------------------------------------------------------
	// STEP 5: Patient personal details
	// ------------------------------------------------------------------

	@Keyword
	def enterPatientDetails(String firstName, String lastName, String dob, String phoneNumber, int timeout = DEFAULT_PAGE_TIMEOUT) {
		KeywordUtil.logInfo('ChatBotBookingFlow: Entering patient personal details')

		TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
		WebUI.waitForElementVisible(firstNameField, timeout)
		WebUI.setText(firstNameField, firstName)

		WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Last Name'), lastName)

		TestObject dobField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_mm_dd_yyyy')
		WebUI.setText(dobField, dob)
		WebUI.sendKeys(dobField, Keys.chord(Keys.ESCAPE))

		WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/input_XXX-XXX-XXXX'), phoneNumber)

		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
		KeywordUtil.logInfo("ChatBotBookingFlow: Submitted - ${firstName} ${lastName}, DOB ${dob}, Phone ${phoneNumber}")
	}

	// ------------------------------------------------------------------
	// STEP 6: OTP entry
	// ------------------------------------------------------------------

	/**
	 * Fills the otp-0..otp-N inputs one digit at a time (clearing any
	 * existing value first). Does NOT click NEXT - call submitOtp()
	 * afterwards, or use enterOtpAndSubmit() to do both in one call.
	 * Splitting these lets negative-path tests (invalid OTP, max
	 * attempts) re-enter digits multiple times between assertions.
	 */
	@Keyword
	def enterOtpDigits(String code, int timeout = DEFAULT_PAGE_TIMEOUT) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Entering OTP digits: '${code}'")
		TestObject otpFirstDigit = findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0')
		WebUI.waitForElementVisible(otpFirstDigit, timeout)

		code.eachWithIndex { String digit, int i ->
	WebUI.sendKeys(findTestObject("Appointment Booking/Chat Bot Appt Book/input_otp-${i}"), digit)
}
	}

	@Keyword
	def submitOtp(int timeout = DEFAULT_PAGE_TIMEOUT) {
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
		
		
	}

	@Keyword
	def enterOtpAndSubmit(String code, int timeout = DEFAULT_PAGE_TIMEOUT) {
		enterOtpDigits(code, timeout)
		submitOtp(timeout)
		KeywordUtil.logInfo("ChatBotBookingFlow: OTP '${code}' entered and submitted")
	}

	@Keyword
	def verifyOtpErrorMessage(String expectedMessage, int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.assertElementText(
			findTestObject('Appointment Booking/Chat Bot Appt Book/p_Invalid One-Time Password (OTP). Please check'),
			expectedMessage,
			timeout)
	}

	@Keyword
	def verifyOtpFieldDisabled(int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.verifyElementHasAttribute(findTestObject('Appointment Booking/Chat Bot Appt Book/input_otp-0'), 'disabled', timeout)
	}

	@Keyword
	def verifyResendOtpButtonDisabled(int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.verifyElementHasAttribute(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Resend OTP'), 'disabled', timeout)
	}

	@Keyword
	def verifyNextButtonDisabled(int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.verifyElementHasAttribute(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT (1)'), 'disabled', timeout)
	}

	@Keyword
	def clickResendOtp() {
		KeywordUtil.logInfo('ChatBotBookingFlow: Clicking "Resend OTP"')
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Resend OTP'))
	}

	// ------------------------------------------------------------------
	// Location / Provider / Reason selection
	// ------------------------------------------------------------------

	@Keyword
	def selectLocationProviderReason(String location, String provider, String reason) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Selecting Location='${location}', Provider='${provider}', Reason='${reason}'")

		WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Location'), location, false)
		CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'(3)
		WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Provider'), provider, false)
		CustomKeywords.'common.ChatBotBookingFlow.waitForLoadingIconToDisappear'(3)
		WebUI.selectOptionByLabel(findTestObject('Appointment Booking/Chat Bot Appt Book/select_Reason'), reason, false)

		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
		
	}

	// ------------------------------------------------------------------
	// Date / Time selection
	// ------------------------------------------------------------------

	/**
	 * Validates the calendar widget (past dates disabled / future dates
	 * enabled), handles month rollover if the target day falls in the
	 * next month, then picks the target day and advances.
	 *
	 * @param targetDay      day-of-month to click, e.g. "23"
	 * @param targetFullDate full date string, only used for logging
	 * @param todayDay       today's day-of-month, used to detect month rollover
	 */
	@Keyword
	def selectAppointmentDate(String targetDay, String targetFullDate, int todayDay, int timeout = 15) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Selecting appointment date - Day '${targetDay}' (${targetFullDate})")

		TestObject loadingIcon = findTestObject('Appointment Booking/Reschedule Appt/Loading Icon')
		WebUI.waitForElementNotVisible(loadingIcon, timeout)

		CustomKeywords.'common.CalendarHelper.verifyPastDatesDisabled'(
			findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'))
		CustomKeywords.'common.CalendarHelper.verifyAvailableDatesEnabled'(
			findTestObject('Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'))

		if (Integer.parseInt(targetDay) < todayDay) {
			KeywordUtil.logInfo('ChatBotBookingFlow: Month rollover detected - navigating to next month')
			WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/Calender Next Month Btn'))
		}

		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_22', ['day': targetDay]))
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
	}

	@Keyword
	def selectAppointmentTime(String time) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Selecting appointment time - ${time}")
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_10_30 AM', ['time': time]))
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
	}

	@Keyword
	def verifyNoAvailableSlotsMessage(String expectedText) {
		KeywordUtil.logInfo('ChatBotBookingFlow: Verifying "no available slots" message is displayed')
		WebUI.assertElementText(
			findTestObject('Appointment Booking/No Slots/Page_Home - EyeMax EyeCare  Optometrists  Comprehensive Eye Care - EVAA.AI React'),
			expectedText,
			DEFAULT_SHORT_TIMEOUT)
	}

	// ------------------------------------------------------------------
	// Reason for visit + Self Pay + finish
	// ------------------------------------------------------------------

	/**
	 * Enters the free-text reason/symptoms and advances. Used standalone by
	 * flows that continue into insurance-card upload afterwards, and as the
	 * first step of enterReasonAndFinishBooking() for the Self Pay flow.
	 */
	@Keyword
	def enterReasonForVisit(String freeTextReason) {
		KeywordUtil.logInfo("ChatBotBookingFlow: Entering reason for visit - '${freeTextReason}'")
		WebUI.setText(findTestObject('Appointment Booking/Chat Bot Appt Book/textarea_Describe your symptoms or reason for th'), freeTextReason)
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_NEXT'))
	}

	/**
	 * Enters the free-text reason/symptoms, selects Self Pay, and finishes booking.
	 */
	@Keyword
	def enterReasonAndFinishBooking(String freeTextReason) {
		enterReasonForVisit(freeTextReason)

		KeywordUtil.logInfo('ChatBotBookingFlow: Selecting "Self Pay / No Insurance Available"')
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/input_Self Pay_No Insurance Available'))

		KeywordUtil.logInfo('ChatBotBookingFlow: Finishing booking')
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_FINISH BOOKING'))
	}

	// ------------------------------------------------------------------
	// Confirmation screen
	// ------------------------------------------------------------------

	/**
	 * Waits for the confirmation screen and verifies every field on it.
	 * @param waitObject the object to wait on before asserting (e.g. the name label)
	 * @param assertions map of TestObject -> expected text
	 */
	@Keyword
	def verifyConfirmationScreen(TestObject waitObject, LinkedHashMap<TestObject, String> assertions, int pageTimeout = DEFAULT_PAGE_TIMEOUT, int shortTimeout = DEFAULT_SHORT_TIMEOUT) {
		KeywordUtil.logInfo('ChatBotBookingFlow: Verifying confirmation screen details')
		WebUI.waitForElementVisible(waitObject, pageTimeout)
		assertions.each { TestObject obj, String expectedText ->
			WebUI.assertElementText(obj, expectedText, shortTimeout)
		}
		KeywordUtil.logInfo('ChatBotBookingFlow: Confirmation screen verified successfully')
	}

	@Keyword
	def declineFurtherAssistance(String expectedPromptText = 'Is there anything else I can help with?', int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Is there anything else I can help with'), expectedPromptText, timeout)
		TestObject noButton = findTestObject('Book Appt With Ins/EVAA.AI React/button_No')
		WebUI.waitForElementVisible(noButton, DEFAULT_PAGE_TIMEOUT)
		WebUI.click(noButton)
	}

	@Keyword
	def skipFeedbackSurvey(String expectedTitleText = 'Share Your Feedback', int timeout = DEFAULT_SHORT_TIMEOUT) {
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h2_Share Your Feedback'), expectedTitleText, timeout)
		WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_Skip'))
	}

	// ------------------------------------------------------------------
	// Composite: full happy-path booking in one call
	// ------------------------------------------------------------------

	/**
	 * Runs the entire happy-path chat-bot booking flow end to end: launch
	 * -> book appointment -> disclaimer/prompt -> patient details -> OTP
	 * -> location/provider/reason -> date -> time -> reason/finish.
	 * Stops right before the confirmation screen so the caller can verify
	 * whatever fields matter for its own scenario.
	 *
	 * @param patient  map with keys: firstName, lastName, dob, phoneNumber, otpCode
	 * @param appt     map with keys: location, provider, reason, targetDay,
	 *                 targetFullDate, todayDay, apptTime, freeTextReason
	 */
	@Keyword
	def bookAppointmentHappyPath(Map patient, Map appt, String appUrl = DEFAULT_APP_URL) {
		navigateToApp(appUrl)
		launchChatBot()
		selectBookAppointment()
		verifyMedicalDisclaimer()
		verifyBookingConfirmationPrompt()
		confirmBookingIntent(true)
		enterPatientDetails(patient.firstName, patient.lastName, patient.dob, patient.phoneNumber)
		enterOtpAndSubmit(patient.otpCode)
		selectLocationProviderReason(appt.location, appt.provider, appt.reason)
		selectAppointmentDate(appt.targetDay, appt.targetFullDate, appt.todayDay)
		selectAppointmentTime(appt.apptTime)
		enterReasonAndFinishBooking(appt.freeTextReason)
	}


@Keyword
def sendChatBotMessage(String message, int timeout = DEFAULT_SHORT_TIMEOUT) {

	TestObject chatTextArea = findTestObject(
		'Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area'
	)

	TestObject nextButton = findTestObject(
		'Appointment Booking/Chat Bot Appt Book/button_NEXT'
	)

	KeywordUtil.logInfo("ChatBotBookingFlow: Sending message '${message}'")

	// Enter message
	WebUI.sendKeys(chatTextArea, message)

	// Press Enter
	WebUI.sendKeys(chatTextArea, Keys.chord(Keys.ENTER))

	// Wait for NEXT button
	WebUI.waitForElementVisible(nextButton, timeout)
}

@Keyword
def declineAssistanceAndEndChat(
    String anythingElseHelpText,
    String shareFeedbackTitleText,
    String restartChatText,
    int timeout = DEFAULT_SHORT_TIMEOUT
) {

    // STEP 20: Decline further assistance
    KeywordUtil.logInfo(
        'STEP 20: Verifying "anything else" prompt and declining further assistance'
    )

    TestObject anythingElsePrompt = findTestObject(
        'Book Appt With Ins/EVAA.AI React/p_Is there anything else I can help with'
    )

    TestObject noButton = findTestObject(
        'Book Appt With Ins/EVAA.AI React/button_No'
    )

    WebUI.waitForElementVisible(anythingElsePrompt, timeout)
    WebUI.verifyElementText(
        anythingElsePrompt,
        anythingElseHelpText
    )

    WebUI.waitForElementVisible(noButton, timeout)
    WebUI.click(noButton)

    KeywordUtil.logInfo(
        'STEP 20: Declined further assistance'
    )


    // STEP 21: Skip feedback survey
    KeywordUtil.logInfo(
        'STEP 21: Verifying Feedback screen is displayed and skipping it'
    )

    TestObject feedbackTitle = findTestObject(
        'Book Appt With Ins/EVAA.AI React/h2_Share Your Feedback'
    )

    TestObject skipButton = findTestObject(
        'Book Appt With Ins/EVAA.AI React/button_Skip'
    )

    WebUI.waitForElementVisible(feedbackTitle, timeout)
    WebUI.verifyElementText(
        feedbackTitle,
        shareFeedbackTitleText
    )

    WebUI.waitForElementVisible(skipButton, timeout)
    WebUI.click(skipButton)

    KeywordUtil.logInfo(
        'STEP 21: Feedback survey skipped'
    )


    // STEP 22: Verify chat session ended
    KeywordUtil.logInfo(
        'STEP 22: Verifying "Restart Chat" option is available at end of flow'
    )

    TestObject restartChatButton = findTestObject(
        'Book Appt With Ins/EVAA.AI React/button_Restart Chat'
    )

    WebUI.waitForElementVisible(restartChatButton, timeout)
    WebUI.verifyElementText(
        restartChatButton,
        restartChatText
    )

    KeywordUtil.logInfo(
        'STEP 22: Test flow completed successfully - "Restart Chat" is available'
    )
}

@Keyword
def verifyAppointmentTimeSlot(String apptTime, boolean shouldBeAvailable, int timeout = 20) {

	TestObject timeSlots = findTestObject(
		'Appointment Booking/Chat Bot Appt Book/Appointment Time Slots'
	)

	TestObject iframe = findTestObject(
		'Appointment Booking/Chat Bot Appt Book/iframe_evaas-iframeId'
	)

	KeywordUtil.logInfo(
		"Verifying appointment time '${apptTime}' | Expected Available: ${shouldBeAvailable}"
	)

	WebUI.switchToFrame(iframe, timeout)

	try {

		List<WebElement> slotElements =
			WebUiCommonHelper.findWebElements(timeSlots, timeout)

		println("No. of Slots = ${slotElements.size()}")

		List<String> availableSlots = slotElements.collect {
			it.getText().replaceAll("\\s+", " ").trim()
		}

		println("Expected Time   : ${apptTime}")
		println("Available Slots : ${availableSlots}")

		boolean isAvailable = availableSlots.contains(apptTime)

		println("Actual Available: ${isAvailable}")

		if (shouldBeAvailable) {

			assert isAvailable :
				"Expected time slot '${apptTime}' to be AVAILABLE, but it is NOT available."

			KeywordUtil.logInfo(
				"PASS: Time slot '${apptTime}' is available as expected."
			)

		} else {

			assert !isAvailable :
				"Expected time slot '${apptTime}' to be NOT AVAILABLE, but it is available."

			KeywordUtil.logInfo(
				"PASS: Time slot '${apptTime}' is not available as expected."
			)
		}

	} finally {

		WebUI.switchToDefaultContent()

		KeywordUtil.logInfo(
			"Switched back to default content."
		)
	}
}

}