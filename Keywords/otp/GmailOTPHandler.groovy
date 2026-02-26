
package otp
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import javax.mail.*
import javax.mail.internet.MimeMultipart
import java.util.Properties

class GmailOTPHandler {

	@Keyword
	static String readOTP(String host, String username, String password, String fromEmail, String subjectKeyword) {

		String otp = ""

		try {
			WebUI.delay(8)

			Properties props = new Properties()
			props.put("mail.store.protocol", "imaps")

			Session session = Session.getDefaultInstance(props, null)
			Store store = session.getStore("imaps")
			store.connect(host, username, password)

			Folder inbox = store.getFolder("INBOX")
			inbox.open(Folder.READ_ONLY)

			Message[] messages = inbox.getMessages()

			// Latest email first
			for (int i = messages.length - 1; i >= 0; i--) {

				Message msg = messages[i]

				if (msg.getFrom()[0].toString().contains(fromEmail) &&
						msg.getSubject().contains(subjectKeyword)) {

					String body = getBody(msg)

					// extract 4-digit OTP
					def matcher = body =~ /\b\d{4}\b/
					if (matcher.find()) {
						otp = matcher.group()
						break
					}
				}
			}

			inbox.close(false)
			store.close()
		} catch (Exception e) {
			println("ERROR while reading OTP: " + e)
		}

		return otp
	}


	private static String getBody(Message message) {
		if (message.getContent() instanceof String) {
			return message.getContent().toString()
		} else if (message.getContent() instanceof MimeMultipart) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent()
			return mimeMultipart.getBodyPart(0).getContent().toString()
		}
		return ""
	}
}