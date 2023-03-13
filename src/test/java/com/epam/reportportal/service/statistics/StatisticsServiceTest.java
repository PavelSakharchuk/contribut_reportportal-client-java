/*
 *  Copyright 2020 EPAM Systems
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

package com.epam.reportportal.service.statistics;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.statistics.item.StatisticsItem;
import com.epam.reportportal.test.TestUtils;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.reactivex.Maybe;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import retrofit2.Response;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

	private static final String SEMANTIC_VERSION_PATTERN = "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?";
	private static final String LOCAL_TEST_PATTERN =
			"Client name \"\\$\\{name}\", version \"\\$\\{version}\", interpreter \"Java [^\"]+\"";
	private static final String FULL_PATTERN =
			"Client name \"client-java\", version \"" + SEMANTIC_VERSION_PATTERN + "\", interpreter \"Java [^\"]+\"";

	private static class TestStatisticsService extends StatisticsService {
		private Statistics statistics;

		public TestStatisticsService(ListenerParameters listenerParameters) {
			super(listenerParameters);
		}

		public void setStatistics(Statistics statistics) {
			this.statistics = statistics;
		}

		@Override
		protected Statistics getStatistics() {
			return statistics;
		}
	}

	@Mock
	private Statistics statistics;

	private final Maybe<String> launchMaybe = Maybe.create(emitter -> {
		Thread.sleep(300);
		emitter.onSuccess("launchId");
		emitter.onComplete();
	});

	private TestStatisticsService service;
	private ListenerParameters parameters;

	@BeforeEach
	public void setup() {
		parameters = TestUtils.standardParameters();
		service = new TestStatisticsService(parameters);
		service.setStatistics(statistics);
		when(statistics.send(any())).thenReturn(Maybe.create(e -> e.onSuccess(Response.success(ResponseBody.create(
				MediaType.get("text/plain"),
				""
		)))));
	}

	@Test
	public void test_statistics_send_event_with_agent() {
//		StartLaunchRQ launchRq = TestUtils.standardLaunchRequest(parameters);
//		launchRq.setAttributes(Collections.singleton(new ItemAttributesRQ("agent", "agent-java-testng|test-version-1", true)));
//
//		service.sendEvent(launchMaybe, launchRq);
//		service.close();
//
//		ArgumentCaptor<StatisticsItem> argumentCaptor = ArgumentCaptor.forClass(StatisticsItem.class);
//		verify(statistics, times(1)).send(argumentCaptor.capture());
//
//		StatisticsItem value = argumentCaptor.getValue();
//
//		Map<String, String> params = value.getParams();
//
//		String type = params.get("t");
//		String eventAction = params.get("ea");
//		String eventCategory = params.get("ec");
//		String eventLabel = params.get("el");
//
//		assertThat(type, equalTo("event"));
//		assertThat(eventAction, equalTo("Start launch"));
//		assertThat(eventCategory, anyOf(matchesRegex(LOCAL_TEST_PATTERN), matchesRegex(FULL_PATTERN)));
//		assertThat(eventLabel, equalTo("Agent name \"agent-java-testng\", version \"test-version-1\""));
	}

	@Test
	public void test_statistics_send_event_no_agent_record() {
//		StartLaunchRQ launchRq = TestUtils.standardLaunchRequest(parameters);
//
//		service.sendEvent(launchMaybe, launchRq);
//		service.close();
//
//		ArgumentCaptor<StatisticsItem> argumentCaptor = ArgumentCaptor.forClass(StatisticsItem.class);
//		verify(statistics, times(1)).send(argumentCaptor.capture());
//
//		StatisticsItem value = argumentCaptor.getValue();
//		Map<String, String> params = value.getParams();
//
//		String type = params.get("t");
//		String eventAction = params.get("ea");
//		String eventCategory = params.get("ec");
//		String eventLabel = params.get("el");
//
//		assertThat(type, equalTo("event"));
//		assertThat(eventAction, equalTo("Start launch"));
//		assertThat(eventCategory, anyOf(matchesRegex(LOCAL_TEST_PATTERN), matchesRegex(FULL_PATTERN)));
//		assertThat(eventLabel, nullValue());
	}

	@Test
	public void test_statistics_send_event_async() {
		StartLaunchRQ launchRq = TestUtils.standardLaunchRequest(parameters);
		service.sendEvent(launchMaybe, launchRq);
		verify(statistics, timeout(2000).times(1)).send(any());
	}
}
