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

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.model.FailureHandling

// ======================
// UTILITY FUNCTIONS
// ======================

def getValue = { String xpath ->
	TestObject obj = new TestObject()
	obj.addProperty("xpath", ConditionType.EQUALS, xpath)

	if(WebUI.verifyElementPresent(obj,5,FailureHandling.OPTIONAL)) {
		try{
			// get the tag name
			String tag = WebUI.getAttribute(obj,"tagName")

			if(tag!=null && tag.trim().equalsIgnoreCase("select")) {
				// dropdown: get text of selected option
				return WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath + "/option[@selected]")).trim()
			}

			// everything else: get value attribute first
			String val = WebUI.getAttribute(obj,"value")
			if(val!=null && val.trim()!="") return val.trim()

			// fallback to visible text
			String text = WebUI.getText(obj)
			if(text!=null && text.trim()!="") return text.trim()
		}
		catch(Exception e){}
	}

	return ""
}

// Convert 24H → AM/PM
def convertTime = { time ->
	if(!time) return ""
	def p = time.split(":")
	int h = p[0] as int
	String m = p[1]
	String period = h >= 12 ? "PM" : "AM"
	if(h > 12) h -= 12
	if(h == 0) h = 12
	return h + ":" + m + " " + period
}

// ======================
// DATA STRUCTURES
// ======================

Map maximEyesData = [:]
Map evaaData = [:]

// Table results
List<Map> results = []
def addResult = { field, maxVal, evaaVal ->
	maxVal = (maxVal ?: "").trim()
	evaaVal = (evaaVal ?: "").trim()
	String res = maxVal.equalsIgnoreCase(evaaVal) ? "PASS" : "FAIL"
	results.add([field: field, max: maxVal, evaa: evaaVal, res: res])
}

// ======================
// MAXIMEYES LOGIN
// ======================

WebUI.callTestCase(findTestCase('Test Cases/Common/Maximeyes Login'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Office Admin Button'))
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Business Administration'))
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Locations'))
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Location_Row',[('locationName') : 'Purchasing']))
WebUI.delay(3)

// ======================
// CAPTURE MAXIMEYES DATA
// ======================

maximEyesData["Address"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_Line1')]")
maximEyesData["Address2"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_Line2')]")
maximEyesData["City"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_CityName')]")
maximEyesData["Zip"] = getValue("//*[@id='Main']//input[contains(@id,'ZipCode')]")
maximEyesData["ZipPlus4"] = getValue("//*[@id='Main']//input[contains(@id,'ZipCodePlus4')]")
maximEyesData["Phone"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_Phone')]")
maximEyesData["Fax"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_Fax')]")
maximEyesData["Email"] = getValue("//*[@id='Main']//input[contains(@id,'PL_Main_Email')]")
maximEyesData["Business Name"] = getValue("//*[@id='Info_BusinessNameID']/option[@selected]")
maximEyesData["Location Name"] = getValue("//*[@id='Info_LocationName']")
maximEyesData["Organization NP"] = getValue("//*[@id='Info_NPI']")
maximEyesData["Tax ID"] = getValue("//*[@id='Info_TaxID']")
maximEyesData["State"] = getValue("//*[@name='Addresses[0].StateID']")

// Office hours
Map officeIds = [
	Sunday:"practiceLocationHours_officeHoursSunday",
	Monday:"practiceLocationHours_officeHoursMonday",
	Tuesday:"practiceLocationHours_officeHoursTuesday",
	Wednesday:"practiceLocationHours_officeHoursWednesday",
	Thursday:"practiceLocationHours_officeHoursThursday",
	Friday:"practiceLocationHours_officeHoursFriday",
	Saturday:"practiceLocationHours_officeHoursSaturday"
]

officeIds.each{ day,id ->
	maximEyesData[day] = getValue("//input[@id='"+id+"']")
}

// ======================
// EVAA LOGIN
// ======================

WebUI.callTestCase(findTestCase('Test Cases/Common/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Login'))
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/button_MaximEyes'))
WebUI.setText(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Username'), 'GajakumarA')
WebUI.setEncryptedText(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Password'), 'cvW8qx4B2o1WegCEDy41Xg==')
WebUI.setText(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_https_MaximEyeURL'), 'qa10evaa')
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/button_Login'))
WebUI.executeJavaScript("document.body.style.zoom='80%'",null)

// ======================
// EVAA LOCATIONS
// ======================

TestObject settingBtn = findTestObject("Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76")
WebUI.waitForElementClickable(settingBtn,5)
WebUI.click(settingBtn)
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Locations'))
WebUI.click(findTestObject('Object Repository/Evaa Locations/View Button by Location',[('locationName') : 'Purchasing']))
WebUI.delay(3)

// ======================
// CAPTURE EVAA DATA
// ======================

evaaData["Address"] = getValue("//input[@name='address']")
evaaData["Address2"] = getValue("//input[@name='address2']")
evaaData["City"] = getValue("//input[@name='city']")
evaaData["Zip"] = getValue("//input[@name='zip']")
evaaData["ZipPlus4"] = getValue("//input[@name='zipPlus4']")
evaaData["Phone"] = getValue("//input[@name='phone']")
evaaData["Fax"] = getValue("//input[@name='fax']")
evaaData["Email"] = getValue("//input[@name='email']")
evaaData["Business Name"] = getValue("(//button[contains(@class,'form-control')]//span)[1]")
evaaData["Location Name"] = getValue("//input[@placeholder='Enter location name']")
evaaData["Organization NP"] = getValue("//input[@placeholder='Enter Group NPI']")
evaaData["Tax ID"] = getValue("//input[@name='taxId']")
evaaData["State"] = getValue("(//button[contains(@class,'form-control')]//span)[3]")

// EVAA office hours
def getEvaaOfficeHours = {
	Map hours = [:]
	List days = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]
	days.each{ day ->
		TestObject closedObj = new TestObject()
		closedObj.addProperty("xpath",ConditionType.EQUALS,"//input[@id='closed-${day}']")
		boolean isClosed = WebUI.verifyElementChecked(closedObj,1,FailureHandling.OPTIONAL)
		if(isClosed) {
			hours[day] = "Closed"
		} else {
			TestObject startObj = new TestObject()
			startObj.addProperty("xpath",ConditionType.EQUALS,"//div[contains(text(),'${day}')]/following::input[@type='time'][1]")
			TestObject endObj = new TestObject()
			endObj.addProperty("xpath",ConditionType.EQUALS,"//div[contains(text(),'${day}')]/following::input[@type='time'][2]")
			String start = convertTime(WebUI.getAttribute(startObj,"value"))
			String end = convertTime(WebUI.getAttribute(endObj,"value"))
			hours[day] = start + " - " + end
		}
	}
	return hours
}
evaaData.putAll(getEvaaOfficeHours())

// ======================
// START COMPARISON
// ======================

maximEyesData.each{ key,value ->
	addResult(key, value, evaaData[key])
}

// ======================
// PRINT TABLE
// ======================

println "--------------------------------------------------------------------------------"
println String.format("| %-15s | %-20s | %-20s | %-6s |", "Field", "MaximEyes", "EVAA", "Result")
println "--------------------------------------------------------------------------------"
results.each {
	println String.format("| %-15s | %-20s | %-20s | %-6s |",
		it.field, it.max, it.evaa, it.res)
}
println "--------------------------------------------------------------------------------"

// ======================
// SOFT ASSERT / FAIL COUNT
// ======================

def failCount = results.count { it.res == "FAIL" }

if(failCount > 0) {
	KeywordUtil.markFailed("Location Data Mismatch Found : ${failCount}")
} else {
	println("ALL DATA MATCHED SUCCESSFULLY")
}

println("COMPARISON COMPLETE")