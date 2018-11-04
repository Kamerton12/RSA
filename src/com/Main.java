package com;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args)
    {
        int ditLength = 32;
        Random r = new Random();
        BigInteger p = BigInteger.probablePrime(ditLength, r);
        BigInteger q = BigInteger.probablePrime(ditLength, r);
        BigInteger n = p.multiply(q);
        BigInteger m = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d;
        do
        {
            d = BigInteger.probablePrime(ditLength * 2, r);
        }while(d.compareTo(m) != -1 && d.gcd(m).compareTo(BigInteger.ONE) != 0);
        BigInteger e = getE(d, m);

        String message = "This is test message";
        BigInteger[] codedMessage = encode(message, d, n);
        System.out.println(decode(codedMessage, e, n));

    }

    public static BigInteger[] encode(String message, BigInteger publicKey, BigInteger n)
    {
        BigInteger[] mes = divideMessage(message);
        for(int i = 0; i < mes.length; i++)
        {
            mes[i] = encodeBigInteger(mes[i], publicKey, n);
        }
        return mes;
    }
    public static String decode(BigInteger[] code, BigInteger publicKey, BigInteger n)
    {
        for(int i = 0; i < code.length; i++)
        {
            code[i] = decodeBigInteger(code[i], publicKey, n);
        }
        return combineMessage(code);
    }


    public static BigInteger[] divideMessage(String message)
    {
        BigInteger[] ans = new BigInteger[message.length()];
        char[] msg = message.toCharArray();
        for(int i = 0; i < message.length(); i++)
        {
            ans[i] = BigInteger.valueOf(msg[i]);
        }
        return ans;
    }

    public static String combineMessage(BigInteger[] message)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < message.length; i++)
            sb.append((char)Integer.parseInt(message[i].toString()));
        return sb.toString();
    }

    public static BigInteger encodeBigInteger(BigInteger message, BigInteger e, BigInteger n)
    {
        return message.modPow(e, n);
    }

    public static BigInteger decodeBigInteger(BigInteger encodedMessage, BigInteger d, BigInteger n)
    {
        return encodedMessage.modPow(d, n);
    }


    public static BigInteger getE(BigInteger d, BigInteger m)
    {
        BigInteger[][] E = {{BigInteger.ONE,BigInteger.ZERO},{BigInteger.ZERO,BigInteger.ONE}};
        while(true)
        {
            BigInteger r = m.mod(d);
            if(r.compareTo(BigInteger.ZERO) == 0)
            {
                return E[1][1];
            }
            BigInteger q = m.divide(d);
            BigInteger[][] mult = {{BigInteger.ZERO,BigInteger.ONE},{BigInteger.ONE, q.negate()}};
            E = matrMult(E, mult);
            m = d;
            d = r;
        }
    }

    public static BigInteger[][] matrMult(BigInteger[][] a, BigInteger[][] b)
    {
        BigInteger leftUp = a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
        BigInteger rightUp = a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
        BigInteger leftDown = a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
        BigInteger rightDown = a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));
        return new BigInteger[][]{{leftUp, rightUp},{leftDown, rightDown}};
    }



    public static int getPrime()
    {
        Random r = new Random();
        int ans = Math.abs(r.nextInt()) | 1;
        while(!isPrime(ans))
        {
            ans = Math.abs(r.nextInt()) | 1;
        }
        return ans;
    }

    public static boolean isPrime(int randomNumber)
    {
        if (randomNumber == 2 || randomNumber == 3)
            return true;

        // если n < 2 или n четное - возвращаем false
        if (randomNumber < 2 || randomNumber % 2 == 0)
            return false;

        Random r = new Random();
        int s = 0, t = 0, k = 10;
        int r1 = randomNumber - 1;
        while(r1 % 2 == 0)
        {
            s++;
            r1 >>= 1;
        }
        t = r1;
        for(int ii = 0; ii < k; ii++)
        {
            //System.out.println("k");
            Integer a = (Math.abs(r.nextInt()) % (randomNumber - 2) + 2);

            BigInteger bi = new BigInteger(a.toString());

            int x  = Integer.parseInt(bi.modPow(BigInteger.valueOf(a), BigInteger.valueOf(randomNumber)).toString());
            //System.out.println(x);
            if(x == 1 || x == randomNumber - 1)
                continue;
            for(int ra = 1; ra < s; ra++)
            {
                //System.out.println("s");
                x = (x * x) % randomNumber;
                if(x == 1)
                    return false;
                if(x == randomNumber-1)
                    break;
            }
            if(x != randomNumber -1)
                return false;
        }
        return true;
    }
}
