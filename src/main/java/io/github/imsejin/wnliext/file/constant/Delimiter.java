package io.github.imsejin.wnliext.file.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Delimiter {

    PLATFORM("_"),
    TITLE(" - "),
    AUTHOR(", "),
    COMPLETED(" [\u5B8C]"); // " [完]"

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

}
