package org.telegram.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.telegram.datatypes.Command;
import org.telegram.datatypes.CommandOption;

import java.util.ArrayList;
import java.util.List;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuMarkUpConfig {
    private List<Row> rows;

    public MenuMarkUpConfig getFromCommand(Command commandActive, Boolean isOnNone) {
        this.rows = new ArrayList<>();
        List<MenuMarkUpConfig.Row.Option> options = new ArrayList<>();
        for (CommandOption option : commandActive.getAllOptionsByOn(isOnNone)) {
            if (options.size() >= 3) {
                this.rows.add(new MenuMarkUpConfig.Row().withOptions(options));
                options = new ArrayList<>();
            }
            options.add(new MenuMarkUpConfig.Row.Option()
                    .withText(option.getDescription())
                    .withCallBack(option.getCallBack()));
        }
        if (options.size() > 0)
            this.rows.add(new MenuMarkUpConfig.Row().withOptions(options));
        return this;
    }

    @With
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        List<Option> options;

        @With
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Option {
            private String text;
            private String callBack;
        }
    }
}
