/*
 * Copyright 2013-2023 Peng Li <madding.lip@gmail.com>
 * Licensed under the AQNote License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.aqnote.com/licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aqnote.shared.encrypt.cert.bc.cover;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;
import org.bouncycastle.pkcs.PKCS12PfxPdu;

import com.Ostermiller.util.CircularByteBuffer;
import com.aqnote.shared.encrypt.cert.bc.constant.BCConstant;
import com.aqnote.shared.encrypt.util.StreamUtil;

/**
 * 类PKCSTransformer.java的实现描述：keystore处理工具类
 * 
 * @author madding.lip Dec 7, 2013 12:09:41 AM
 */
public class PKCSTransformer implements BCConstant {

    public static String getCrtFileString(X509Certificate cert) throws Exception {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        PEMWriter pemWriter = new PEMWriter(new PrintWriter(cbb.getOutputStream()));
        cbb.getOutputStream().flush();
        cbb.getOutputStream().close();
        pemWriter.writeObject(cert);
        pemWriter.flush();
        pemWriter.close();
        String crtFile = StreamUtil.stream2Bytes(cbb.getInputStream(), StandardCharsets.UTF_8);
        cbb.getInputStream().close();
        cbb.clear();
        return crtFile;
    }

    public static String getCrtFileB64(X509Certificate x509Cert) throws Exception {
        return Base64.encodeBase64String(x509Cert.getEncoded());
    }

    public static String getCRLFileString(X509CRL x509CRL) throws Exception {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        PEMWriter pemWriter = new PEMWriter(new PrintWriter(cbb.getOutputStream()));
        cbb.getOutputStream().flush();
        cbb.getOutputStream().close();
        pemWriter.writeObject(x509CRL);
        pemWriter.flush();
        pemWriter.close();
        String crlFile = StreamUtil.stream2Bytes(cbb.getInputStream(), StandardCharsets.UTF_8);
        cbb.getInputStream().close();
        cbb.clear();
        return crlFile;
    }

    public static String getCRLFileB64(X509CRL x509CRL) throws Exception {
        return Base64.encodeBase64String(x509CRL.getEncoded());
    }

    public static String getKeyFileString(PrivateKey privKey) throws Exception {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        PEMWriter pemWriter = new PEMWriter(new PrintWriter(cbb.getOutputStream()));
        cbb.getOutputStream().flush();
        cbb.getOutputStream().close();
        pemWriter.writeObject(privKey);
        pemWriter.flush();
        pemWriter.close();
        String keyFile = StreamUtil.stream2Bytes(cbb.getInputStream(), StandardCharsets.UTF_8);
        cbb.getInputStream().close();
        cbb.clear();
        return keyFile;
    }

    public static String getKeyFileString(PrivateKey privKey, char[] pwd) throws Exception {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        PEMWriter pemWriter = new PEMWriter(new PrintWriter(cbb.getOutputStream()));
        cbb.getOutputStream().flush();
        cbb.getOutputStream().close();
        if (pwd != null) {
            JcePEMEncryptorBuilder encryptorBuilder = new JcePEMEncryptorBuilder(DES_EDE3_CBC);
            encryptorBuilder.setProvider(JCE_PROVIDER);
            encryptorBuilder.setSecureRandom(new SecureRandom());
            pemWriter.writeObject(privKey, encryptorBuilder.build(pwd));
        } else {
            pemWriter.writeObject(privKey);
        }
        pemWriter.flush();
        pemWriter.close();
        String keyFile = StreamUtil.stream2Bytes(cbb.getInputStream(), StandardCharsets.UTF_8);
        cbb.getInputStream().close();
        cbb.clear();
        return keyFile;
    }

    public static String getKeyFileStringB64(PrivateKey privKey) throws Exception {
        return Base64.encodeBase64String(privKey.getEncoded());
    }

    public static String getP12FileString(KeyStore keyStore, char[] passwd) throws Exception {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        keyStore.store(cbb.getOutputStream(), passwd);
        cbb.getOutputStream().flush();
        cbb.getOutputStream().close();
        String p12File = Base64.encodeBase64String(StreamUtil.stream2Bytes(cbb.getInputStream()));
        cbb.getInputStream().close();
        cbb.clear();
        return p12File;
    }

    public static String getP12FileString(PKCS12PfxPdu pfxPdu, char[] passwd) throws Exception {
        return Base64.encodeBase64String(pfxPdu.getEncoded(ASN1Encoding.DER));
    }

    public static String getP12FileString2(KeyStore keyStore, char[] passwd) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        keyStore.store(out, passwd);
        out.flush();
        String p12File = Base64.encodeBase64String(out.toByteArray());
        out.close();
        return p12File;
    }

}
