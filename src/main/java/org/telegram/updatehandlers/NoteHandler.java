package org.telegram.updatehandlers;

import org.telegram.datatypes.Command;
import org.telegram.datatypes.CommandOption;
import org.telegram.services.MessageService;
import org.telegram.structure.MenuMarkUpConfig;
import org.telegram.structure.NewMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class NoteHandler {
    private boolean nextSave = true;
    private final MessageService messageService;
    private final List<String> allNotes;

    public NoteHandler(MessageService messageService) {
        this.messageService = messageService;
        this.allNotes = new ArrayList<>();
    }

    public List<NewMessage> handler(Command commandActive, CommandOption optionActive, Update update) {
        return switch (optionActive) {
            case SHOW_ALL -> this.showAllNotes(commandActive);
            case NEW -> List.of(openNewSave(update));
            default -> List.of(this.startCommandNote(commandActive));
        };
    }

    private List<NewMessage> showAllNotes(Command commandActive) {
        List<NewMessage> notes = new ArrayList<>();
        for (String note : allNotes) {
            notes.add(new NewMessage().withMessage(note).withMenuMarkUpConfig(new MenuMarkUpConfig().getFromCommand(commandActive, false)));
        }
        return notes;
    }

    private NewMessage openNewSave(Update update) {
        if (!this.nextSave)
            allNotes.add(update.getMessage().getText());
        NewMessage m = new NewMessage()
                .withMessage(this.nextSave ? "Send note to save" : "Note saved")
                .withClearOption(!this.nextSave);
        this.nextSave = !this.nextSave;
        return m;
    }

    private NewMessage startCommandNote(Command commandActive) {
        return new NewMessage()
                .withMessage(this.messageService.get("msg-note-command"))
                .withMenuMarkUpConfig(new MenuMarkUpConfig().getFromCommand(commandActive, true));
    }
}
