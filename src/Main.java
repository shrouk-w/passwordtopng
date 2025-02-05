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

                for(int i=0;i<8;i++)
                    bitlist.add(1);

                int index=0;

                for(int y=image.getHeight()/2;y< image.getHeight()&&index<bitlist.size();y++){
                    for(int x=image.getWidth()/2;x< image.getWidth()&&index<bitlist.size();x++){
                        int argb = image.getRGB(x, y);

                        int bit = bitlist.get(index++);

                        argb = (argb & ~0b1) | bit;

                        image.setRGB(x,y,argb);

                    }
                }

                ImageIO.write(image, "png", new File("output.png"));


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
                        bitlist.add(argb&0b1);
                    }
                }

                StringBuilder finalpassword = new StringBuilder();

                byte c = 0;
                int bitCount = 0;


                for (int i = 0; i < bitlist.size(); i++) {
                    c = (byte) (c << 1);
                    c |= bitlist.get(i);
                    bitCount++;

                    if (bitCount == 8) {
                        if((c&0xFF)==0xFF)
                            break;
                        finalpassword.append((char) c);
                        c = 0;
                        bitCount = 0;
                    }
                }

                System.out.println(finalpassword);

            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}