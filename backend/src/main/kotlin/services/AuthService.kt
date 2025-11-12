package team.mediagroup.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*
import com.auth0.jwt.JWTVerifier
class AuthService(secret: String) {

    private val algorithm = Algorithm.HMAC256(secret)
    private val issuer = "evms-app"
    private val validityInMs = 36_000_00 * 24 // 24 hours

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password: String, hash: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), hash)
        return result.verified
    }

    fun generateToken(userId: Int, role: String): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("role", role)
            .withExpiresAt(Date(now + validityInMs))
            .sign(algorithm)
    }


    fun getVerifier(): JWTVerifier {
        return JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
    }
}
