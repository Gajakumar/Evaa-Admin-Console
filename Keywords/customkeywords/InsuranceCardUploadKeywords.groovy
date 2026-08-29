package customkeywords

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import org.openqa.selenium.Keys

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

/**
 * Keywords for the "Insurance Card Upload & Details" step of the
 * appointment booking flow: deleting an existing insurance, uploading
 * front/back card images, verifying OCR-filled details, and entering
 * coverage information.
 */
class InsuranceCardUploadKeywords {

	/** Deletes an already-added insurance record, if one is present. */
	@Keyword
	def deleteInsuranceIfPresent(int timeout = 10) {
		TestObject deleteInsuranceBtn = findTestObject('Appointment Booking/Delete Ins/Delete Added Ins/button_Delete insurance')

		if (WebUI.verifyElementVisible(deleteInsuranceBtn, FailureHandling.OPTIONAL)) {
			WebUI.waitForElementClickable(deleteInsuranceBtn, timeout)
			WebUI.click(deleteInsuranceBtn)
			KeywordUtil.logInfo('Existing insurance deleted.')
		} else {
			KeywordUtil.logInfo('No existing insurance found. Skipping delete.')
		}
	}

	/** Verifies the static labels/hints on the insurance card upload screen. */
	@Keyword
	def verifyInsuranceCardUploadScreen(
			String insuranceCardPhotoText,
			String insuranceNameLabelText,
			String frontSideLabelText,
			String uploadFrontHintText,
			String backSideLabelText,
			String uploadBackHintText,
			String insuranceNoteText,
			int shortTimeout) {

		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Insurance Card Photo'), insuranceCardPhotoText, shortTimeout)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/label_Insurance Name'), insuranceNameLabelText, 0)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Front Side'), frontSideLabelText, 0)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Upload or take a photo of the front of your in'), uploadFrontHintText, 0)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Back Side'), backSideLabelText, 0)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Upload or take a photo of the back of your ins'), uploadBackHintText, 0)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_Note_ Please add clear image of card. Image si'), insuranceNoteText, 0)
		KeywordUtil.logInfo('Insurance card upload screen elements verified.')
	}

	@Keyword
	def enterInsuranceName(String insuranceName) {
		WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter Insurance Name'), insuranceName)
		KeywordUtil.logInfo("Insurance Name entered: '${insuranceName}'")
	}

	/** Uploads a single file to a TestCloud-backed upload input. */
	private def uploadFileToTestCloud(TestObject uploadObj, File baseDir, String fileName) {
		assert uploadObj != null : '❌ Upload input TestObject is NULL'

		File fileToUpload = new File(baseDir, fileName)
		assert fileToUpload.exists() && fileToUpload.isFile() :
				"❌ Upload file not found: ${fileToUpload.absolutePath}"

		KeywordUtil.logInfo("☁ TestCloud uploading: ${fileToUpload.absolutePath}")
		CustomKeywords.'com.katalon.testcloud.FileExecutor.uploadFileToWeb'(
				uploadObj,
				fileToUpload.absolutePath)
	}

	/**
	 * Uploads the front and back insurance card images (found under
	 * Include/TestFiles in the project) and clicks NEXT.
	 */
	@Keyword
	def uploadInsuranceCardImages(String frontCardFileName, String backCardFileName) {
		String projectDir = RunConfiguration.getProjectDir()
		File baseDir = new File(projectDir, 'Include/TestFiles')

		[
			1: frontCardFileName,
			2: backCardFileName
		].each { int inputIndex, String fileName ->
			TestObject uploadInput = findTestObject('Appointment Booking/Chat Bot Appt Book/Upload Input', ['index': inputIndex])
			uploadFileToTestCloud(uploadInput, baseDir, fileName)
			KeywordUtil.logInfo("Uploaded file '${fileName}' successfully")
		}

		WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
		KeywordUtil.logInfo('Insurance card images submitted.')
	}

	/** Waits for and verifies the "Insurance card scanned" confirmation, then clicks Continue. */
	@Keyword
	def verifyAndContinueFromCardScanned(String cardScannedTitleText, String cardScannedBodyText, int longTimeout, int mediumTimeout) {
		TestObject continueBtn = findTestObject('Book Appt With Ins/EVAA.AI React/button_Continue')
		WebUI.waitForElementVisible(continueBtn, longTimeout)

		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), cardScannedTitleText, mediumTimeout)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/p_We filled in your insurance details from the c'), cardScannedBodyText, 0)
		KeywordUtil.logInfo('Insurance card scan confirmation verified.')

		WebUI.click(continueBtn)
		KeywordUtil.logInfo('Continued to review insurance details.')
	}

	/** Verifies the insurance fields that were auto-filled from OCR against expected values. */
	@Keyword
	def verifyOcrFilledInsuranceDetails(String insuranceName, String insuredId, String firstName, String lastName, String dob, int mediumTimeout) {
		WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insurance Name'), 'defaultValue', insuranceName, mediumTimeout)
		WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Insured ID'), 'defaultValue', insuredId, mediumTimeout)
		WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_First Name'), 'defaultValue', firstName, mediumTimeout)
		WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_Last Name'), 'defaultValue', lastName, mediumTimeout)
		WebUI.verifyElementAttributeValue(findTestObject('Book Appt With Ins/EVAA.AI React/input_mm_dd_yyyy'), 'defaultValue', dob, mediumTimeout)
		KeywordUtil.logInfo('OCR-filled insurance details verified against expected patient data.')
	}

	@Keyword
	def selectGender(String gender, int mediumTimeout) {
		TestObject genderDropdown = findTestObject('Book Appt With Ins/EVAA.AI React/select_Gender')
		WebUI.selectOptionByLabel(genderDropdown, gender, false)
		WebUI.verifyOptionSelectedByLabel(genderDropdown, gender, false, mediumTimeout)
		KeywordUtil.logInfo("Gender selected and verified: '${gender}'")
	}

	@Keyword
	def clickNext() {
		WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
	}

	/** Selects patient relationship and enters insurance group/employer names. */
	@Keyword
	def enterInsuranceCoverageDetails(String patientRelationship, String insuranceGroupName, String insuranceEmployerName) {
		WebUI.selectOptionByValue(findTestObject('Book Appt With Ins/EVAA.AI React/select_Patient Relationship to insured'), patientRelationship, false)
		KeywordUtil.logInfo("Patient Relationship selected: '${patientRelationship}'")

		WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter'), insuranceGroupName)
		WebUI.setText(findTestObject('Book Appt With Ins/EVAA.AI React/input_Enter_1'), insuranceEmployerName)
		KeywordUtil.logInfo('Insurance group/employer details entered.')
	}

	/** Enters coverage start & end dates and dismisses the date picker with Escape. */
	@Keyword
	def enterCoverageDates(String startDateStr, String endDateStr) {
		TestObject coverageStartDate = findTestObject('Book Appt With Ins/EVAA.AI React/input_Coverage Start Date')
		WebUI.setText(coverageStartDate, startDateStr)
		WebUI.sendKeys(coverageStartDate, Keys.chord(Keys.ESCAPE))
		KeywordUtil.logInfo("Coverage Start Date entered: ${startDateStr}")

		TestObject coverageEndDate = findTestObject('Book Appt With Ins/EVAA.AI React/input_Coverage End Date')
		WebUI.setText(coverageEndDate, endDateStr)
		WebUI.sendKeys(coverageEndDate, Keys.chord(Keys.ESCAPE))
		KeywordUtil.logInfo("Coverage End Date entered: ${endDateStr}")

		WebUI.click(findTestObject('Book Appt With Ins/EVAA.AI React/button_NEXT'))
		KeywordUtil.logInfo('Insurance coverage details submitted.')
	}

	/** Verifies the final "insurance details saved" confirmation and clicks Continue. */
	@Keyword
	def verifyAndContinueFromInsuranceSaved(String insuranceSavedTitleText, String viewAddInsurancesText, int longTimeout, int mediumTimeout) {
		TestObject continueBtn = findTestObject('Book Appt With Ins/EVAA.AI React/button_Continue')
		WebUI.waitForElementVisible(continueBtn, longTimeout)

		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/h3_Insurance card scanned'), insuranceSavedTitleText, mediumTimeout)
		WebUI.assertElementText(findTestObject('Book Appt With Ins/EVAA.AI React/button_View _ Add Insurances'), viewAddInsurancesText, mediumTimeout)
		KeywordUtil.logInfo('Insurance details save confirmed.')

		WebUI.click(continueBtn)
		KeywordUtil.logInfo('Continued to appointment confirmation screen.')
	}
}