package com.example.jykin.concolor.logic;

/**
 * Created by jykin.
 */

public class Val {
    public static int hexToInt(String hex){
        return Integer.parseInt(hex,16);
    }
    public static int[] hexToInt(String[] hex){
        int[] result = new int[hex.length];
        for(int i = 0; i < result.length; i++)
            result[i] = Integer.parseInt(hex[i],16);
        return result;
    }
    public static String intToHex(int num){
        return Integer.toHexString(num);
    }
    public static String[] hexToInt(int[] nums){
        String[] result = new String[nums.length];
        for(int i = 0; i < result.length; i++)
            result[i] = Integer.toHexString(nums[i]);
        return result;
    }
    public static String assemble(int[] values){
        String result = "#";
        for(int i = 0; i < values.length; i++)
            result += intToHex(values[i]);
        return result;
    }
    public static String assemble(String[] values){
        String result = "#";
        for(int i = 0; i < values.length; i++)
            result += values[i];
        return result;
    }
}
