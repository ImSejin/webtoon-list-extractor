package io.github.imsejin.wnliext.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CipherUtils {

    public static String sha256_base64(String plaintext, boolean salting) {
        return encode(sha256(plaintext, salting));
    }

    public static byte[] sha256(String plaintext, boolean salting) {
        // SHA-256 알고리즘을 사용한다
        byte[] bytes = encrypt(plaintext, "SHA-256");

        return salting ? salting(bytes) : bytes;
    }

    public static String sha512_base64(String plaintext, boolean salting) {
        return encode(sha512(plaintext, salting));
    }

    public static byte[] sha512(String plaintext, boolean salting) {
        // SHA-512 알고리즘을 사용한다
        byte[] bytes = encrypt(plaintext, "SHA-512");

        return salting ? salting(bytes) : bytes;
    }

    private static byte[] encrypt(String plaintext, final String algorithm) {
        MessageDigest md;
        try {
            // 지정한 암호화 알고리즘을 사용한다
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }

        // 해싱하기 전에 해싱 인스턴스를 초기화한다
        md.reset();

        // 바이트배열을 해싱 인스턴스에 넣는다
        md.update(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] bytes = md.digest();

        return bytes;
    }

    private static byte[] salting(byte[] bytes) {
        // 0을 포함한 짝수 번째의 바이트를 반전한다
        for (int i = 0; i < bytes.length; i++) {
            if ((i & 1) == 0) bytes[i] = (byte) ~bytes[i];
        }

        return bytes;
    }

    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decode(String encoded, boolean salting) {
        byte[] bytes = Base64.getDecoder().decode(encoded);

        return salting ? salting(bytes) : bytes;
    }

    public static String byteToHex(byte[] bytes) {
        // %064x에서 x는 16진수이고, 총 64자리의 16진수에서 값이 들어가지 못한 자리는 0으로 채워지게 된다
        // 최종적으로 해싱된 값이 16진수의 64바이트로 나오게 된다
        return String.format("%064x", new BigInteger(1, bytes));
    }

}
