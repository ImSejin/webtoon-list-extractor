/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.imsejin;

import io.github.imsejin.base.action.BaseAction;

public final class WebtoonListExtractorApplication {

	private static final BaseAction baseAction = new BaseAction();

	public static void main(String[] args) {
		baseAction.execute();

		// Fix the bug that `ERROR: JDWP Unable to get JNI 1.2 environment`
		System.exit(0);
	}

}
