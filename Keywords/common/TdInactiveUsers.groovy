package common

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.testobject.ConditionType

public class TdInactiveUsers {
	Map<String, List<Map>> store = [:]
	
	@Keyword
	def readAndStoreMaxInactiveUsers(
			String key,
			TestObject tableObj,
			int firstNameCol,
			int lastNameCol,
			int loginCol,
			int activeCol,
			TestObject nextBtn) {
	
		List<Map> users = []
		Set<String> capturedLogins = new HashSet<>()
	
		while (true) {
	
			int lastCount = -1
			int stable = 0
	
			while (stable < 3) {
	
				List<WebElement> rows = DriverFactory.getWebDriver()
						.findElements(By.xpath("//div[@id='userGridContainer']//tbody/tr"))
	
				for (WebElement row : rows) {
	
					List<WebElement> cols = row.findElements(By.tagName("td"))
	
					if (cols.size() > activeCol) {
	
						String active = cols[activeCol].getText().trim()
	
						// ⭐ ONLY STORE INACTIVE USERS
						if (!active.equalsIgnoreCase("No")) {
							continue
						}
	
						String login = cols[loginCol].getText().trim()
	
						if (login && !capturedLogins.contains(login)) {
	
							capturedLogins.add(login)
	
							users.add([
									login     : login,
									firstName : cols[firstNameCol].getText().trim(),
									lastName  : cols[lastNameCol].getText().trim()
							])
						}
					}
				}
	
				WebUI.executeJavaScript(
						"document.querySelector('#userGridContainer .dx-scrollable-container').scrollBy(0,400)",
						null
				)
	
				WebUI.delay(1)
	
				if (users.size() == lastCount) {
					stable++
				} else {
					stable = 0
				}
	
				lastCount = users.size()
			}
	
			println("Inactive users collected so far: " + users.size())
	
			WebElement next = WebUiCommonHelper.findWebElement(nextBtn, 5)
	
			if (!next.isEnabled() || next.getAttribute("class").contains("disable")) {
				println("⛔ Last page reached")
				break
			}
	
			println("➡️ Moving to next page")
	
			WebUI.click(nextBtn)
			WebUI.delay(2)
		}
	
		store.put(key, users)
	
		KeywordUtil.logInfo("📥 STORED INACTIVE USERS: " + users.size())
	}
	
	// =====================================================
	// READ EVAA USERS
	// =====================================================

	@Keyword
	Map<String, String> getEvaaInactiveUsers(TestObject tableObject) {

		Map<String, String> usersEvaa = [:]

		WebElement table = WebUiCommonHelper.findWebElement(tableObject, 20)
		List<WebElement> rows = table.findElements(By.xpath(".//tr"))

		for (WebElement row : rows) {

			List<WebElement> cells = row.findElements(By.tagName("td"))

			if (cells.size() < 2) {
				continue
			}

			String fullName = cells.get(0).getText().trim()
			String login = cells.get(1).getText().trim()

			if (fullName && login) {
				usersEvaa.put(login.toLowerCase(), fullName)
			}
		}

		KeywordUtil.logInfo("Total EVAA Inactive Users Found: " + usersEvaa.size())

		return usersEvaa
	}


	// =====================================================
	// COMPARE USERS
	// =====================================================

	@Keyword
	def compareInctiveUsers(String key, Map evaaUsers) {

		if (!store.containsKey(key)) {

			KeywordUtil.markFailed("❌ No stored users found for: " + key)
			return
		}

		List<Map> rawUsers = (List<Map>) store.get(key)

		Map<String, String> maxUsers = [:]

		rawUsers.each { u ->

			String username = u.login?.toLowerCase()?.trim()
			String fullname = ((u.firstName ?: "") + " " + (u.lastName ?: "")).trim()

			if (!username) {
				username = "INVALID_USER_" + maxUsers.size()
			}

			maxUsers.put(username, fullname)
		}

		KeywordUtil.logInfo("Maximeyes Users : " + maxUsers.size())
		KeywordUtil.logInfo("EVAA Users      : " + evaaUsers.size())

		println("")
		println("USER LOGIN           MAXIMEYES   EVAA")
		println("--------------------------------------")

		Set<String> allUsers = new HashSet<>()
		allUsers.addAll(maxUsers.keySet())
		allUsers.addAll(evaaUsers.keySet())

		int matched = 0
		int missing = 0
		int extra = 0

		allUsers.sort().each { login ->

			boolean inMax = maxUsers.containsKey(login)
			boolean inEva = evaaUsers.containsKey(login)

			String maxFlag = inMax ? "✓" : "✗"
			String evaFlag = inEva ? "✓" : "✗"

			println String.format("%-20s %-10s %-5s", login, maxFlag, evaFlag)

			if (inMax && inEva) matched++
			if (inMax && !inEva) missing++
			if (!inMax && inEva) extra++
		}

		KeywordUtil.logInfo("================================")
		KeywordUtil.logInfo("📊 USER COMPARISON REPORT")
		KeywordUtil.logInfo("--------------------------------")
		KeywordUtil.logInfo("✅ Matched Users : " + matched)
		KeywordUtil.logInfo("❌ Missing Users : " + missing)
		KeywordUtil.logInfo("⚠️ Extra Users   : " + extra)
		KeywordUtil.logInfo("================================")
	}
}


