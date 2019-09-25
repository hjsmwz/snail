package com.acgist.snail.net.codec;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.acgist.snail.system.exception.NetException;

/**
 * <p>消息处理器适配器</p>
 * 
 * @author acgist
 * @since 1.1.1
 */
public abstract class MessageCodec<T, X> implements IMessageCodec<T> {

	/**
	 * 下一个消息处理器
	 */
	protected final IMessageCodec<X> messageCodec;

	public MessageCodec(IMessageCodec<X> messageCodec) {
		this.messageCodec = messageCodec;
	}

	@Override
	public boolean done() {
		return false;
	}
	
	@Override
	public void decode(T message) throws NetException {
		this.decode(message, null, false);
	}

	@Override
	public void decode(T message, InetSocketAddress address) throws NetException {
		this.decode(message, address, true);
	}
	
	/**
	 * 消息解码
	 * 
	 * @param message 消息
	 * @param address 地址
	 * @param hasAddress 是否包含地址
	 */
	protected abstract void decode(T message, InetSocketAddress address, boolean hasAddress) throws NetException;
	
	/**
	 * 执行下一个处理器
	 * 
	 * @param message 消息
	 * @param address 地址
	 * @param hasAddress 是否含有地址
	 */
	protected void doNext(X message, InetSocketAddress address, boolean hasAddress) throws NetException {
		if(hasAddress) {
			if(this.messageCodec.done()) {
				this.messageCodec.onMessage(message, address);
			} else {
				this.messageCodec.decode(message, address);
			}
		} else {
			if(this.messageCodec.done()) {
				this.messageCodec.onMessage(message);
			} else {
				this.messageCodec.decode(message);
			}
		}
	}
	
	@Override
	public String encode(String message) {
		return this.messageCodec.encode(message);
	}
	
	@Override
	public void encode(ByteBuffer message) {
		this.messageCodec.encode(message);
	}

}
