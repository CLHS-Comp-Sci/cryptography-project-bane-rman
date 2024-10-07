import java.util.ArrayList;
import java.util.Scanner;

public class Chachacipher 
{
	public static void main(String[] args) 
	{
	    Scanner input = new Scanner(System.in);
	    
	    System.out.print("Enter the key: ");
	    String tempKey = input.nextLine();
	    
	    System.out.print("Enter the nonce: ");
	    String tempNonce = input.nextLine();
	    
	    System.out.print("Enter the plaintext: ");
	    String plainText = input.nextLine();
	    
	    String[][] keyBox = keyBoxGen(tempKey, tempNonce);
	    
	    String cipherText = encrypt(keyBox, plainText);
	    System.out.println(cipherText);
	    
	    
	    
	    
	    
	    
	    /*
	    ##Testing
	    
	    for (int i=0; i<4; i++)
	    {
	        System.out.println(keyBox[0][i]);
	        test += fromBinary(keyBox[0][i]);
	    }
	    System.out.println(test);
	    
	    String binary = toBinary("test string");
	    System.out.println(binary);
	    String str = fromBinary(binary);
	    System.out.print(str);
	    for (int row = 0; row < 4; row++)
	    {
	        for (int col = 0; col < 4; col++)
	        {
	            System.out.print("Col " + col + ":");
	            System.out.print(fromBinary(keyBox[row][col]) + "|");
	            //System.out.print(keyBox[row][col] + "|");
	        }
	        System.out.println();
	        System.out.println();
	    }
	    */
	    
	}
	
	public static String encrypt(String[][] box, String plainText)
	{
	    for (int i = 0; i < 5; i++)
	    {
	        //Column Shuffle
	        for (int col = 0; col < 4; col++)
	        {
	            String[] tempList = shuffle(box[0][col],box[1][col],box[2][col],box[3][col]);
	            for (int j = 0; j < 4; j++)
	            {
	                box[j][col] = tempList[j];
	            }
	        }
	        
	        //Diagonal Shuffle
	        for (int spot = 0; spot < 4; spot++)
	        {
	            String a = box[0][spot];
	            String b = box[1][(spot+1)%4];
	            String c = box[2][(spot+2)%4];
	            String d = box[3][(spot+3)%4];
	            String[] tempList = shuffle(a,b,c,d);
	            for (int j = 0; j < 4; j++)
	            {
	                box[j][(spot+j)%4] = tempList[j];
	            }
	        }
	    }
	    int i = 0;
	    String keyString = "";
	    plainText = toBinary(plainText);
	    while (plainText.length() > keyString.length())
	    {
	        keyString = box[i%4][0] + box[i%4][1] + box[i%4][2] + box[i%4][3];
	        i++;
	        System.out.println(keyString);
	    }
	    if (keyString.length() > plainText.length())
	    {
	        System.out.println(plainText.length());
	        keyString = keyString.substring(0, plainText.length());
	        System.out.println(keyString);
	    }
	    System.out.println();
	    
	    System.out.println("Plain text:" + plainText);
	    System.out.println("Key string:" + keyString);
	    String cipherText = xor(keyString,plainText);
	    System.out.println("Cipher binary:" + cipherText);
	    return fromBinary(cipherText);
	}
	
	
	
	
	
	
	public static String[][] keyBoxGen(String key, String nonce)
	{
		String[][] box = new String[4][4];
		//Keybox constant
		String constant = "expand 32-byte k";
		for (int i = 0; i < 4; i++)
		{
		    box[0][i] = toBinary(constant.substring(i*4, i*4+4));
		}
		
		//Nonce generation
		if (nonce.equals(""))
		{
		    String tempNonce = "";
		    for (int i = 0; i <= 64; i++)
		    {
		        if (Math.random() < .5)
		        {
		            tempNonce += "0";
		        }
		        else
		        {
		            tempNonce += "1";
		        }
		    }
		    box[3][2] = tempNonce.substring(0,32);
		    box[3][3] = tempNonce.substring(32);
		    //System.out.print(tempNonce);
		}
		else
		{
		    String tempNonce = toBinary(nonce);
		    while (tempNonce.length() < 64)
		    {
		        tempNonce += tempNonce; 
		    }
		    if (tempNonce.length() > 64)
		    {
		        tempNonce = tempNonce.substring(0, 64);
		    }
		    box[3][2] = tempNonce.substring(0,32);
		    box[3][3] = tempNonce.substring(32);
		    //System.out.print(tempNonce);
		}
		
		//Insert key into box
		String keyString = toBinary(key);
		while (keyString.length() < 320)
		{
		    keyString += keyString;
		}
		if (keyString.length() > 320)
		{
		    keyString = keyString.substring(0,320);
		}
		for (int i = 0; i < 10; i++)
		{
		    box[i/4+1][i%4] = keyString.substring(i*32, i*32+32);
		}
		
		
		
		/*for (int i = 0; i < 4; i++)
		{
		    box[1][i] = keyString.substring(i*32, i*32+32);
		}
		keyString = keyString.substring(128);
		for (int i = 0; i < 4; i++)
		{
		    box[2][i] = keyString.substring(i*32, i*32+32);
		}
		keyString = keyString.substring(128);
		for (int i = 0; i < 2; i++)
		{
		    box[3][i] = keyString.substring(i*32, i*32+32);
		}*/
		
		return box;
	}
	
	
	
	
	//To and from binary
	public static String toBinary(String word)
	{
	    String s = word;
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
		   int val = b;
		   for (int i = 0; i < 8; i++)
		   {
		      binary.append((val & 128) == 0 ? 0 : 1);
		      val <<= 1;
		   }
		}
		return(binary.toString());
	}
	
	public static String fromBinary(String binary)
	{
	    
	    StringBuilder str = new StringBuilder();
	    String[] letterList = new String[binary.length()/8];
	    for (int i = 0; i < binary.length()/8; i++)
	    {
	        letterList[i] = binary.substring(i*8, i*8+8);
	    }
	    for (String l : letterList)
	    {
	        int charCode = Integer.parseInt(l, 2);
	        str.append(new Character((char)charCode).toString());    
	    }
	    
	    
	    return str.toString();
	}
	
	//To and from decimal
	public static int toDecimal(String binary)
	{
	    int decimal = Integer.parseInt(binary.substring(1),2);  
	    return decimal;
	}
	public static String fromDecimal(int decimal)
	{
	    return Integer.toBinaryString(decimal);
	}
	
	
	
	//XOR two strings
	public static String xor(String num1, String num2)
	{
	    String end = "";
	    for (int i = 0; i < num1.length(); i++)
        {
            // If the Character matches
            if (num1.charAt(i) == num2.charAt(i))
                end += "0";
            else
                end += "1";
        }
        return end;
	}
	
	//Shift string
	public static String shiftLeft(String str, int num)
	{
	    return str.substring(num) + str.substring(0,num);
	}
	
	//Add binary Strings
	public static String addBinary(String num1, String num2)
	{
	    String temp = fromDecimal(toDecimal(num1) + toDecimal(num2));
	    if (temp.length() > 32)
	    {
	        temp = temp.substring(0,32);
	    }
	    while (temp.length() < 32)
	    {
	        temp = "0" + temp;
	    }
	    return temp;
	}
	
	
	
	
	public static String[] shuffle(String a, String b, String c, String d)
	{
	    String[] endStr = new String[4];
	    String tempStr = "";
	    int tempNum = 0;
	    
	    
	    a = addBinary(a,b);
	    d = xor(a,d);
	    d = shiftLeft(d,16);
	    c = addBinary(c,d);
	    b = xor(b,c);
	    b = shiftLeft(b,12);
	    a = addBinary(a,b);
	    d = xor(d,a);
	    d = shiftLeft(d,8);
	    c = addBinary(c,d);
	    b = xor(b,c);
	    b = shiftLeft(b,7);
	    endStr[0] = a;
	    endStr[1] = b;
	    endStr[2] = c;
	    endStr[3] = d;
	    
	    return endStr;
	}
	
	
}
