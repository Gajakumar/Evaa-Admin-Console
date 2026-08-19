package utils

import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.BodyPart
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Store
import javax.mail.internet.MimeMultipart
import javax.mail.search.AndTerm
import javax.mail.search.FlagTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

class GmailOTPReader {

    @Keyword
    String getVerificationCode(String email,
                               String appPassword,
                               int retryCount = 6,
                               int delaySeconds = 10) {

        String host = "imap.gmail.com"

        for (int attempt = 1; attempt <= retryCount; attempt++) {

            Store store = null
            Folder inbox = null

            try {

                Properties props = new Properties()
                props.put("mail.store.protocol", "imaps")
                props.put("mail.imaps.host", host)
                props.put("mail.imaps.port", "993")
                props.put("mail.imaps.ssl.enable", "true")
                props.put("mail.imaps.ssl.trust", host)

                Session session = Session.getInstance(props)
                store = session.getStore("imaps")
                store.connect(host, email, appPassword)

                inbox = store.getFolder("INBOX")
                inbox.open(Folder.READ_WRITE)

                SearchTerm searchTerm = new AndTerm(
                        new FlagTerm(new Flags(Flags.Flag.SEEN), false),
                        new AndTerm(
                                new SubjectTerm("MaximEyes 2FA Verification"),
                                new FromStringTerm("do-not-reply@maximeyes.com")
                        )
                )

                Message[] messages = inbox.search(searchTerm)

                KeywordUtil.logInfo("Unread 2FA Emails Found : " + messages.length)

                if (messages.length > 0) {

                    for (int i = messages.length - 1; i >= 0; i--) {

                        Message message = messages[i]

                        String body = getTextFromMessage(message)

                        KeywordUtil.logInfo("Reading email received at : " + message.getReceivedDate())

                        Matcher matcher = Pattern.compile(
                                "Verification\\s*Code\\s*:\\s*(\\d{6})",
                                Pattern.CASE_INSENSITIVE
                        ).matcher(body)

                        if (matcher.find()) {

                            String otp = matcher.group(1)

                            KeywordUtil.logInfo("Verification Code = " + otp)

                            //Mark email as Read
                            message.setFlag(Flags.Flag.SEEN, true)

                            return otp
                        }
                    }
                }

                KeywordUtil.logInfo("Attempt ${attempt}/${retryCount} : OTP email not found. Waiting ${delaySeconds} seconds...")

            } catch (Exception e) {

                KeywordUtil.logInfo("Error : " + e.getMessage())

            } finally {

                if (inbox != null && inbox.isOpen()) {
                    inbox.close(true)
                }

                if (store != null && store.isConnected()) {
                    store.close()
                }
            }

            Thread.sleep(delaySeconds * 1000)
        }

        KeywordUtil.markFailedAndStop("Unable to find MaximEyes 2FA email.")
        return null
    }


    private String getTextFromMessage(Message message) throws Exception {

        if (message.isMimeType("text/plain")) {
            return message.getContent().toString()
        }

        if (message.isMimeType("text/html")) {

            return message.getContent().toString()
                    .replaceAll("<[^>]+>", " ")
                    .replace("&nbsp;", " ")
                    .replaceAll("\\s+", " ")
        }

        if (message.isMimeType("multipart/*")) {
            return getTextFromMultipart((Multipart) message.getContent())
        }

        return ""
    }


    private String getTextFromMultipart(Multipart multipart) throws Exception {

        StringBuilder result = new StringBuilder()

        for (int i = 0; i < multipart.getCount(); i++) {

            BodyPart part = multipart.getBodyPart(i)

            if (part.isMimeType("text/plain")) {

                result.append(part.getContent().toString())

            } else if (part.isMimeType("text/html")) {

                result.append(
                        part.getContent().toString()
                                .replaceAll("<[^>]+>", " ")
                                .replace("&nbsp;", " ")
                                .replaceAll("\\s+", " ")
                )

            } else if (part.getContent() instanceof MimeMultipart) {

                result.append(getTextFromMultipart((Multipart) part.getContent()))
            }
        }

        return result.toString()
    }
}