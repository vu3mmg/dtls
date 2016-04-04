import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by mahesh.govind on 2/14/16.
 */
public class BoundedClient implements  Runnable{
    private int port;

    public BoundedClient(int port) {
        this.port = port;
    }

    public void run()  {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            final Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        public void initChannel(final NioDatagramChannel ch) throws Exception {
                            System.out.println("InitChannel");

                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IncommingPacketHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            System.out.printf("Client -  Waiting for reply message %d %s",port,"127.0.0.1");
            try {
                b.bind(new InetSocketAddress("127.0.0.1", port)).sync().channel().closeFuture().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            System.out.print("In Bounded Client Finally");
        }
    }

    public static void main(String[] args) throws Exception {
        int port =7000;
        Thread t =  new Thread (new BoundedClient(port));
        System.out.println("Before thread start");
        t.start();
        System.out.println("After thread start");
        ClientSocket sock = new ClientSocket(port+1);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter the message");
            String msg = scanner.next();
            sock.sendMessage(msg);

        }

    }
}
