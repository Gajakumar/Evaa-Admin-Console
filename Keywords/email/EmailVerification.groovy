package email

import javax.mail.*
import javax.mail.search.*
import java.util.Properties
import java.text.SimpleDateFormat
import java.util.Date
import com.kms.katalon.core.annotation.Keyword

class EmailVerification {

    // ✅ HELPER METHOD (CLASS LEVEL)
    private String extractTextFromMessage(Message message) {

        if (message.isMimeType("text/plain")) {
            return message.getContent().toString()
        }

        if (message.isMimeType("text/html")) {
            return message.getContent().toString()
                    .replaceAll("<[^>]*>", " ")
                    .replaceAll("&nbsp;", " ")
                    .replaceAll("\\s+", " ")
                    .trim()
        }

        if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent()
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i)

                if (part.isMimeType("text/html")) {
                    return part.getContent().toString()
                            .replaceAll("<[^>]*>", " ")
                            .replaceAll("&nbsp;", " ")
                            .replaceAll("\\s+", " ")
                            .trim()
                }

                if (part.isMimeType("text/plain")) {
                    return part.getContent().toString()
                }
            }
        }
        return ""
    }

    // ✅ KEYWORD METHOD
    @Keyword
    def verifyAgreementEmailWithRetry(String productName,
                                      String annualPrice,
                                      String monthlyPrice,
                                      String signedByExpected,
                                      int maxWaitSeconds = 180,
                                      int pollIntervalSeconds = 10) {

        String todayDate =
                new SimpleDateFormat("MMMM d, yyyy").format(new Date())

        String host = "imap.gmail.com"
        String username = "gajakumara@first-insight.com"
        String appPassword = "qnkj qbyt goya wbhd"

        Properties props = new Properties()
        props.put("mail.store.protocol", "imaps")

        Session session = Session.getDefaultInstance(props, null)
        Store store = session.getStore("imaps")
        store.connect(host, username, appPassword)

        Folder inbox = store.getFolder("INBOX")
        inbox.open(Folder.READ_ONLY)

        Message email = null
        int elapsed = 0

        while (elapsed < maxWaitSeconds) {
            Message[] messages = inbox.search(
                new SubjectTerm("EVAA AI Service Agreement - Signed")
            )

            if (messages.length > 0) {
                email = messages[messages.length - 1]
                break
            }

            Thread.sleep(pollIntervalSeconds * 1000)
            elapsed += pollIntervalSeconds

            inbox.close(false)
            inbox.open(Folder.READ_ONLY)
        }

        assert email != null :
                "❌ Agreement email not received in time"

        // ✅ USE HELPER METHOD
        String body = extractTextFromMessage(email)

        println "📩 EMAIL BODY:\n" + body

        assert body.contains(productName)
        assert body.contains(annualPrice)
        assert body.contains("Monthly Equivalent")
        assert body.contains(monthlyPrice)
        assert body.contains("Signed by: ${signedByExpected}")
        assert body.contains("Date: ${todayDate}")

        inbox.close(false)
        store.close()
    }
}
