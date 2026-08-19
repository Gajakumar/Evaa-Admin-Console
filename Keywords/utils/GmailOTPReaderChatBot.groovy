package utils

import javax.mail.*
import javax.mail.search.*
import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

class GmailOTPReaderChatBot {

    static String getOTP(String email, String appPassword) {

        Properties props = new Properties()
        props.put("mail.store.protocol", "imaps")

        Session session = Session.getDefaultInstance(props, null)
        Store store = session.getStore("imaps")
        store.connect("imap.gmail.com", email, appPassword)

        Folder inbox = store.getFolder("INBOX")
        inbox.open(Folder.READ_WRITE)

        // Search unread mails from EVAA
        SearchTerm searchTerm = new AndTerm(
                new FlagTerm(new Flags(Flags.Flag.SEEN), false),
                new FromStringTerm("do-not-reply@evaa.ai")
        )

        Message[] messages = inbox.search(searchTerm)

        if (messages.length == 0) {
            inbox.close(false)
            store.close()
            return null
        }

        // Read latest mail
        Message message = messages[messages.length - 1]

        String subject = message.getSubject()

        if (!subject.contains("OTP")) {
            inbox.close(false)
            store.close()
            return null
        }

        String body = getTextFromMessage(message)

        Pattern pattern = Pattern.compile("\\b\\d{4}\\b")
        Matcher matcher = pattern.matcher(body)

        String otp = null

        if (matcher.find()) {
            otp = matcher.group()
        }

        // Mark email as read
        message.setFlag(Flags.Flag.SEEN, true)

        inbox.close(false)
        store.close()

        return otp
    }

    private static String getTextFromMessage(Part part) throws Exception {

        if (part.isMimeType("text/plain")) {
            return part.getContent().toString()
        }

        if (part.isMimeType("text/html")) {
            String html = part.getContent().toString()
            return html.replaceAll("<[^>]*>", " ")
        }

        if (part.isMimeType("multipart/*")) {

            Multipart multipart = (Multipart) part.getContent()

            for (int i = 0; i < multipart.getCount(); i++) {
                String text = getTextFromMessage(multipart.getBodyPart(i))
                if (text != null && !text.trim().isEmpty()) {
                    return text
                }
            }
        }

        return ""
    }
}