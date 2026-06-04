package app.ecoRideCD.DAOconfig;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Cifra simétrica AES-256-GCM para campos sensíveis do Funcionario.
 * A chave é lida da variável de ambiente ENCRYPTION_KEY (Base64, 32 bytes).
 * Formato armazenado: Base64(IV[12] || ciphertext || tag[16])
 */
public class CifraUtil {

    private static final String ALGORITHM  = "AES/GCM/NoPadding";
    private static final int    IV_LEN     = 12;  // bytes
    private static final int    TAG_LEN    = 128; // bits
    private static final String ENV_KEY    = "ENCRYPTION_KEY";

    private static final SecretKey SECRET_KEY;

    static {
        String b64 = System.getenv(ENV_KEY);
        if (b64 == null || b64.isBlank())
            throw new IllegalStateException(
                "[EcoRide] Variável de ambiente " + ENV_KEY + " não definida — impossível cifrar dados.");
        byte[] keyBytes = Base64.getDecoder().decode(b64);
        if (keyBytes.length != 32)
            throw new IllegalStateException(
                "[EcoRide] " + ENV_KEY + " deve ter exactamente 32 bytes (256 bits) em Base64.");
        SECRET_KEY = new SecretKeySpec(keyBytes, "AES");
    }

    public static String cifrar(String plaintext) {
        if (plaintext == null) return null;
        try {
            byte[] iv = new byte[IV_LEN];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LEN, iv));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

            // IV + ciphertext+tag concatenados
            byte[] out = new byte[IV_LEN + encrypted.length];
            System.arraycopy(iv, 0, out, 0, IV_LEN);
            System.arraycopy(encrypted, 0, out, IV_LEN, encrypted.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cifrar dado", e);
        }
    }

    public static String decifrar(String ciphertext) {
        if (ciphertext == null) return null;
        try {
            byte[] raw = Base64.getDecoder().decode(ciphertext);
            byte[] iv  = new byte[IV_LEN];
            System.arraycopy(raw, 0, iv, 0, IV_LEN);
            byte[] data = new byte[raw.length - IV_LEN];
            System.arraycopy(raw, IV_LEN, data, 0, data.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, new GCMParameterSpec(TAG_LEN, iv));
            return new String(cipher.doFinal(data), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decifrar dado", e);
        }
    }

    // Cifra um float: converte para String e cifra
    public static String cifrarFloat(double value) {
        return cifrar(String.valueOf(value));
    }

    // Decifra para double
    public static double decifrarFloat(String ciphertext) {
        return Double.parseDouble(decifrar(ciphertext));
    }
}
