package email

import com.kms.katalon.core.annotation.Keyword
import internal.GlobalVariable
import javax.mail.*
import javax.mail.internet.MimeMultipart
import java.util.Properties

class GmailCredentialExtractorKey_Value {

	 @Keyword
    static void extractCredentials(
            String gmailUser,
            String gmailAppPassword,
            String fromEmail,
            String subjectKeyword,
            String userKey   // <-- User1, User2, etc.
    ) {

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

        for (int i = inbox.getMessageCount(); i > 0; i--) {
            Message msg = inbox.getMessage(i)

            if (msg.getFrom()[0].toString().contains(fromEmail)
                    && msg.getSubject().contains(subjectKeyword)) {
                latestMail = msg
                break
            }
        }

        if (!latestMail) {
            inbox.close(false)
            store.close()
            throw new Exception("❌ No matching email found")
        }

        String emailContent = getCleanContent(latestMail)

        def usernameMatch = (emailContent =~ /Username:\s*([A-Za-z0-9._-]+)/)
        def passwordMatch = (emailContent =~ /Password:\s*([^\s]+)/)

        String username = usernameMatch ? usernameMatch[0][1].trim() : ""
        String password = passwordMatch ? passwordMatch[0][1].trim() : ""

        // 🔐 Store in Map
        GlobalVariable.GV_Credentials[userKey] = [
            username: username,
            password: password
        ]

        println "✔ Stored credentials for $userKey"
        println "   Username: $username"
        println "   Password: $password"

        inbox.close(false)
        store.close()
    }

    private static String getCleanContent(Message message) {
        def content = message.getContent()
        String text = ""

        if (content instanceof String) {
            text = content
        } else if (content instanceof MimeMultipart) {
            text = content.getBodyPart(0).getContent().toString()
        }

        return text
                .replaceAll("(?i)<br>", "\n")
                .replaceAll("<[^>]+>", "")
                .replace("&nbsp;", " ")
                .trim()
    }
}
