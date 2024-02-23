package org.telegram.updatehandlers;

import org.telegram.datatypes.Command;
import org.telegram.datatypes.CommandOption;
import org.telegram.services.MessageService;
import org.telegram.structure.NewMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class CommandHandler {
    private final MessageService messageService;
    private final NoteHandler noteHandler;
    private final GalleryHandler galleryHandler;

    public CommandHandler(MessageService messageService) {
        this.messageService = messageService;
        this.noteHandler = new NoteHandler(messageService);
        this.galleryHandler = new GalleryHandler();
    }

    public List<NewMessage> switchCommands(Command commandActive, CommandOption optionActive, Update update) {
        return switch (commandActive) {
            case NOTE -> this.noteHandler.handler(commandActive, optionActive, update);
            case EXIT -> List.of(new NewMessage().withMessage(messageService.get("msg-exit-command")));
            case GALLERY -> this.galleryHandler.readAllGallery();
        };
    }
}
