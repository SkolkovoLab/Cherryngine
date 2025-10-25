package ru.cherryngine.lib.minecraft.protocol.cryptography

import java.nio.charset.Charset
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object EncryptionUtil {
    const val TRANSFORMATION = "AES/CFB8/NoPadding"
    val DIGEST_CHARSET = Charset.forName("ISO_8859_1")
    var keyPair: KeyPair = generateKeyPair()

    fun generateKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(1024)
        return keyGen.generateKeyPair()
    }

    fun digestData(data: String, publicKey: PublicKey, secretKey: SecretKey): ByteArray {
        return digestData("SHA-1", data.toByteArray(DIGEST_CHARSET), secretKey.encoded, publicKey.encoded)
    }

    fun digestData(algorithm: String, vararg data: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance(algorithm)
        data.forEach { array ->
            digest.update(array)
        }
        return digest.digest()
    }

    fun getDecryptionCipherInstance(playerCrypto: PlayerCrypto): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, playerCrypto.sharedSecret, IvParameterSpec(playerCrypto.sharedSecret!!.encoded))
        return cipher
    }

    fun getEncryptionCipherInstance(playerCrypto: PlayerCrypto): Cipher {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, playerCrypto.sharedSecret, IvParameterSpec(playerCrypto.sharedSecret!!.encoded))
        return cipher
    }

    fun getNewPlayerCrypto(): PlayerCrypto {

        val verificationToken = ByteArray(4)
        ThreadLocalRandom.current().nextBytes(verificationToken)

        return PlayerCrypto(keyPair.public, keyPair.private, verificationToken)
    }

    fun publicRSAKeyFrom(data: ByteArray): PublicKey {
        val spec = X509EncodedKeySpec(data)
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    }
}