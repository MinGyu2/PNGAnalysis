package pnganalysis;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class IDATToPixel {
    // Decompressing IDAT
    private ByteArrayOutputStream decompressData;
    private byte[] decompressDataArray;
    final private int width;
    final private int height;
    private ArrayList<Integer> recon = null;
    public IDATToPixel(ByteArrayOutputStream compressedDate, int width, int height) throws DataFormatException{
        this.width = width;
        this.height = height;

        // decompress ready
        decompressData = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(compressedDate.toByteArray());
        var decompress = new Inflater();
        decompress.setInput(byteBuffer);

        byte[] temp = new byte[8192];
        int len = 0;
        while((len = decompress.inflate(temp)) != 0){
            decompressData.write(temp, 0, len);
        }
        // decompress completes
        decompressDataArray = decompressData.toByteArray();
        System.out.println(String.format("%d -> %d",compressedDate.size(),decompressData.size()));
    }
    public byte[] getDecompressDate(){
        return decompressDataArray;
    }
    public ArrayList<Integer> getPixel(){
        if(recon != null){
            return recon;
        }
        recon = new ArrayList<>();
        final int bytePerPixel = 4;
        final int stribe = width*bytePerPixel;
        int i = 0;
        for(int r = 0;r<height;r++){
            int filterType = decompressDataArray[i];
            i++;
            for(int c = 0;c < stribe;c++){
                int reconX = 0;
                int fileX = (int)decompressDataArray[i]&0xff;
                i++;
                if(filterType == 0){
                    reconX = fileX;
                }else if(filterType == 1){
                    reconX = fileX + reconA(stribe, bytePerPixel, r, c);
                }else if(filterType == 2){
                    reconX = fileX + reconB(stribe, bytePerPixel, r, c);
                }else if(filterType == 3){
                    reconX = fileX + (reconA(stribe, bytePerPixel, r, c) + reconB(stribe, bytePerPixel, r, c))/2;
                }else if(filterType == 4){
                    reconX = fileX + paethPredictor(reconA(stribe, bytePerPixel, r, c), reconB(stribe, bytePerPixel, r, c), reconC(stribe, bytePerPixel, r, c));
                }
                recon.add(reconX&0xff);
            }
        }
        return recon;
    }
    private int paethPredictor(int a, int b, int c){
        int p = a + b - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        if(pa <= pb && pa <= pc){
            return a;
        }else if(pb <= pc){
            return b;
        }else {
            return c;
        }
    }
    private int reconA(int stribe, int bytePrePixel,int r, int c){
        return (c >= bytePrePixel)?recon.get(r*stribe + c - bytePrePixel):0;
    }
    private int reconB(int stribe, int bytePrePixel,int r, int c){
        return (r > 0)?recon.get((r-1)*stribe + c):0;
    }
    private int reconC(int stribe, int bytePrePixel,int r, int c){
        return (r > 0 && c >= bytePrePixel)?recon.get((r-1)*stribe + c - bytePrePixel):0;
    }
    
    
}
