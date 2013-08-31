package com.phoney.data;

import com.jme3.math.Matrix4f;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nikola
 */
public class DataManager {
    
    private static final DataManager instance= new DataManager();
    private final int port= 28000;
    private InetSocketAddress address;
    private ByteBuffer bytes;
    private DatagramChannel channel;
    
    public DataManager(){
        bytes= ByteBuffer.allocate(64);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        
        try {
            InetAddress addr= InetAddress.getByName("localhost");
            address= new InetSocketAddress(addr, port);
            channel= SelectorProvider.provider().openDatagramChannel();
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
        
    public static DataManager getInstance(){
        return instance;
    }

    public void send(Matrix4f mat) {
        setMatrixRawData(mat);
        bytes.rewind();
        try {
            channel.send(bytes, address);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    private void setMatrixRawData(Matrix4f mat) {
        float data[]= new float[16];
        mat.fillFloatArray(data, true);
        bytes.rewind();
        for(int i=0; i<data.length; i++){
            bytes.putFloat(data[i]);   
        }
    }
}
