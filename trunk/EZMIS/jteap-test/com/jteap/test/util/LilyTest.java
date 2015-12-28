package com.jteap.test.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;

public class LilyTest {
	private List<A> list = new ArrayList<A>();

	public static String listToJson(Collection obj,final String[] fields) {
		JsonConfig jsconfig = new JsonConfig();
		jsconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsconfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				for (String filter : fields) {
					if (name.equals(filter)) {
						return false;
					}
				}
				return true;
			}
		});
		JSONArray json = JSONArray.fromObject(obj, jsconfig);
		return json.toString();
	}
	public static void main(String args[]) {
		int count = 10;
		System.out.println("Input the word to be guess ");
		char[]words=getInputWords().toCharArray();
		char[]result=new char[words.length];
		Arrays.fill(result, '_');
		while(count!=0){
			String input = getInputWords().toLowerCase();
			if(input.length()!=1){
				System.out.println("intput error");
				continue;
			}
			guess(words,input.charAt(0),result);
		    System.out.println("input:"+input+"\t\t"+arrayToString(result));
		    if(checkIsEnd(words,result)) break;
		    count--;
		}
	}
	private static boolean checkIsEnd(char[] words, char[] result) {
		for(int i=0;i<words.length;i++){
			if(words[i]!=result[i]) return false;
		}
		return true;
	}
	public static String getInputWords(){
		Scanner in = new Scanner(System.in);
		String input =in.next();
		return input;
	}
	
	public static void guess(char[] target,char input,char[] result){
		for(int i=0;i<target.length;i++){
			if(target[i]==input){
				result[i]=input;
			}
		}
	}
	
	public static String arrayToString (char[] array){
		StringBuilder sb = new StringBuilder(array.length*2);
		for(char o : array){
			sb.append(o).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public static void test() {
		LilyTest test = new LilyTest();
		test.list.add(new A(1, "a*b;c*d;e*f"));
		test.list.add(new A(2, "g*h;i*j"));
		test.list.add(new A(3, "k*l"));
		// 拿到每个A
		for (A a : test.list) {
			int i = 0;
			for (String s : a.str.split(";")) {
				System.out.println("insert into B (id,seq,num,size) values ('"
						+ a.id + "','" + (++i) + "','" + s.split("\\*")[0]
						+ "','" + s.split("\\*")[1] + "')");
			}
		}
	}
}

class A {
	int id;
	String str;

	A(int id, String str) {
		this.id = id;
		this.str = str;
	}
}