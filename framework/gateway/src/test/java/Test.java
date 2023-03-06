import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.utils.MD5Util;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(UUID.randomUUID());

        //SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ="));
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("UjgXWgYPCi6EeWzPhzVc0PGfRu26mw1Z7VV51NvSFagYxHHJaXBibvnVVAB928qLIJbHL/RuwnEB6PEGpCDOYw=="));

        Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

        //Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("xxxxxxxxxxxxxxxxxxxxxxxxxx"));

        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.PS512); //or RS384, RS512, PS256, PS384, PS512, ES256, ES384, ES512

        //System.out.println(keyPair.getPrivate());
        System.out.println(keyPair.getPublic().toString());

        //System.out.println(mapper.writeValueAsString(keyPair));

        String jws = Jwts.builder()
                .setSubject("Joe")
                //.signWith(key,  SignatureAlgorithm.HS512)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.PS256)
                //.signWith(SignatureAlgorithm.HS256, Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ="))
                .compressWith(CompressionCodecs.GZIP)
                .compact();

        System.out.println(jws);

        String userinfo = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();

        System.out.println(userinfo);


        //KeyPair keyPair1 = new KeyPair(pub, pri);

    }

    private static void saveFile(String path, byte[] encode) {
        try {
            Files.writeString(Path.of(path), Base64.encodeBase64String(encode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    public void test2() throws Exception {
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS512);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        saveFile("/opt/cloudexplorer/conf/RSA_PRIVATE_KEY", privateKey.getEncoded());
        saveFile("/opt/cloudexplorer/conf/RSA_PUBLIC_KEY", publicKey.getEncoded());


        String RSA_PUBLIC_KEY = Files.readString(Path.of("/opt/cloudexplorer/conf/RSA_PUBLIC_KEY"));
        String RSA_PRIVATE_KEY = Files.readString(Path.of("/opt/cloudexplorer/conf/RSA_PRIVATE_KEY"));

        KeyFactory factory = KeyFactory.getInstance("DSA");

        publicKey = factory.generatePublic(new X509EncodedKeySpec(Decoders.BASE64.decode(RSA_PUBLIC_KEY)));
        //PublicKey publicKey = (PublicKey) Keys.hmacShaKeyFor(Decoders.BASE64.decode(RSA_PUBLIC_KEY));
        privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Decoders.BASE64.decode(RSA_PRIVATE_KEY)));
        //PrivateKey privateKey = (PrivateKey) Keys.hmacShaKeyFor(Decoders.BASE64.decode(RSA_PRIVATE_KEY));


    }

    @org.junit.jupiter.api.Test
    public void test3(){

        System.out.println(Base64.encodeBase64String(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));

        System.out.println(Encoders.BASE64URL.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
    }
}
