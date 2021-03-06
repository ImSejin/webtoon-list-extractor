package io.github.imsejin.wnliext.console;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Working process
 */
@Getter
@Setter
@ToString
@Builder
public class WorkingProcess {

    private String message;

    private int currentProcess;

    private int totalProcess;

}
