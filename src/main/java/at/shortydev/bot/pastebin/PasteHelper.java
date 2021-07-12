package at.shortydev.bot.pastebin;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class PasteHelper {

    public static String createPaste(String[] content) {
        final PastebinFactory factory = new PastebinFactory();
        final Pastebin pastebin = factory.createPastebin("devkey");

        final PasteBuilder pasteBuilder = factory.createPaste();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

        pasteBuilder.setTitle("Ticket-Logs: " + content.length + " results");
        pasteBuilder.setRaw(Arrays.stream(content).map(s -> "\n-> " + s.split("%__\\$__%")[0] + " - " + simpleDateFormat.format(new Date(Long.parseLong(s.split("%__\\$__%")[1]))) + ": " + s.split("%__\\$__%")[2]).collect(Collectors.joining()));
        pasteBuilder.setMachineFriendlyLanguage("text");
        pasteBuilder.setVisiblity(PasteVisiblity.Public);
        pasteBuilder.setExpire(PasteExpire.Burn);

        final Paste paste = pasteBuilder.build();
        return pastebin.post(paste).get();
    }

}
