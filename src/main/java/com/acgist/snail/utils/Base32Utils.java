package com.acgist.snail.utils;

/**
 * <p>Base32编码工具</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class Base32Utils {

	/**
	 * 编码字符
	 */
	private static final char[] BASE_32_ENCODE = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'2', '3', '4', '5', '6', '7'
	};
	/**
	 * 解码字符
	 */
	private static final byte[] BASE_32_DECODE;

	static {
		BASE_32_DECODE = new byte[128];
		for (int index = 0; index < BASE_32_DECODE.length; index++) {
			BASE_32_DECODE[index] = (byte) 0xFF;
		}
		for (int index = 0; index < BASE_32_ENCODE.length; index++) {
			BASE_32_DECODE[(int) BASE_32_ENCODE[index]] = (byte) index;
			if (index < 24) {
				BASE_32_DECODE[(int) Character.toLowerCase(BASE_32_ENCODE[index])] = (byte) index;
			}
		}
	}

	/**
	 * 编码
	 */
	public static final String encode(final byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		final char[] chars = new char[((bytes.length * 8) / 5) + ((bytes.length % 5) != 0 ? 1 : 0)];
		for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
			if (index > 3) {
				int value = bytes[j] & (0xFF >> index);
				index = (index + 5) % 8;
				value <<= index;
				if (j < bytes.length - 1) {
					value |= (bytes[j + 1] & 0xFF) >> (8 - index);
				}
				chars[i] = BASE_32_ENCODE[value];
				j++;
			} else {
				chars[i] = BASE_32_ENCODE[((bytes[j] >> (8 - (index + 5))) & 0x1F)];
				index = (index + 5) % 8;
				if (index == 0) {
					j++;
				}
			}
		}
		return new String(chars);
	}

	/**
	 * 解码
	 */
	public static final byte[] decode(final String content) {
		if(content == null) {
			return null;
		}
		final char[] chars = content.toUpperCase().toCharArray();
		final byte[] bytes = new byte[(chars.length * 5) / 8];
		for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
			int value = BASE_32_DECODE[chars[i]];
			if (index <= 3) {
				index = (index + 5) % 8;
				if (index == 0) {
					bytes[j++] |= value;
				} else {
					bytes[j] |= value << (8 - index);
				}
			} else {
				index = (index + 5) % 8;
				bytes[j++] |= (value >> index);
				if (j < bytes.length) {
					bytes[j] |= value << (8 - index);
				}
			}
		}
		return bytes;
	}

}
