package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import javax.crypto.SecretKey;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.LazyLoadBase;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler {
   private static final Logger logger = LogManager.getLogger();
   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
   public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
   public static final AttributeKey attrKeyConnectionState = AttributeKey.valueOf("protocol");
   public static final LazyLoadBase CLIENT_NIO_EVENTLOOP = new LazyLoadBase() {
      private static final String __OBFID = "CL_00001241";

      protected NioEventLoopGroup genericLoad() {
         return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
      }

      protected Object load() {
         return this.genericLoad();
      }
   };
   public static final LazyLoadBase CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase() {
      private static final String __OBFID = "CL_00001242";

      protected LocalEventLoopGroup genericLoad() {
         return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
      }

      protected Object load() {
         return this.genericLoad();
      }
   };
   private final EnumPacketDirection direction;
   private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private Channel channel;
   private SocketAddress socketAddress;
   private INetHandler packetListener;
   private IChatComponent terminationReason;
   private boolean isEncrypted;
   private boolean disconnected;
   private static final String __OBFID = "CL_00001240";

   public NetworkManager(EnumPacketDirection packetDirection) {
      this.direction = packetDirection;
   }

   public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
      super.channelActive(p_channelActive_1_);
      this.channel = p_channelActive_1_.channel();
      this.socketAddress = this.channel.remoteAddress();

      try {
         this.setConnectionState(EnumConnectionState.HANDSHAKING);
      } catch (Throwable var3) {
         logger.fatal(var3);
      }
   }

   public void setConnectionState(EnumConnectionState newState) {
      this.channel.attr(attrKeyConnectionState).set(newState);
      this.channel.config().setAutoRead(true);
      logger.debug("Enabled auto read");
   }

   public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {
      this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
   }

   public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
      logger.debug("Disconnecting " + this.getRemoteAddress(), p_exceptionCaught_2_);
      this.closeChannel(new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Internal Exception: " + p_exceptionCaught_2_}));
   }

   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {
      if(this.channel.isOpen()) {
         try {
            p_channelRead0_2_.processPacket(this.packetListener);
         } catch (ThreadQuickExitException var4) {
            ;
         }
      }
   }

   public void setNetHandler(INetHandler handler) {
      Validate.notNull(handler, "packetListener", new Object[0]);
      logger.debug("Set listener of {} to {}", new Object[]{this, handler});
      this.packetListener = handler;
   }

   public void sendPacket(Packet packetIn) {
      if(this.channel != null && this.channel.isOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packetIn, (GenericFutureListener[])null);
      } else {
         this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])null));
      }
   }

   public void sendPacket(Packet packetIn, GenericFutureListener listener, GenericFutureListener ... listeners) {
      if(this.channel != null && this.channel.isOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(packetIn, (GenericFutureListener[])ArrayUtils.add(listeners, 0, listener));
      } else {
         this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[])ArrayUtils.add(listeners, 0, listener)));
      }
   }

   private void dispatchPacket(final Packet inPacket, final GenericFutureListener[] futureListeners) {
      final EnumConnectionState var3 = EnumConnectionState.getFromPacket(inPacket);
      final EnumConnectionState var4 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
      if(var4 != var3) {
         logger.debug("Disabled auto read");
         this.channel.config().setAutoRead(false);
      }

      if(this.channel.eventLoop().inEventLoop()) {
         if(var3 != var4) {
            this.setConnectionState(var3);
         }

         ChannelFuture var5 = this.channel.writeAndFlush(inPacket);
         if(futureListeners != null) {
            var5.addListeners(futureListeners);
         }

         var5.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         this.channel.eventLoop().execute(new Runnable() {
            private static final String __OBFID = "CL_00001243";

            public void run() {
               if(var3 != var4) {
                  NetworkManager.this.setConnectionState(var3);
               }

               ChannelFuture var1 = NetworkManager.this.channel.writeAndFlush(inPacket);
               if(futureListeners != null) {
                  var1.addListeners(futureListeners);
               }

               var1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
         });
      }
   }

   private void flushOutboundQueue() {
      if(this.channel != null && this.channel.isOpen()) {
         while(!this.outboundPacketsQueue.isEmpty()) {
            NetworkManager.InboundHandlerTuplePacketListener var1 = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
            this.dispatchPacket(var1.packet, var1.futureListeners);
         }
      }
   }

   public void processReceivedPackets() {
      this.flushOutboundQueue();
      if(this.packetListener instanceof IUpdatePlayerListBox) {
         ((IUpdatePlayerListBox)this.packetListener).update();
      }

      this.channel.flush();
   }

   public SocketAddress getRemoteAddress() {
      return this.socketAddress;
   }

   public void closeChannel(IChatComponent message) {
      if(this.channel.isOpen()) {
         this.channel.close().awaitUninterruptibly();
         this.terminationReason = message;
      }
   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public void enableEncryption(SecretKey key) {
      this.isEncrypted = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.func_151229_a(2, key)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.func_151229_a(1, key)));
   }

   public boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public boolean hasNoChannel() {
      return this.channel == null;
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public IChatComponent getExitMessage() {
      return this.terminationReason;
   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   public void setCompressionTreshold(int treshold) {
      if(treshold >= 0) {
         if(this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
         }

         if(this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            ((NettyCompressionEncoder)this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
         }
      } else {
         if(this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
            this.channel.pipeline().remove("decompress");
         }

         if(this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
            this.channel.pipeline().remove("compress");
         }
      }
   }

   public void checkDisconnected() {
      if(!this.hasNoChannel() && !this.isChannelOpen() && !this.disconnected) {
         this.disconnected = true;
         if(this.getExitMessage() != null) {
            this.getNetHandler().onDisconnect(this.getExitMessage());
         } else if(this.getNetHandler() != null) {
            this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
         }
      }
   }

   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {
      this.channelRead0(p_channelRead0_1_, (Packet)p_channelRead0_2_);
   }

   static class InboundHandlerTuplePacketListener {
      private final Packet packet;
      private final GenericFutureListener[] futureListeners;
      private static final String __OBFID = "CL_00001244";

      public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener ... inFutureListeners) {
         this.packet = inPacket;
         this.futureListeners = inFutureListeners;
      }
   }
}
