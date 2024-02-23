package org.telegram.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.telegram.datatypes.MessageType;

import java.util.Objects;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMessage {
    private MessageType messageType = MessageType.MESSAGE;
    private String message;
    private Boolean autoDelete = true;
    private MenuMarkUpConfig menuMarkUpConfig;
    private String photoUrl;
    private PollConfig pollConfig;
    private Boolean clearOption = true;

    public boolean hasMenuMarkUpConfig() {
        return Objects.nonNull(this.menuMarkUpConfig);
    }

    public boolean hasPollConfig() {
        return Objects.nonNull(this.pollConfig);
    }
}
