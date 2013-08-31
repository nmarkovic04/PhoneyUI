/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.game;

import com.jme3.math.Matrix4f;
import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.SelectorProvider;
/**
 *
 * @author Nikola
 */
public class DataManager {
    
    private static final DataManager instance= new DataManager();
    private int port;
    private InetSocketAddress address;
    private ByteBuffer bytes;
    private DatagramChannel channel;
    public DataManager(){
        port= 28000;
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
            int send = channel.send(bytes, address);
            System.out.println("Sending "+send);
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
