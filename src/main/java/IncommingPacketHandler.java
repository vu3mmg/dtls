/**
 * Created by mahesh.govind on 2/5/16.
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;


public class IncommingPacketHandler extends  SimpleChannelInboundHandler<DatagramPacket> {

    IncommingPacketHandler( ){
        System.out.println("Initing incoming packet handler");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        final ByteBuf buf = packet.content();
        Channel outbound = ctx.channel();
        System.out.printf("Received Message %s from %s\n",new String(bytes(packet)),packet.sender());
        outbound.write(packet);

    }

    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        final ByteBuf buf = packet.content();
        Channel outbound = ctx.channel();
        System.out.printf("Received Message %s from %s\n",new String(bytes(packet)),packet.sender());
        outbound.write(packet);

    }

    protected byte[] bytes(DatagramPacket msg) {
        ByteBuf content = msg.content();

        int num = content.readableBytes();
        byte[] message = new byte[num];
        content.readBytes(message);

        return message;

    }
}
