package customkeywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.Keys

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * Keywords for interacting with the patient-facing chat bot appointment
 * booking flow on the eyeclinic.ai site.
 */
class ChatBotBookingKeywords {

	@Keyword
	def openApplication(String url) {
		WebUI.navigateToUrl(url)
		KeywordUtil.logInfo('Navigated to application: ' + url)
	}

	/** Launches the chat bot via the "Push to talk" icon and waits for the bot UI to be ready. */
	@Keyword
	def launchChatBot(int pageTimeout) {
		TestObject pushToTalk = findTestObject('Appointment Booking/Chat Bot Appt Book/img_Push to talk')
		WebUI.waitForElementVisible(pushToTalk, pageTimeout)
		WebUI.click(pushToTalk)

		TestObject bookAppt = findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment')
		WebUI.waitForElementVisible(bookAppt, pageTimeout)
		KeywordUtil.logInfo('Chat bot launched.')
	}

	/** Types a message into the chat input and submits it with Enter. */
	@Keyword
	def sendChatMessage(String message) {
		TestObject chatInput = findTestObject('Appointment Booking/Chat Bot Appt Book/Chat Bot Enter Text Area')
		WebUI.sendKeys(chatInput, message)
		WebUI.sendKeys(chatInput, Keys.chord(Keys.ENTER))
		KeywordUtil.logInfo('Sent chat message: ' + message)
	}

	/** Reads a chat bot message's text and normalizes the phone number for comparison. */
	@Keyword
	String getMessageText(TestObject messageObject, int timeout = 5) {
		String message = WebUI.getText(messageObject, timeout)
		message = message.replaceFirst(/(<number>|XXX-XXX-XXXX|\d{3}-\d{3}-\d{4})/, '<PHONE>')
		KeywordUtil.logInfo('Captured chat bot message text: ' + message)
		return message
	}

	/** Clicks "Book Appointment" and confirms the "Yes" prompt to start the booking flow. */
	@Keyword
	def confirmBookingIntent() {
		WebUI.click(findTestObject('Object Repository/Appointment Booking/Chat Bot Appt Book/button_Book Appointment'))
		WebUI.click(findTestObject('Appointment Booking/Chat Bot Appt Book/button_Yes'))
		KeywordUtil.logInfo('Confirmed booking intent.')
	}

	/** Waits for the patient details form (First Name field) to appear. */
	@Keyword
	def waitForPatientDetailsForm(int pageTimeout) {
		TestObject firstNameField = findTestObject('Appointment Booking/Chat Bot Appt Book/input_First Name')
		WebUI.waitForElementVisible(firstNameField, pageTimeout)
		KeywordUtil.logInfo('Patient details form is visible.')
	}
}