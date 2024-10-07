import java.util.Scanner;

public class customCipher
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        
        while (true)
        {
            System.out.print("Encrypt or Decrypt? ");
            String str = input.nextLine();
            if (str.equals("Encrypt"))
            {
                System.out.print("Enter plaintext: ");
                String text = input.nextLine();
                System.out.print("Enter key: ");
                String key = input.nextLine();
                while (key.length() < text.length())
                {
                    key += key;
                }
                int[] keyInt = keyMake(key);
                int[] textInt = encrypt(text,keyInt);
                System.out.print("Encrypted: [");
                for (int i = 0; i < textInt.length; i++)
                {
                    if (i < textInt.length-1)
                    {
                        System.out.print(textInt[i] + ",");
                    }
                    else
                    {
                        System.out.print(textInt[i]);
                    }
                }
                System.out.println("]");
            }
            else if (str.equals("Decrypt"))
            {
                System.out.print("How many values? ");
                int length = input.nextInt();
                input.nextLine();
                int[] textInt = new int[length];
                System.out.println("Type values:");
                for (int i = 0; i < length; i++)
                {
                    textInt[i] = input.nextInt();
                }
                input.nextLine();
                System.out.print("Enter key: ");
                String key = input.nextLine();
                while (key.length() < length)
                {
                    key += key;
                }
                int[] keyInt = keyMake(key);
                String deciphered = decrypt(textInt, keyInt);
                System.out.println("Decrypted: " + deciphered);
            }
            else
            {
                break;
            }
        }
        
        
    }
    
    public static int[] keyMake(String key)
    {
        String[] keyList = new String[key.length()];
        int[] keyInt = new int[key.length()];
        for (int i = 0; i < key.length(); i++)
        {
            keyList[i] = key.substring(i,i+1);
        }
        
        for (int i = 0; i < keyList.length; i+=2)
        {
            if (keyList.length > 1)
            {
                String temp = keyList[i];
                keyList[i] = keyList[i+1];
                keyList[i+1] = temp;
            }
        }
        
        for (int i = 0; i < keyList.length; i++)
        {
            int max = keyList.length;
            String bin1 = toBinary(keyList[i]);
            String bin2 = toBinary(keyList[(i+i+1)%max]);
            String temp = xor(bin1,bin2);
            keyInt[i] = toDecimal(temp);
            if (keyInt[i] <= 0)
            {
                keyInt[i] = 1;
            }
        }
        
        return keyInt;
    }
    
    
    public static int[] encrypt(String text, int[] key)
    {
        String[] textList = new String[text.length()];
        int[] textInt = new int[text.length()];
        if (key.length < textList.length)
        {
            
        }
        for (int i = 0; i < text.length(); i++)
        {
            textList[i] = text.substring(i,i+1);
            textList[i] = toBinary(textList[i]);
            textList[i] = textList[i].substring(5) + textList[i].substring(0,5);
            textInt[i] = toDecimal(textList[i]);
            textInt[i] = textInt[i] * key[i];
            textList[i] = fromDecimal(textInt[i]);
            textInt[i] = toDecimal(textList[i]);
        }
        
        
        return textInt;
    }
    
    public static String decrypt(int[] textInt, int[] key)
    {
        String[] textList = new String[textInt.length];
        String text = "";
        for (int i = 0; i < textList.length; i++)
        {
            textList[i] = fromDecimal(textInt[i]);
            textInt[i] = toDecimal(textList[i]);
            textInt[i] = textInt[i] / key[i];
            textList[i] = fromDecimal(textInt[i]);
            while (textList[i].length() < 8)
            {
                textList[i] = "0" + textList[i];
            }
            textList[i] = textList[i].substring(3) + textList[i].substring(0,3);
            textList[i] = fromBinary(textList[i]);
            text += textList[i];
        }
        
        
        return text;
    }
    

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
    
    public static int toDecimal(String binary)
	{
	    int decimal = Integer.parseInt(binary,2);  
	    return decimal;
	}
	public static String fromDecimal(int decimal)
	{
	    return Integer.toBinaryString(decimal);
	}
    
}
