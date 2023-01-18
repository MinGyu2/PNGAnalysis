package pnganalysis;

public class IHDRofPNG {
    final static String type = "IHDR"; 
    private int width = 0;
    private int height = 0;
    private int bitDepth = 0;
    private int colorType = 0;
    private int compressionMethod = 0;
    private int filterMethod = 0;
    private int interlaceMethod = 0;
    public IHDRofPNG(byte[] data, int startIndex){
        // 13 byte
        byte[] b = new byte[4];
        int size = 4;

        changeByte(data,startIndex,size,b);
        startIndex += size;
        width = getValue(b,size);

        changeByte(data,startIndex,size,b);
        startIndex += size;
        height = getValue(b,size);

        size = 1;
        changeByte(data,startIndex,size,b);
        startIndex += size;
        bitDepth = getValue(b, size);
        
        changeByte(data,startIndex,size,b);
        startIndex += size;
        colorType = getValue(b, size);
        
        changeByte(data,startIndex,size,b);
        startIndex += size;
        compressionMethod = getValue(b, size);

        changeByte(data,startIndex,size,b);
        startIndex += size;
        filterMethod = getValue(b, size);

        changeByte(data,startIndex,size,b);
        startIndex += size;
        interlaceMethod = getValue(b, size);

        // System.out.println("w : " +width);
        // System.out.println("h : " +height);
        // System.out.println("bit : " +bitDepth);
        // System.out.println("ct : " +colorType);
        // System.out.println("cm : " +compressionMethod);
        // System.out.println("fm : " +filterMethod);
        // System.out.println("il : " +interlaceMethod);
        // System.out.println(Arrays.toString(data));
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    private void changeByte(byte[] data, int startIndex, int size, byte[] container){
        for(int i = 0;i<size;i++){
            container[i] = data[startIndex + i];
        }
    }
    private int getValue(byte[] b, int size){
        int v = 0;
        for(int i = 0;i<size;i++){
            v = (v << 8) + ((int)b[i] & 0xff);
        }
        return v;
    }
}
