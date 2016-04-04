/**
 * Created by mahesh.govind on 2/5/16.
 */
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import org.bouncycastle.crypto.tls.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Scanner;

import static io.netty.buffer.Unpooled.BIG_ENDIAN;
import static io.netty.buffer.Unpooled.buffer;

public class UdpClient {

    public static void main(String args[]){

        ClientSocket sock = new ClientSocket(8989);


        /*
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter the message");
            String msg = scanner.next();
            sock.sendMessage(msg);

        }

     typedef struct __attribute__((__packed__)) {
         uint8 content_type;
         uint16 version;
         uint16 epoch;
         uint48 sequence_number;
         uint16 length;
   } dtls_record_header_t;
   */

    }


}
