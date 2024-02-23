package org.telegram.bot;

import lombok.SneakyThrows;
import org.telegram.datatypes.Command;
import org.telegram.datatypes.MessageType;
import org.telegram.datatypes.CommandOption;
import org.telegram.services.MessageService;
import org.telegram.structure.MenuMarkUpConfig;
import org.telegram.structure.NewMessage;
import org.telegram.structure.PollConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.updatehandlers.GalleryHandler;
import org.telegram.updatehandlers.UpdateHandler;
import org.telegram.utils.TimerUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Bot extends TelegramLongPollingBot {

    private final UpdateHandler updateHandler;
    private final MessageService messageService;
    private Long chatId;
    private Command commandActive = Command.EXIT;
    private CommandOption optionActive = CommandOption.NONE;

    public Bot() {
        super("6125764902:AAHhdQ0zWIzeCcnfZgNKNDj496rdgarfU2Q");
        this.messageService = new MessageService();
        this.updateHandler = new UpdateHandler(this.messageService);
    }

    @Override
    public String getBotUsername() {
        return "AllBot";
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        this.chatId = this.updateHandler.getChatId(update);
        Message message = this.updateHandler.getMessage(update);
        if (this.updateHandler.validateUser(message.getFrom())) {
            MessageType type = this.updateHandler.getUpdateType(update);
            if (!(MessageType.COMMAND.equals(type) && "/start".equals(message.getText())))
                deleteMessage(message);
            if (MessageType.COMMAND.equals(type))
                setCommandActive(message.getText());
            if (MessageType.OPTION.equals(type))
                setOptionActive(update.getCallbackQuery().getData());
            send(this.updateHandler.handleAllUpdates(update, type, this.commandActive, this.optionActive));
//            System.out.println("End " + type.name() + " " + this.commandActive.name() + " " + this.optionActive.name());
        } else
            send(List.of(new NewMessage().withMessage(this.messageService.get("incorrect-user"))));
    }

    public Bot buildCommands() {
        try {
            List<BotCommand> botCommands = new ArrayList<>();
            for (Command command : Command.values())
                botCommands.add(new BotCommand(command.getCommand(), command.getDescription()));
            SetMyCommands registerBotCommand = new SetMyCommands();
            registerBotCommand.setCommands(botCommands);
            execute(registerBotCommand);
            return this;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCommandActive(final String commandString) {
        this.commandActive = Command.findByCommand(commandString);
    }

    public void setOptionActive(final String optionActive) {
        this.optionActive = this.commandActive.findByDescription(optionActive);
    }

    private void send(List<NewMessage> messages) {
        try {
            for (NewMessage message : messages) {
                Message messageSend = switch (message.getMessageType()) {
                    case MESSAGE -> sendMessage(message);
                    case OPTION -> null;
                    case PHOTO -> sendPhoto(message);
                    case VOICE -> null;
                    case DOCUMENT -> null;
                    case LOCATION -> null;
                    case POLL -> sendPoll(message);
                    case CONTACT -> sendContact(message);
                    default -> null;
                };
                if (message.getClearOption())
                    this.optionActive = CommandOption.NONE;
                if (Objects.nonNull(messageSend) && message.getAutoDelete())
                    deleteMessage(messageSend, TimeUnit.MINUTES, 1);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Message sendMessage(NewMessage message) throws TelegramApiException {
        SendMessage sm = new SendMessage();
        sm.setChatId(this.chatId);
        sm.setText(message.getMessage());
        if (message.hasMenuMarkUpConfig())
            sm.setReplyMarkup(buildMenu(message.getMenuMarkUpConfig()));
        return execute(sm);
    }

    private InlineKeyboardMarkup buildMenu(MenuMarkUpConfig config) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (MenuMarkUpConfig.Row row : config.getRows()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            for (MenuMarkUpConfig.Row.Option option : row.getOptions()) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(option.getText());
                button.setCallbackData(option.getCallBack());
                buttonsRow.add(button);
            }
            keyboard.add(buttonsRow);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    private Message sendPhoto(NewMessage message) throws TelegramApiException {
        SendPhoto sp = new SendPhoto();
        sp.setChatId(this.chatId);
        sp.setPhoto(new InputFile().setMedia(new File(message.getPhotoUrl())));
        if (message.hasMenuMarkUpConfig())
            sp.setReplyMarkup(buildMenu(message.getMenuMarkUpConfig()));
        return execute(sp);
    }

    private Message sendPoll(NewMessage message) throws TelegramApiException {
        final PollConfig pollConfig = message.getPollConfig();
        SendPoll sp = new SendPoll();
        sp.setChatId(this.chatId);
        sp.setQuestion(message.getMessage());
        sp.setOptions(pollConfig.getOptions());
        sp.setAllowMultipleAnswers(pollConfig.getMultipleAnswers());
        sp.setIsAnonymous(pollConfig.getAnonymous());
        if (message.hasMenuMarkUpConfig())
            sp.setReplyMarkup(buildMenu(message.getMenuMarkUpConfig()));
        return execute(sp);
    }

    private Message sendContact(NewMessage message) throws TelegramApiException {
        SendContact sc = new SendContact();
        sc.setChatId(this.chatId);
        return execute(sc);
    }

    private void deleteMessage(Integer messageId) {
        try {
            DeleteMessage dm = new DeleteMessage();
            dm.setChatId(this.chatId);
            dm.setMessageId(messageId);
            execute(dm);
        } catch (TelegramApiException ignore) {
        }
    }

    private void deleteMessage(Message msg) {
        deleteMessage(msg, TimeUnit.SECONDS, 0);
    }

    private void deleteMessage(Message msg, TimeUnit timeUnit, int time) {
        TimerUtils.executeAfterSeconds(() -> deleteMessage(msg.getMessageId()), timeUnit, time);
    }
}
