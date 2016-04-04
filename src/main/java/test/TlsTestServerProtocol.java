package test;

import org.bouncycastle.crypto.tls.TlsServerProtocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

class TlsTestServerProtocol extends TlsServerProtocol
{
    protected final TlsTestConfig config;

    public TlsTestServerProtocol(InputStream input, OutputStream output, SecureRandom secureRandom, TlsTestConfig config)
    {
        super(input, output, secureRandom);

        this.config = config;
    }
}
