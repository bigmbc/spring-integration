/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.config.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.gateway.SimpleMessagingGateway;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mark Fisher
 * @since 2.0
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceActivatorParserTests {

	@Autowired
	private MessageChannel literalExpressionInput;

	@Autowired
	private MessageChannel beanAsTargetInput;

	@Autowired
	private MessageChannel beanAsArgumentInput;

	@Autowired
	private MessageChannel beanInvocationResultInput;


	@Test
	public void literalExpression() {
		Object result = this.sendAndReceive(literalExpressionInput, "hello");
		assertEquals("foo", result);
	}

	@Test
	public void beanAsTarget() {
		Object result = this.sendAndReceive(beanAsTargetInput, "hello");
		assertEquals("HELLO", result);
	}

	@Test
	public void beanAsArgument() {
		Object result = this.sendAndReceive(beanAsArgumentInput, new TestPayload());
		assertEquals("TestBean", result);
	}

	@Test
	public void beanInvocationResult() {
		Object result = this.sendAndReceive(beanInvocationResultInput, "hello");
		assertEquals("helloFOO", result);
	}


	private Object sendAndReceive(MessageChannel channel, Object payload) {
		SimpleMessagingGateway gateway = new SimpleMessagingGateway();
		gateway.setRequestChannel(channel);
		return gateway.sendAndReceive(payload);
	}


	@SuppressWarnings("unused")
	private static class TestBean {

		public String caps(String s) {
			return s.toUpperCase();
		}
	}


	@SuppressWarnings("unused")
	private static class TestPayload {

		public String getSimpleClassName(Object o) {
			return o.getClass().getSimpleName();
		}
	}

}