package appointment

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil

class AppointmentKeywords {

    /**
     * Verifies the Medical Disclaimer text is displayed and matches one of the
     * accepted variants (handles "follow up" vs "follow-up" wording differences).
     *
     * @param disclaimerTestObject TestObject for the Medical Disclaimer element
     */
    @Keyword
    def verifyMedicalDisclaimer(TestObject disclaimerTestObject) {
        KeywordUtil.logInfo('Verifying Medical Disclaimer is displayed')

        List<String> expectedVariants = [
            "Online appointment booking is only for routine exam and follow up appointments and should not be used if you have any urgent or concerning medical issues. If experiencing medical issues please call our office during office hours. If outside of office hours please call 911 or visit an urgent care or emergency room for immediate assistance.",
            "Online appointment booking is only for routine exam and follow-up appointments and should not be used if you have any urgent or concerning medical issues. If experiencing medical issues please call our office during office hours. If outside of office hours please call 911 or visit an urgent care or emergency room for immediate assistance."
        ]

        String actualDisclaimer = WebUI.getText(disclaimerTestObject).replaceAll("\\s+", " ").trim()

        boolean isMatch = expectedVariants.contains(actualDisclaimer)

        if (isMatch) {
            KeywordUtil.logInfo('Medical disclaimer verified successfully (matched one of the accepted variants)')
        } else {
            KeywordUtil.markFailed('Medical disclaimer text did not match any expected variant. Actual: ' + actualDisclaimer)
        }

        return isMatch
    }

    /**
     * Verifies the "Do you want to proceed with booking an appointment? Yes No"
     * confirmation prompt is displayed with the expected text.
     *
     * @param confirmTestObject TestObject for the confirmation prompt element
     */
    @Keyword
    def verifyBookingConfirmationPrompt(TestObject confirmTestObject) {
        KeywordUtil.logInfo('Verifying confirming message is displayed with Yes and No')

        String expectedConfirming = "Do you want to proceed with booking an appointment? Yes No"

        String actualConfirming = WebUI.getText(confirmTestObject).replaceAll("\\s+", " ").trim()

        WebUI.verifyMatch(actualConfirming, expectedConfirming, false)
    }

    /**
     * Convenience wrapper that runs both verifications using findTestObject paths,
     * matching the original inline script's object repository references.
     */
    @Keyword
    def verifyDisclaimerAndConfirmationPrompt() {
        TestObject disclaimerObj = com.kms.katalon.core.testobject.ObjectRepository.findTestObject(
            'Appointment Booking/Chat Bot Appt Book/EVAA.AI React/Medical Disclaimer')
        TestObject confirmObj = com.kms.katalon.core.testobject.ObjectRepository.findTestObject(
            'Appointment Booking/Chat Bot Appt Book/EVAA.AI React/div_Do you want to proceed with booking an appoi')

        verifyMedicalDisclaimer(disclaimerObj)
        verifyBookingConfirmationPrompt(confirmObj)
    }
}