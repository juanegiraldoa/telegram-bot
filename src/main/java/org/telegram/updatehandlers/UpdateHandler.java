package org.telegram.updatehandlers;

import org.telegram.datatypes.Command;
import org.telegram.datatypes.CommandOption;
import org.telegram.datatypes.MessageType;
import org.telegram.services.MessageService;
import org.telegram.structure.NewMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.List;

public class UpdateHandler {
    private final MessageService messageService;
    private final CommandHandler commandHandler;
    private final static String[] onlyUsername = {"xX_Juan_Xx", "AllJuanBot"};

    public UpdateHandler(MessageService messageService) {
        this.messageService = messageService;
        this.commandHandler = new CommandHandler(messageService);
    }

    public Long getChatId(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId();
        return update.getMessage().getChatId();
    }

    public MessageType getUpdateType(Update update) {
        if (update.hasCallbackQuery())
            return MessageType.OPTION;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasEntities())
                return MessageType.COMMAND;
            if (message.hasPhoto())
                return MessageType.PHOTO;
            if (message.hasVoice())
                return MessageType.VOICE;
            if (message.hasDocument())
                return MessageType.DOCUMENT;
            if (message.hasLocation())
                return MessageType.LOCATION;
            if (message.hasPoll())
                return MessageType.POLL;
            if (message.hasContact())
                return MessageType.CONTACT;
            return MessageType.MESSAGE;
        }
        return null;
    }

    public boolean validateUser(User user) {
        return Arrays.stream(onlyUsername).anyMatch(u -> u.equals(user.getUserName()));
    }

    public Message getMessage(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage();
        return update.getMessage();
    }

    public List<NewMessage> handleAllUpdates(Update update, MessageType type, Command commandActive, CommandOption optionActive) {
//        System.out.println("Start " + type.name() + " " + commandActive.name() + " " + optionActive.name());
        return this.commandHandler.switchCommands(commandActive, optionActive, update);


//        if (MessageType.COMMAND.equals(type) || MessageType.OPTION.equals(type))
//            return this.commandHandler.switchCommands(commandActive, "");
//        return List.of(new NewMessage()
//                .withMessageType(MessageType.MESSAGE)
//                .withMessage(this.messageService.get("delete-message"))
//                .withMenuMarkUpConfig(
//                        new MenuMarkUpConfig()
//                                .withRows(new ArrayList<>() {{
//                                    add(new MenuMarkUpConfig.Row()
//                                            .withOptions(new ArrayList<>() {{
//                                                add(new MenuMarkUpConfig.Row.Option().withText("test").withCallBack("test"));
//                                                add(new MenuMarkUpConfig.Row.Option().withText("test").withCallBack("test"));
//                                            }}));
//                                    add(new MenuMarkUpConfig.Row()
//                                            .withOptions(new ArrayList<>() {{
//                                                add(new MenuMarkUpConfig.Row.Option().withText("test").withCallBack("test"));
//                                                add(new MenuMarkUpConfig.Row.Option().withText("test").withCallBack("test"));
//                                                add(new MenuMarkUpConfig.Row.Option().withText("test").withCallBack("test"));
//                                            }}));
//                                }}))
//                .withPhotoUrl("E:\\Proton\\My files\\Gallery\\FB_IMG_1678844231245.jpg")
//                .withPollConfig(new PollConfig()
//                        .withOptions(new ArrayList<>() {{
//                            add("opcion 1");
//                            add("opcion 2");
//                        }})));
//        return List.of();
    }
}
