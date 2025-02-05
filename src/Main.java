import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        if(args.length<1||args.length>2)
            throw new RuntimeException("wrong arguments");

        boolean write = args.length==2;

        if(write) {
            File input = new File(args[0]);
            try {
                BufferedImage image = ImageIO.read(input);
                byte[] password = args[1].getBytes();
                List<Integer> bitlist = new ArrayList();

                for(byte a: password)
                {
                    for(int i=7;i>=0;i--)
                    {
                        bitlist.add((a>>i)&1);
                    }
                }

                int index=0;

                for(int y=image.getHeight()/2;y< image.getHeight()&&index<bitlist.size();y++){
                    for(int x=image.getWidth()/2;x< image.getWidth()&&index<bitlist.size();x++){
                        int argb = image.getRGB(x, y);

                        int alpha = bitlist.get(index++);
                        int red = (argb >> 16) & 0x11111111;
                        int green = (argb >> 8) & 0x11111111;
                        int blue = argb & 0x11111111;

                        int newArgb = (alpha<<24) | (red << 16) | (green << 8) | blue;

                        image.setRGB(x,y,newArgb);

                        System.out.print(alpha+" ");
                        System.out.println((image.getRGB(x,y)>>24)&0x00000001);
                    }
                }


            } catch (IOException e) {
                throw new RuntimeException("cant find file");
            }
        }
        else{
            File input = new File(args[0]);
            List<Integer> bitlist = new ArrayList<>();
            try {
                BufferedImage image = ImageIO.read(input);
                for(int y=image.getHeight()/2;y< image.getHeight();y++){
                    for(int x=image.getWidth()/2;x< image.getWidth();x++){
                        int argb = image.getRGB(x, y);

                        bitlist.add((argb>>24)&0x00000001);
                    }
                }


                for(int i:bitlist)
                    System.out.println(i);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}