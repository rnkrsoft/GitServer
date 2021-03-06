// Copyright (C) 2009 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.rnkrsoft.gitserver.command;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;

public abstract class BaseCommand implements Command, SessionAware {

	
	protected InputStream in;
	protected OutputStream out;
	protected OutputStream err;
	protected ServerSession session;

	private ExitCallback exit;

	public void setInputStream(final InputStream in) {
		this.in = in;
	}

	public void setOutputStream(final OutputStream out) {
		this.out = out;
	}

	public void setErrorStream(final OutputStream err) {
		this.err = err;
	}

	public void setExitCallback(final ExitCallback callback) {
		this.exit = callback;
	}

	public void destroy() {
		exit.onExit(CommandConstants.CODE_OK);
	}

	protected void startThread(final Runnable thunk) {
		new Thread(thunk, "SSH Worker").start();
	}

	/**
	 * Terminate this command and return a result code to the remote client.
	 * <p>
	 * Commands should invoke this at most once. Once invoked, the command may
	 * lose access to request based resources as any callbacks previously
	 * registered with RequestCleanup will fire.
	 * 
	 * @param requestCleanup exit code for the remote client.
	 */
	protected void onExit(final int requestCleanup) {
		exit.onExit(requestCleanup);
	}
	
	protected void onExit(final int requestCleanup, final String message) {
		exit.onExit(requestCleanup, message);
	}

	@Override
	public void setSession(ServerSession session) {
		this.session = session;
	}
}
