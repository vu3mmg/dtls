import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.tls.*;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;

public class MockDTLSClient
    extends DefaultTlsClient
{
    protected TlsSession session;

    public MockDTLSClient(TlsSession session)
    {
        this.session = session;
    }

    public TlsSession getSessionToResume()
    {
        return this.session;
    }

    public void notifyAlertRaised(short alertLevel, short alertDescription, String message, Throwable cause)
    {
        PrintStream out = (alertLevel == AlertLevel.fatal) ? System.err : System.out;
        out.println("DTLS client raised alert: " + AlertLevel.getText(alertLevel)
            + ", " + AlertDescription.getText(alertDescription));
        if (message != null)
        {
            out.println(message);
        }
        if (cause != null)
        {
            cause.printStackTrace(out);
        }
    }

    public void notifyAlertReceived(short alertLevel, short alertDescription)
    {
        PrintStream out = (alertLevel == AlertLevel.fatal) ? System.err : System.out;
        out.println("DTLS client received alert: " + AlertLevel.getText(alertLevel)
            + ", " + AlertDescription.getText(alertDescription));
    }

    public ProtocolVersion getClientVersion()
    {
        return ProtocolVersion.DTLSv12;
    }

    public ProtocolVersion getMinimumVersion()
    {
        return ProtocolVersion.DTLSv10;
    }
/*
    public int[] getCipherSuites()
    {
        return Arrays.concatenate(super.getCipherSuites(),
            new int[]
            {
                CipherSuite. TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,
            });
    }
*/

 //   public Hashtable getClientExtensions() throws IOException
   // {
     //   Hashtable clientExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(super.getClientExtensions());
       // TlsExtensionsUtils.addEncryptThenMACExtension(clientExtensions);
        //TlsExtensionsUtils.addExtendedMasterSecretExtension(clientExtensions);
        //{
          //  /*
            // * NOTE: If you are copying test code, do not blindly set these extensions in your own client.
             //*/
           // TlsExtensionsUtils.addMaxFragmentLengthExtension(clientExtensions, MaxFragmentLength.pow2_9);
            //this.addPaddingExtension(clientExtensions, context.getSecureRandom().nextInt(16));
            //TlsExtensionsUtils.addTruncatedHMacExtension(clientExtensions);
       // }
        //return clientExtensions;
    //}

    public  void addPaddingExtension(Hashtable extensions, int dataLength)
            throws IOException
    {
        extensions.put(21, createPaddingExtension(dataLength));
    }

    public  byte[] createPaddingExtension(int dataLength)
            throws IOException
    {
        TlsUtils.checkUint16(dataLength);
        return new byte[dataLength];
    }

    public void notifyServerVersion(ProtocolVersion serverVersion) throws IOException
    {
        super.notifyServerVersion(serverVersion);

        System.out.println("Negotiated " + serverVersion);
    }

    public TlsAuthentication getAuthentication()
        throws IOException
    {
        return new TlsAuthentication()
        {
            public void notifyServerCertificate(org.bouncycastle.crypto.tls.Certificate serverCertificate)
                throws IOException
            {
                Certificate[] chain = serverCertificate.getCertificateList();
                System.out.println("DTLS client received server certificate chain of length " + chain.length);
                for (int i = 0; i != chain.length; i++)
                {
                    Certificate entry = chain[i];
                    // TODO Create fingerprint based on certificate signature algorithm digest
                    System.out.println("    fingerprint:SHA-256 " + TlsTestUtils.fingerprint(entry) + " ("
                        + entry.getSubject() + ")");
                }
            }

            public TlsCredentials getClientCredentials(CertificateRequest certificateRequest)
                throws IOException
            {
                short[] certificateTypes = certificateRequest.getCertificateTypes();
                if (certificateTypes == null || !Arrays.contains(certificateTypes, ClientCertificateType.rsa_sign))
                {
                    return null;
                }

                return TlsTestUtils.loadSignerCredentials(context, certificateRequest.getSupportedSignatureAlgorithms(),
                    SignatureAlgorithm.rsa, "x509-client.pem", "x509-client-key.pem");
            }
        };
    }

    public void notifyHandshakeComplete() throws IOException
    {
        super.notifyHandshakeComplete();

        TlsSession newSession = context.getResumableSession();
        if (newSession != null)
        {
            byte[] newSessionID = newSession.getSessionID();
            String hex = Hex.toHexString(newSessionID);

            if (this.session != null && Arrays.areEqual(this.session.getSessionID(), newSessionID))
            {
                System.out.println("Resumed session: " + hex);
            }
            else
            {
                System.out.println("Established session: " + hex);
            }

            this.session = newSession;
        }
    }
}
