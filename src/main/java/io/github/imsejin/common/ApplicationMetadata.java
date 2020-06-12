package io.github.imsejin.common;

import lombok.experimental.UtilityClass;

/**
 * ApplicationMetadata
 * 
 * @author SEJIN
 */
@UtilityClass
public class ApplicationMetadata {

    public final String APPLICATION_NAME = "WebtoonListExtractor";

    public final String VERSION = "1.1.0";

    public final String VERSION_NAME = "v" + VERSION + ".RELEASE";

    public final String[] APPLICATION_TITLE = { "	                 _                     _    _     _",
                                                "	___ __   ____   | |                   | |  (_)   | |",
                                                "	\\  \\  \\ /   /___| |__  ___  ___  _ ___| |   _ ___| |_",
                                                "	 \\  \\  \\   / __ \\  _ \\/ _ \\/ _ \\| '_  \\ |  | | __|  _|",
                                                "	  \\  \\  \\ /| ___/ |_) )(_) )(_) ) | | | |__| |__ \\ |_",
                                                "	   \\__\\__\\ \\____|\\___/\\___/\\___/|_| |_|____|_|___/\\__|",
                                                "	    _____       _                  _", "	   |  ___|     | |                | |",
                                                "	   | |___ __  _| |_ _ __ __ _  ___| |_  ___  _ __",
                                                "	   |  ___|\\ \\/ /  _| '_ / _` |/ __|  _|/ _ \\| '__|",
                                                "	   | |___ /    \\ |_| | | (_| | (__| |_( (_) ) |",
                                                "	   |_____|__/\\__\\__|_|  \\__,_|\\___|\\__|\\___/|_|",
                                                "	   :: " + APPLICATION_NAME + " ::       (" + VERSION_NAME + ")" };

}
