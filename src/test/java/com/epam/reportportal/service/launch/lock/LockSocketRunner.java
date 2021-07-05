/*
 *  Copyright 2021 EPAM Systems
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.epam.reportportal.service.launch.lock;

import com.epam.reportportal.listeners.ListenerParameters;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.epam.reportportal.service.launch.lock.LockTestUtil.WELCOME_MESSAGE;

/**
 * An utility class which runs LockFile from a new application context. Used for testing LockFile service in separate processes.
 *
 * @author <a href="mailto:vadzim_hushchanskou@epam.com">Vadzim Hushchanskou</a>
 */
public class LockSocketRunner {

	public static void main(String[] args) throws IOException {
		String instanceUuid = args[0];

		ListenerParameters params = new ListenerParameters();
		LaunchIdLockSocket lock = new LaunchIdLockSocket(params);
		System.out.println(WELCOME_MESSAGE);
		InputStreamReader isr = new InputStreamReader(System.in);
		//noinspection ResultOfMethodCallIgnored
		isr.read(new char[3]);
		System.out.println(lock.obtainLaunchUuid(instanceUuid));
		//noinspection ResultOfMethodCallIgnored
		isr.read(new char[3]);
		lock.finishInstanceUuid(instanceUuid);
		isr.close();
	}
}
