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

package io.github.imsejin;

import io.github.imsejin.console.ConsolePrinter;
import io.github.imsejin.file.model.Webtoon;
import io.github.imsejin.util.PathnameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.github.imsejin.common.ApplicationMetadata.APPLICATION_NAME;
import static io.github.imsejin.excel.ExcelExecutor.createWebtoonList;
import static io.github.imsejin.excel.ExcelExecutor.updateWebtoonList;
import static io.github.imsejin.file.FileFinder.findLatestWebtoonListName;
import static io.github.imsejin.file.FileFinder.findWebtoons;
import static io.github.imsejin.util.StringUtils.isNullOrBlank;

public final class WebtoonListExtractorApplication {

    private WebtoonListExtractorApplication() {
    }

    public static void main(String[] args) {
        // 웹툰이 있는 경로를 첫 번째 인자로 주지 않았으면, jar가 있는 현재 경로로 지정한다
        final String pathname = args == null || args.length == 0 || isNullOrBlank(args[0]) || !Files.isDirectory(Paths.get(args[0]))
                ? PathnameUtils.getCurrentPathname()
                : args[0];

        List<Webtoon> webtoons = findWebtoons(pathname);
        String latestWebtoonListName = findLatestWebtoonListName(pathname);

        try {
            if (isNullOrBlank(latestWebtoonListName)) {
                createWebtoonList(webtoons, pathname);
            } else {
                File webtoonList = new File(pathname, latestWebtoonListName);
                updateWebtoonList(webtoons, pathname, webtoonList);
            }

            ConsolePrinter.clear();
            System.out.println(APPLICATION_NAME + " is successfully done.");
        } catch (Exception ex) {
            ConsolePrinter.clear();
            System.out.println(APPLICATION_NAME + " has failed.");
        }

        // Fix the bug that `ERROR: JDWP Unable to get JNI 1.2 environment`
        System.exit(0);
    }

}
