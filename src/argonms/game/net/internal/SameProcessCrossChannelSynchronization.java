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

package argonms.game.net.internal;

import argonms.common.util.collections.Pair;
import argonms.game.GameServer;
import argonms.game.character.PlayerContinuation;
import argonms.game.command.CommandTarget;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author GoldenKevin
 */
public class SameProcessCrossChannelSynchronization implements CrossChannelSynchronization {
	private final CrossServerSynchronization handler;
	private SameProcessCrossChannelSynchronization pipe;
	private final byte localCh;
	private final byte targetCh;

	public SameProcessCrossChannelSynchronization(CrossServerSynchronization self, byte localCh, byte remoteCh) {
		this.handler = self;
		this.localCh = localCh;
		this.targetCh = remoteCh;
	}

	public void connect(SameProcessCrossChannelSynchronization other) {
		this.pipe = other;
		other.pipe = this;
	}

	@Override
	public byte[] getIpAddress() throws UnknownHostException {
		return InetAddress.getByName(GameServer.getInstance().getExternalIp()).getAddress();
	}

	@Override
	public int getPort() {
		return GameServer.getChannel(targetCh).getPort();
	}

	@Override
	public void sendPlayerContext(int playerId, PlayerContinuation context) {
		pipe.receivedPlayerContext(playerId, context);
	}

	private void receivedPlayerContext(int playerId, PlayerContinuation context) {
		handler.receivedChannelChangeRequest(targetCh, playerId, context);
	}

	@Override
	public void sendChannelChangeAcceptance(int playerId) {
		pipe.receivedChannelChangeAcceptance(playerId);
	}

	private void receivedChannelChangeAcceptance(int playerId) {
		handler.receivedChannelChangeAcceptance(targetCh, playerId);
	}

	@Override
	public void callPlayerExistsCheck(BlockingQueue<Pair<Byte, Object>> resultConsumer, String name) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), Byte.valueOf(pipe.returnPlayerExistsResult(name))));
	}

	private byte returnPlayerExistsResult(String name) {
		return handler.makePlayerExistsResult(name);
	}

	@Override
	public void sendPrivateChat(byte type, int[] recipients, String name, String message) {
		pipe.receivedPrivateChat(type, recipients, name, message);
	}

	private void receivedPrivateChat(byte type, int[] recipients, String name, String message) {
		handler.receivedPrivateChat(type, recipients, name, message);
	}

	@Override
	public void callSendWhisper(BlockingQueue<Pair<Byte, Object>> resultConsumer, String recipient, String sender, String message) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), Boolean.valueOf(pipe.returnWhisperResult(recipient, sender, message))));
	}

	private boolean returnWhisperResult(String recipient, String sender, String message) {
		return handler.makeWhisperResult(recipient, sender, message, targetCh);
	}

	@Override
	public boolean sendSpouseChat(int recipient, String sender, String message) {
		return pipe.receivedSpouseChat(recipient, sender, message);
	}

	private boolean receivedSpouseChat(int recipient, String sender, String message) {
		return handler.receivedSpouseChat(recipient, sender, message);
	}

	@Override
	public void callSendBuddyInvite(BlockingQueue<Pair<Byte, Object>> resultConsumer, int recipientId, int senderId, String senderName) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), Byte.valueOf(pipe.returnBuddyInviteResult(recipientId, senderId, senderName))));
	}

	private byte returnBuddyInviteResult(int recipientId, int senderId, String senderName) {
		return handler.makeBuddyInviteResult(recipientId, targetCh, senderId, senderName);
	}

	@Override
	public boolean sendBuddyInviteRetracted(int sender, int recipient) {
		return pipe.receivedBuddyInviteRetracted(recipient, sender);
	}

	private boolean receivedBuddyInviteRetracted(int recipient, int sender) {
		return handler.receivedBuddyInviteRetracted(recipient, sender);
	}

	@Override
	public int exchangeBuddyLogInNotifications(int sender, int[] recipients) {
		return pipe.receivedSentBuddyLogInNotifications(sender, recipients);
	}

	private int receivedSentBuddyLogInNotifications(int sender, int[] recipients) {
		return handler.receivedSentBuddyLogInNotifications(sender, recipients, targetCh);
	}

	@Override
	public void sendReturnBuddyLogInNotifications(int recipient, List<Integer> senders, boolean bubble) {
		pipe.receivedReturnedBuddyLogInNotifications(recipient, senders, bubble);
	}

	private void receivedReturnedBuddyLogInNotifications(int recipient, List<Integer> senders, boolean bubble) {
		handler.receivedReturnedBuddyLogInNotifications(recipient, senders, bubble, targetCh);
	}

	@Override
	public boolean sendBuddyInviteAccepted(int sender, int recipient) {
		return pipe.receivedBuddyInviteAccepted(sender, recipient);
	}

	private boolean receivedBuddyInviteAccepted(int sender, int recipient) {
		return handler.receivedBuddyInviteAccepted(sender, recipient, targetCh);
	}

	@Override
	public void sendBuddyLogOffNotifications(int sender, int[] recipients) {
		pipe.receivedBuddyLogOffNotifications(sender, recipients);
	}

	private void receivedBuddyLogOffNotifications(int sender, int[] recipients) {
		handler.receivedBuddyLogOffNotifications(sender, recipients);
	}

	@Override
	public void sendBuddyDeleted(int sender, int recipient) {
		pipe.receivedBuddyDeleted(sender, recipient);
	}

	private void receivedBuddyDeleted(int sender, int recipient) {
		handler.receivedBuddyDeleted(recipient, sender);
	}

	@Override
	public void callSendChatroomInvite(BlockingQueue<Pair<Byte, Object>> resultConsumer, String invitee, int roomId, String inviter) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), Boolean.valueOf(pipe.returnChatroomInviteResult(invitee, roomId, inviter))));
	}

	private boolean returnChatroomInviteResult(String invitee, int roomId, String inviter) {
		return handler.makeChatroomInviteResult(invitee, roomId, inviter);
	}

	@Override
	public boolean sendChatroomDecline(String invitee, String inviter) {
		return pipe.receivedChatroomDecline(invitee, inviter);
	}

	private boolean receivedChatroomDecline(String invitee, String inviter) {
		return handler.receivedChatroomDecline(invitee, inviter);
	}

	@Override
	public void sendChatroomText(String text, int roomId, int sender) {
		pipe.receivedChatroomText(text, roomId, sender);
	}

	private void receivedChatroomText(String text, int roomId, int sender) {
		handler.receivedChatroomText(text, roomId, sender);
	}

	@Override
	public void sendCrossChannelCommandCharacterManipulation(String recipient, List<CommandTarget.CharacterManipulation> updates) {
		pipe.receivedCrossChannelCommandCharacterManipulation(recipient, updates);
	}

	private void receivedCrossChannelCommandCharacterManipulation(String recipient, List<CommandTarget.CharacterManipulation> updates) {
		handler.receivedCrossChannelCommandCharacterManipulation(recipient, updates);
	}

	@Override
	public void callCrossChannelCommandCharacterAccess(BlockingQueue<Pair<Byte, Object>> resultConsumer, String target, CommandTarget.CharacterProperty key) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), pipe.returnCrossChannelCommandCharacterAccessResult(target, key)));
	}

	private Object returnCrossChannelCommandCharacterAccessResult(String target, CommandTarget.CharacterProperty key) {
		return handler.makeCrossChannelCommandCharacterAccessResult(target, key);
	}

	@Override
	public void sendWorldWideNotice(byte style, String message) {
		pipe.receivedWorldWideNotice(style, message);
	}

	private void receivedWorldWideNotice(byte style, String message) {
		handler.receivedWorldWideNotice(style, message);
	}

	@Override
	public void sendServerShutdown(boolean halt, boolean restart, boolean cancel, int seconds, String message) {
		pipe.receivedServerShutdown(halt, restart, cancel, seconds, message);
	}

	private void receivedServerShutdown(boolean halt, boolean restart, boolean cancel, int seconds, String message) {
		handler.receivedServerShutdown(halt, restart, cancel, seconds, message);
	}

	@Override
	public void sendServerRateChange(byte type, short newRate) {
		pipe.receivedServerRateChange(type, newRate);
	}

	private void receivedServerRateChange(byte type, short newRate) {
		handler.receivedServerRateChange(type, newRate);
	}

	@Override
	public void callRetrieveConnectedPlayersList(BlockingQueue<Pair<Byte, Object>> resultConsumer, byte privilegeLevelLimit) {
		resultConsumer.offer(new Pair<Byte, Object>(Byte.valueOf(targetCh), pipe.returnRetrieveConnectedPlayersListResult(privilegeLevelLimit)));
	}

	private Object returnRetrieveConnectedPlayersListResult(byte privilegeLevelLimit) {
		return handler.makeRetrieveConnectedPlayersListResult(privilegeLevelLimit);
	}
}
