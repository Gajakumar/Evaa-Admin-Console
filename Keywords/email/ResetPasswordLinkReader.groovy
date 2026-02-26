package email

import com.kms.katalon.core.annotation.Keyword
import internal.GlobalVariable
import javax.mail.*
import javax.mail.internet.MimeMultipart
import java.util.Properties

class ResetPasswordLinkReader {

	@Keyword
	def fetchResetLink(String host, String username, String appPassword,
			String fromEmail, String subjectKeyword) {

		Properties props = new Properties()
		props.put("mail.store.protocol", "imaps")

		Session session = Session.getInstance(props)
		Store store = session.getStore("imaps")
		store.connect(host, username, appPassword)

		Folder inbox = store.getFolder("INBOX")
		inbox.open(Folder.READ_ONLY)

		Message mail = null

		for (int i = inbox.getMessageCount(); i > 0; i--) {
			Message msg = inbox.getMessage(i)
			if (msg.getFrom()[0].toString().contains(fromEmail) &&
					msg.getSubject().toLowerCase().contains(subjectKeyword.toLowerCase())) {
				mail = msg
				break
			}
		}

		if (mail == null) {
			throw new Exception("Reset email not found")
		}

		String body = ""
		def content = mail.getContent()

		if (content instanceof String) {
			body = content
		} else if (content instanceof MimeMultipart) {
			body = content.getBodyPart(0).getContent().toString()
		}

		def matcher = body =~ /(https?:\/\/[^\s]+)/

		if (!matcher.find()) {
			throw new Exception("Reset URL not found")
		}

		String resetURL = matcher.group(1)

		GlobalVariable.ResetPasswordURL = resetURL

		println("🔗 Stored Reset URL: " + resetURL)

		inbox.close(false)
		store.close()
	}
}
