package org.telegram.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    MESSAGE, OPTION, COMMAND,
    PHOTO, VOICE, DOCUMENT,
    LOCATION, POLL, CONTACT
}
