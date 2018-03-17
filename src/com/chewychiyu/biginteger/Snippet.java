package com.chewychiyu.biginteger;

public class Snippet {

	
	public static void main(String[] args){
		new Snippet();
	}

	Snippet(){

		/* test area */
		BigInteger b = new BigInteger("32422349134893487934587903420123423048750983857298932894328983458384513489348793458790342012342304875098313489348793458790342012342304875098334");
		BigInteger a = new BigInteger("134891348934879345879034201234230487509833487934587903134893487934587903420123134893487934587903420123423048750983423048750983420123423048750983");
		System.out.println(a.multiply(b));
		
	}
	
	int rand_int(int min, int max){
		return (int)(min + (max-min)*Math.random()+1);
	}
}
