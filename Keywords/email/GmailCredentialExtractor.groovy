package email

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
import internal.GlobalVariable as GlobalVariable
import javax.mail.*
import javax.mail.internet.MimeMultipart
import java.util.Properties

class GmailCredentialExtractor {

	@Keyword
	static void extractUsernameAndPassword(String gmailUser, String gmailAppPassword,
			String fromEmail, String subjectKeyword) {

		Properties props = new Properties()
		props.put("mail.store.protocol", "imaps")
		props.put("mail.imaps.host", "imap.gmail.com")
		props.put("mail.imaps.port", "993")
		props.put("mail.imaps.ssl.enable", "true")

		Session session = Session.getInstance(props)
		Store store = session.getStore("imaps")
		store.connect("imap.gmail.com", gmailUser, gmailAppPassword)

		Folder inbox = store.getFolder("INBOX")
		inbox.open(Folder.READ_ONLY)

		Message latestMail = null

		// Find latest matching email
		for (int i = inbox.getMessageCount(); i > 0; i--) {
			Message msg = inbox.getMessage(i)

			if (msg.getFrom()[0].toString().contains(fromEmail) &&
					msg.getSubject().contains(subjectKeyword)) {
				latestMail = msg
				break
			}
		}

		if (latestMail == null) {
			println("❌ No matching email found.")
			inbox.close(false)
			store.close()
			return
		}

		// Extract body
		String emailContent = getCleanContent(latestMail)

		println("📩 CLEAN EMAIL BODY:\n" + emailContent)

		// Extract Username
		def usernameMatch = (emailContent =~ /Username:\s*([A-Za-z0-9._-]+)/)
		String extractedUsername = usernameMatch ? usernameMatch[0][1].trim() : ""

		// Extract Password
		def passwordMatch = (emailContent =~ /Password:\s*([^\s]+)/)
		String extractedPassword = passwordMatch ? passwordMatch[0][1].trim() : ""

		println("🔐 Username: $extractedUsername")
		println("🔐 Password: $extractedPassword")

		// Store in Katalon global variables
		GlobalVariable.GV_Username = extractedUsername
		GlobalVariable.GV_Password = extractedPassword

		inbox.close(false)
		store.close()
	}


	private static String getCleanContent(Message message) {
		String emailContent = ""

		def content = message.getContent()

		if (content instanceof String) {
			emailContent = content
		} else if (content instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) content
			emailContent = multipart.getBodyPart(0).getContent().toString()
		}

		// Clean HTML
		emailContent = emailContent.replaceAll("(?i)<br>", "\n")
		emailContent = emailContent.replaceAll("<[^>]+>", "")
		emailContent = emailContent.replace("&nbsp;", " ")
		emailContent = emailContent.trim()

		return emailContent
	}
}