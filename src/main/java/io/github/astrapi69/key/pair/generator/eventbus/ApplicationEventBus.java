/**
 * The MIT License
 *
 * Copyright (C) 2024 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.key.pair.generator.eventbus;

import io.github.astrapi69.design.pattern.eventbus.GenericEventBus;
import io.github.astrapi69.design.pattern.observer.event.EventListener;
import io.github.astrapi69.design.pattern.observer.event.EventObject;
import lombok.Getter;
import lombok.NonNull;

public class ApplicationEventBus
{

	/** The instance. */
	@Getter
	private static final ApplicationEventBus instance = new ApplicationEventBus();

	private ApplicationEventBus()
	{
	}

	/**
	 * Registers a new {@link EventListener} with the specified event source type class to this
	 * EventBus instance
	 *
	 * @param <T>
	 *            the type parameter that represents the event source
	 * @param listener
	 *            the listener to register
	 * @param eventSourceTypeClass
	 *            the class type of the event source
	 */
	@SuppressWarnings("unchecked")
	public static <T> void register(@NonNull final EventListener<EventObject<T>> listener,
		@NonNull final Class<T> eventSourceTypeClass)
	{
		GenericEventBus.register(listener, eventSourceTypeClass);
	}

	/**
	 * Unregisters the given {@link EventListener} with the specified event source type class from
	 * this EventBus
	 *
	 * @param <T>
	 *            the type parameter that represents the event source
	 * @param listener
	 *            the listener to register
	 * @param eventSourceTypeClass
	 *            the class type of the event source
	 */
	@SuppressWarnings("unchecked")
	public static <T> void unregister(@NonNull final EventListener<EventObject<T>> listener,
		@NonNull final Class<T> eventSourceTypeClass)
	{
		GenericEventBus.unregister(listener, eventSourceTypeClass);
	}

	/**
	 * Posts an event to the event bus. The event is dispatched to all registered listeners
	 * associated with the event's class type
	 *
	 * @param <T>
	 *            the type parameter representing the event source
	 * @param source
	 *            the source event to be posted
	 */
	@SuppressWarnings("unchecked")
	public static <T> void post(final T source)
	{
		GenericEventBus.post(source);
	}

}
