/*
 * ArgonMS MapleStory server emulator written in Java
 * Copyright (C) 2011-2013  GoldenKevin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package argonms.center.net.remoteadmin;

import argonms.common.net.SessionCreator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GoldenKevin
 */
public class TelnetListener implements SessionCreator {
	private static final Logger LOG = Logger.getLogger(TelnetListener.class.getName());
	private final ExecutorService bossThreadPool, workerThreadPool;
	private final TelnetCommandProcessor packetProc;
	private final TelnetSession.CommandReceivedDelegate packetDelegate;
	private ServerSocketChannel listener;
	private final AtomicBoolean closeEventsTriggered;

	public TelnetListener(boolean useNio) {
		closeEventsTriggered = new AtomicBoolean(false);
		bossThreadPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
			private final ThreadGroup group;

			{
				SecurityManager s = System.getSecurityManager();
				group = (s != null)? s.getThreadGroup() :
									 Thread.currentThread().getThreadGroup();
			}

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(group, r, "telnet-boss-thread", 0);
				if (t.isDaemon())
					t.setDaemon(false);
				if (t.getPriority() != Thread.NORM_PRIORITY)
					t.setPriority(Thread.NORM_PRIORITY);
				return t;
			}
		});
		workerThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
			private final ThreadGroup group;
			private final AtomicInteger threadNumber = new AtomicInteger(1);

			{
				SecurityManager s = System.getSecurityManager();
				group = (s != null)? s.getThreadGroup() :
									 Thread.currentThread().getThreadGroup();
			}

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(group, r, "telnet-worker-pool-thread-" + threadNumber.getAndIncrement(), 0);
				if (t.isDaemon())
					t.setDaemon(false);
				if (t.getPriority() != Thread.NORM_PRIORITY)
					t.setPriority(Thread.NORM_PRIORITY);
				return t;
			}
		});

		this.packetProc = new TelnetCommandProcessor();
		this.packetDelegate = new TelnetSession.CommandReceivedDelegate() {
			@Override
			public void lineReceived(String message, TelnetClient client) {
				packetProc.process(message, client);
			}
		};
	}

	public boolean bind(int port) {
		try {
			listener = ServerSocketChannel.open();
			listener.socket().bind(new InetSocketAddress(port));
			LOG.log(Level.INFO, "Listening on port {0}", port);
			listener.configureBlocking(false);
			bossThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Selector selector = Selector.open();
						SelectionKey acceptorKey = listener.register(selector, SelectionKey.OP_ACCEPT);
						while (true) {
							selector.select();
							Set<SelectionKey> keys = selector.selectedKeys();

							for (Iterator<SelectionKey> keyIter = keys.iterator(); keyIter.hasNext(); ) {
								SelectionKey key = keyIter.next();
								keyIter.remove();

								if (key == acceptorKey) {
									if (key.isValid() && key.isAcceptable()) {
										try {
											SocketChannel client = listener.accept();
											client.socket().setTcpNoDelay(false);
											client.configureBlocking(false);
											LOG.log(Level.FINE, "Telnet client connected from {0}", client.socket().getRemoteSocketAddress());
											SelectionKey acceptedKey = client.register(selector, SelectionKey.OP_READ);
											TelnetClient clientState = new TelnetClient();
											TelnetSession session = new TelnetSession(client, acceptedKey, clientState, packetDelegate);
											clientState.setSession(session);
											acceptedKey.attach(session);
										} catch (IOException ex) {
											close(ex.getMessage(), ex);
										}
									}
								} else {
									SocketChannel client = (SocketChannel) key.channel();
									final TelnetSession session = (TelnetSession) key.attachment();
									try {
										if (key.isValid() && key.isReadable()) {
											try {
												int read = client.read(session.readBuffer());
												final byte[] message = session.readMessage(read);
												if (message != null && message.length > 0) {
													workerThreadPool.submit(new Runnable() {
														@Override
														public void run() {
															try {
																session.processRead(message);
															} catch (Throwable ex) {
																LOG.log(Level.WARNING, "Uncaught exception while processing packet from telnet client " + session.getClient().getAccountName() + " (" + session.getAddress() + ")", ex);
															}
														}
													});
												}
											} catch (IOException ex) {
												//does an IOException in read always mean an invalid channel?
												session.close(ex.getMessage());
											}
										}
										if (key.isValid() && key.isWritable())
											if (session.tryFlushSendQueue() == 1)
												key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
									} catch (CancelledKeyException e) {
										//don't worry about it - session is already closed
									}
								}
							}
						}
					} catch (IOException ex) {
						close(ex.getMessage(), ex);
					}
				}
			});
			return true;
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Could not bind on port " + port, ex);
			return false;
		}
	}

	public void close(String reason, Throwable reasonExc) {
		if (closeEventsTriggered.compareAndSet(false, true)) {
			try {
				listener.close();
			} catch (IOException ex) {
				LOG.log(Level.WARNING, "Error while unbinding telnet selector (" + listener.socket().getLocalSocketAddress() + ")", ex);
			}
			if (reasonExc == null)
				LOG.log(Level.FINE, "Telnet selector ({0}) closed: {1}", new Object[] { listener.socket().getLocalSocketAddress(), reason });
			else
				LOG.log(Level.FINE, "Telnet selector (" + listener.socket().getLocalSocketAddress() + ") closed: " + reason, reasonExc);
			bossThreadPool.shutdown();
			workerThreadPool.shutdown();
		}
	}
}
