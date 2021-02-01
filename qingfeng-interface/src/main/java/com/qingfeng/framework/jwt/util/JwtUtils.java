package com.qingfeng.framework.jwt.util;

import com.qingfeng.framework.jwt.constant.SystemConstant;
import com.qingfeng.framework.jwt.entity.CheckResult;
import com.qingfeng.util.GuidUtil;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * @Title: JwtUtils
 * @ProjectName wdata
 * @Description: jwt加密和解密的工具类
 * @author anxingtao
 * @date 2020-4-23 15:32
 */
public class JwtUtils {

	private static String issuer = "vue";

	/**
	 * @Description: 签发JWT
	 * @Param: [id, subject可以是JSON数据 尽可能少, ttlMillis]
	 * @return: java.lang.String
	 * @Author: anxingtao
	 * @Date: 2020-4-23 15:32
	 */
	public static String createJWT(String id, String subject, long ttlMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey secretKey = generalKey();
		JwtBuilder builder = Jwts.builder()
				// 设置header, 可以调用setHeaderParams()方法同时设置token类型和加密算法，加密的默认值是HS256
				.setHeaderParam("typ", "JWT")
				.setId(id)
				.setSubject(subject)   // 主题
				.setIssuer(issuer)     // 签发者
				.setIssuedAt(now)      // 签发时间
				.signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙

		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date expDate = new Date(expMillis);
			builder.setExpiration(expDate); // 过期时间
		}
		return builder.compact();
	}
	/**
	 * 验证JWT
	 * @param jwtStr
	 * @return
	 */
	public static CheckResult validateJWT(String jwtStr) {
		CheckResult checkResult = new CheckResult();
		Claims claims = null;
		try {
			claims = parseJWT(jwtStr);
			checkResult.setSuccess(true);
			checkResult.setClaims(claims);
		} catch (ExpiredJwtException e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_EXPIRE);
			checkResult.setSuccess(false);
//			e.printStackTrace();
		} catch (SignatureException e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
//			e.printStackTrace();
		} catch (Exception e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
//			e.printStackTrace();
		}
		return checkResult;
	}
	public static SecretKey generalKey() {
		byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECERT);
	    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	    return key;
	}
	
	/**
	 * 
	 * 解析JWT字符串
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJWT(String jwt) throws Exception {
		SecretKey secretKey = generalKey();
		return Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(jwt)
			.getBody();
	}
	public static void main(String[] args) throws InterruptedException {
		//小明失效 10s
		String sc = createJWT(GuidUtil.getUuid(),"小明", 10000);
		System.out.println(sc);
		System.out.println(validateJWT(sc).getErrCode());
		System.out.println(validateJWT(sc).getClaims().getId());
//		Thread.sleep(30000);
//		System.out.println(validateJWT(sc).getClaims());

	}
}
