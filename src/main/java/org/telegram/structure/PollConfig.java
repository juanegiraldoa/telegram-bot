package org.telegram.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollConfig {
    private Boolean multipleAnswers = false;
    private List<String> options;
    private Boolean anonymous = false;
}
