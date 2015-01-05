package Participant;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Messages.Message;

/** Handle read loop of a participant socket */
public class RunnableParticipantRead implements Runnable {

	protected Participant participant;
	private boolean shouldStop;
	private ObjectInputStream mObjectIS;
	private Socket toMaster;

	public RunnableParticipantRead(Socket toMaster, Participant participant,
			ObjectInputStream mObjectIS) {
		this.participant = participant;
		this.mObjectIS = mObjectIS;
		this.toMaster = toMaster;
		shouldStop = false;
	}

	public void stop() {
		shouldStop = true;
	}

	@Override
	public void run() {
		while (!shouldStop) {
			try {
				if (toMaster.isConnected()) {
					  Message message = (Message) mObjectIS.readObject();
					  participant.receiveMessage(message);
				}
			} catch (EOFException e) {
				shouldStop = true;
			} catch (Exception e) {
				e.printStackTrace();
				// System.err.println("Error: Participant getting I/O streams from client.");
			}
		}
		return;
	}
}