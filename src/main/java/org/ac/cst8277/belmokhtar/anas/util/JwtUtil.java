package org.ac.cst8277.belmokhtar.anas.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public class JwtUtil {
   private static String secret = "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyIiwianRpIjoiNDUxMDZmZTEtOTcxYi00N2VmLWFkMGQtOWY0Yzc0NmZlZTRmIiwiaWF0IjoxNjQ4MzIxNzQ0LCJleHAiOjE2NDgzMjIwNDR9.";

   public static String getUserId(String token) {

      SignedJWT jwsObject = null;
      try {
         jwsObject = SignedJWT.parse(token.substring(6));
      } catch (ParseException e) {
         e.printStackTrace();
         return null;
      }

      JWSVerifier verifier = null;
      try {
         verifier = new MACVerifier(secret);
      } catch (JOSEException e) {
         e.printStackTrace();
         return null;
      }

      try {
         if(jwsObject.verify(verifier)){
            String userId = jwsObject.getJWTClaimsSet().getClaim("UserId").toString();
            System.out.println(userId);
            return userId;
         }
         else{
            return null;
         }
      } catch (JOSEException e) {
         e.printStackTrace();
         return null;
      } catch (ParseException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static boolean isValid(String token) {

      SignedJWT jwsObject = null;
      try {
         jwsObject = SignedJWT.parse(token.substring(6));
      } catch (ParseException e) {
         e.printStackTrace();
         return false;
      }

      JWSVerifier verifier = null;
      try {
         verifier = new MACVerifier(secret);
      } catch (JOSEException e) {
         e.printStackTrace();
         return false;
      }

      try {
         return jwsObject.verify(verifier);

      } catch (JOSEException e) {
         e.printStackTrace();
         return false;
      }
   }
}
