/*
 * MIT License
 *
 * Copyright (c) 2019-2020 Im Sejin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.imsejin.wnliext;

import io.github.imsejin.common.util.PathnameUtils;
import io.github.imsejin.wnliext.console.ConsolePrinter;
import io.github.imsejin.wnliext.file.model.Webtoon;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.github.imsejin.wnliext.common.ApplicationMetadata.APPLICATION_NAME;
import static io.github.imsejin.wnliext.excel.ExcelExecutor.create;
import static io.github.imsejin.wnliext.excel.ExcelExecutor.update;
import static io.github.imsejin.wnliext.file.FileFinder.findLatestWebtoonList;
import static io.github.imsejin.wnliext.file.FileFinder.findWebtoons;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Application {

    public static void main(String[] args) {
        String pathname;
        if (args == null || args.length == 0) {
            pathname = PathnameUtils.getCurrentPathname();
        } else {
            String arg = args[0];
            if (!Files.isDirectory(Paths.get(arg))) {
                throw new RuntimeException(String.format("Invalid pathname: '%s'", arg));
            }

            pathname = arg;
        }

        List<Webtoon> webtoons = findWebtoons(pathname);
        File listFile = findLatestWebtoonList(pathname);

        // Prints console logs.
        webtoons.forEach(System.out::println);
        System.out.printf("%nTotal %,d webtoon%s%n", webtoons.size(), webtoons.isEmpty() ? "" : "s");

        try {
            if (listFile == null) {
                create(webtoons, pathname);
            } else {
                update(webtoons, pathname, listFile);
            }

            ConsolePrinter.printLogo();
            System.out.printf("%s is successfully done.%n", APPLICATION_NAME);
        } catch (Exception e) {
            ConsolePrinter.printLogo();
            System.out.printf("%s has failed.%n", APPLICATION_NAME);
            e.printStackTrace();
        }

        // Fix the bug that `ERROR: JDWP Unable to get JNI 1.2 environment`
        System.exit(0);
    }

}
