package org.telegram.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Command {
    EXIT("/exit", "Exit current command", List.of()),
    NOTE("/note", "Notes", List.of(CommandOption.NEW, CommandOption.SHOW_ALL, CommandOption.DELETE)),
    GALLERY("/gallery", "Show gallery", List.of());

    private final String command;
    private final String description;
    private final List<CommandOption> options;

    public static Command findByCommand(String command) {
        return Arrays.stream(Command.values()).filter(c -> c.getCommand().equals(command)).findFirst().orElse(Command.EXIT);
    }

    public List<CommandOption> getAllOptionsByOn(boolean isOnNone) {
        return this.options.stream().filter(o -> o.getOnNone().equals(isOnNone)).collect(Collectors.toList());
    }

    public CommandOption findByDescription(String description) {
        return this.options.stream().filter(o -> o.getCallBack().equals(description)).findFirst().orElse(CommandOption.NONE);
    }
}
