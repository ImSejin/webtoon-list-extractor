package io.github.imsejin.console;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * WorkingProcess
 * 
 * @author SEJIN
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
