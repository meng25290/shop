package xyz.silencelurker.project.shop.easyshop.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author Silence_Lurker
 */
public class TokenUtil {
    @Value("${token.privateKey}")
    private static String privateKey;

    public static String buildToken(Map<String, String> info) {
        var builder = JWT.create();

        var it = info.entrySet().iterator();

        while (it.hasNext()) {
            var data = it.next();
            builder.withClaim(data.getKey(), data.getValue());
        }

        return builder.sign(Algorithm.HMAC512(privateKey));
    }

    public static Map<String, String> decodeToken(String token) {

        var decodeJwt = JWT.require(Algorithm.HMAC512(privateKey)).build().verify(token);

        var data = decodeJwt.getClaims();

        var map = new HashMap<String, String>(data.size());
        var it = data.entrySet().iterator();

        while (it.hasNext()) {
            var item = it.next();
            map.put(item.getKey(), item.getValue().asString());
        }

        return map;
    }

}
