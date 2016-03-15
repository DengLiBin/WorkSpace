package org.heima.lib.core;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.util.Log;

public class PacketConnector {
	private String host;
	private int port;

	private boolean isConnected;
	private NioSocketConnector connector;
	private IoSession ioSession;

	private int poolSize;

	private ConnectListener connectListener;
	private IOListener ioListener;

	private LinkedBlockingQueue<ChatRequest> requestQueue = new LinkedBlockingQueue<ChatRequest>(
			128);

	public PacketConnector(String host, int port, int threadSize) {
		this.host = host;
		this.port = port;
		this.poolSize = threadSize;
	}

	/**
	 * 是否连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * 设置输入输出监听
	 * 
	 * @param listener
	 */
	public void setIOListener(IOListener listener) {
		this.ioListener = listener;
	}

	/**
	 * 设置连接监听
	 * 
	 * @param listener
	 */
	public void setConnectListener(ConnectListener listener) {
		this.connectListener = listener;
	}

	public void connect() {
		if (isConnected) {
			return;
		}
		int count = 0;
		while (count < 3) {
			try {
				Log.d("connector", "connect : start");
				// 对外告知重连
				if (count > 0 && connectListener != null) {
					connectListener.onReConnecting();
				}
				connector = new NioSocketConnector();
				// 设置超时时间
				connector.setConnectTimeoutMillis(30 * 1000);
				// 设置过滤链
				DefaultIoFilterChainBuilder filterChain = connector
						.getFilterChain();
				filterChain.addLast("codec", new ProtocolCodecFilter(
						new TextLineCodecFactory(Charset.forName("utf-8"))));
				// 设置监听
				connector.setHandler(new PacketHandler());

				ConnectFuture future = connector.connect(new InetSocketAddress(
						host, port));
				future.awaitUninterruptibly();
				ioSession = future.getSession();
				isConnected = true;

				// 对外告知已经连接上
				if (connectListener != null) {
					connectListener.onConnected();
				}

				for (int i = 0; i < poolSize; i++) {
					new Thread(new RequestWorker()).start();
				}

				Log.d("connector", "connect : end");
				break;
			} catch (RuntimeIoException e) {
				Log.e("error", "" + e.getMessage());
				isConnected = false;
				count++;
			}
		}
	}

	public void disconnect() {
		try {
			if (connector != null) {
				isConnected = false;
				connector.dispose();
				connector = null;
			}
		} catch (Exception e) {
			Log.e("error", "" + e.getMessage());
		}
	}

	public void addRequest(ChatRequest request) {
		try {
			requestQueue.put(request);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// 请求失败
			if (ioListener != null) {
				ioListener.onOutputFailed(request, e);
			}
		}
	}

	public final class RequestWorker implements Runnable {
		@Override
		public void run() {
			while (isConnected) {
				ChatRequest request = null;
				try {
					request = requestQueue.take();// 阻塞方法

					Log.d("connector", "send : ......");

					ioSession.write(request.getTransport());
				} catch (InterruptedException e) {
					e.printStackTrace();

					// 请求失败
					if (ioListener != null) {
						ioListener.onOutputFailed(request, e);
					}
				}
			}
		}
	}

	private final class PacketHandler implements IoHandler {
		@Override
		public void sessionCreated(IoSession session) throws Exception {
			SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
			cfg.setKeepAlive(true);
			cfg.setReadBufferSize(4 * 1024);
			cfg.setReceiveBufferSize(4 * 1024);
			cfg.setSendBufferSize(4 * 1024);
			cfg.setTcpNoDelay(true);
			cfg.setBothIdleTime(60 * 1000);
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			// session.setAttributeIfAbsent("token", "abc");
			// session.setAttribute("token", "abc");
			Log.d("connector", "sessionOpen : " + session.getId());
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			isConnected = false;
			Log.d("connector", "sessionClosed");
			Log.d("connector", "sessionOpen : " + session.getId());

			if (connectListener != null) {
				connectListener.onDisconnected();
			}
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {

		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {

			// isConnected = false;
			// if (connectListener != null) {
			// connectListener.onDisconnected();
			// }
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			Log.d("receive", " " + message.toString().trim());

			if (ioListener != null) {
				ioListener.onInputComed(session, message);
			}
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
			Log.d("send", " " + message);
		}
	}

	public interface IOListener {
		void onOutputFailed(ChatRequest request, Exception e);

		/**
		 * 有消息进来时
		 * 
		 * @param message
		 */
		void onInputComed(IoSession session, Object message);
	}

	public interface ConnectListener {
		/**
		 * 正在连接
		 */
		void onConnecting();

		/**
		 * 已经连接
		 */
		void onConnected();

		/**
		 * 正在重连
		 */
		void onReConnecting();

		/**
		 * 连接断开
		 */
		void onDisconnected();
	}

}
