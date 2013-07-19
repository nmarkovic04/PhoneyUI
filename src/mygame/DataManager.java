/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Matrix4f;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nikola
 */
public class DataManager {
    
    private static final DataManager instance= new DataManager();
    private int port;
    private InetAddress address;
    private DatagramSocket clientSocket;
    private DatagramPacket packet;
    
    public DataManager(){
        port= 28000;
        
        try {
            address= InetAddress.getByName("127.0.0.1");
            clientSocket = new DatagramSocket(port, address);
            
        } catch (SocketException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    
    public static DataManager getInstance(){
        return instance;
    }

    public void send(Matrix4f mat) {
        
        byte[] buffer= matrixRawData(mat);
        try {
            if(packet==null){
                packet= new DatagramPacket(buffer, 16, address, port);
            }else{
                packet.setData(buffer);
            }
            clientSocket.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private byte[] matrixRawData(Matrix4f mat) {
        float data[]= new float[16];
        mat.fillFloatArray(data, true);
        ByteBuffer buffer= ByteBuffer.allocate(data.length*4);
        for(int i=0; i<data.length; i++){
            buffer.putFloat(i, data[i]);
        }
        return buffer.array();
    }
}
