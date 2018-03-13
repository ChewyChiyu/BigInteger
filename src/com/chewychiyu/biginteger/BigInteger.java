package com.chewychiyu.biginteger;

public class BigInteger {

	private byte[] digits;

	private final String BASE_10_DIGITS = "0123456789";
	private final int BASE = 10;
	private boolean positive = true;

	public final static BigInteger TEN = new BigInteger("10");
	public final static BigInteger ZERO = new BigInteger("0");
	public final static BigInteger ONE = new BigInteger("1");

	public BigInteger(String digit_string){
		if(digit_string.charAt(0) =='-'){
			positive = false;
			digit_string = digit_string.substring(1);
		}
		digits = string_to_array(digit_string);
	}

	public BigInteger(BigInteger big){
		new BigInteger(big.toString());
	}

	public BigInteger(Integer num){
		new BigInteger(""+num);
	}

	public byte[] string_to_array(String digit_string){
		byte[] array = new byte[digit_string.length()];
		for(int index = 0; index < digit_string.length(); index++){
			String possible_digit = digit_string.substring(index,index+1);
			if(!BASE_10_DIGITS.contains(possible_digit)){
				return new byte[]{};
			}
			array[index] = Byte.parseByte(possible_digit,10);
		}
		return array;
	}

	public BigInteger add(BigInteger big){

		if(positive){
			if(!big.positive){
				if(big.positive().compare(this)==1){
					return big.positive().sub(this).negative();
				}else{
					return big.positive().sub(this).positive();
				}
			}
		}else{
			if(!big.positive){
				return add(big.positive()).negative();
			}else{
				return big.sub(positive());
			}
		}

		StringBuilder big_sum = new StringBuilder();
		BigInteger larger = (big.compare(this)==1) ? big : this;
		BigInteger smaller = larger.equals(big) ? this : big;

		boolean carry_over_next = false;
		int decrement = 0;
		while(decrement++ < larger.digits.length){
			int num = larger.digits[larger.digits.length-decrement];
			if(decrement <= smaller.digits.length){
				num += smaller.digits[smaller.digits.length-decrement];
			}
			if(carry_over_next){
				num++;
			}
			carry_over_next = num>=BASE;
			big_sum.insert(0, num%BASE);
		}
		if(carry_over_next){
			big_sum.insert(0, 1);
		}
		return new BigInteger(big_sum.toString());
	}

	public BigInteger sub(BigInteger big){

		if(positive){
			if(!big.positive){
				return add(big.positive());
			}else{
				if(big.compare(this)==1){
					return big.sub(this).negative();
				}
			}
		}else{
			if(!big.positive){
				if(big.compare(this)==1){
					return big.positive().sub(positive());
				}else{
					return positive().sub(big.positive()).negative();
				}
			}else{
				return positive().add(big.positive()).negative();
			}
		}		

		StringBuilder big_sum = new StringBuilder();
		BigInteger minuend = (big.compare(this)==1) ? big : this;
		BigInteger subtrahend = minuend.equals(big) ? this : big;

		int decrement = 0;
		boolean borrow_over = false;
		while(decrement++ < minuend.digits.length){
			int num = minuend.digits[minuend.digits.length-decrement];
			if(borrow_over){
				num--;
				borrow_over = false;
			}
			if(num<0){
				num+=10;
				borrow_over = true;
			}
			if(decrement <= subtrahend.digits.length){
				if(num < subtrahend.digits[subtrahend.digits.length-decrement]){
					num+=10;
					num-= subtrahend.digits[subtrahend.digits.length-decrement];
					borrow_over = true;
				}else{
					num -= subtrahend.digits[subtrahend.digits.length-decrement];
				}
			}
			big_sum.insert(0, num);
		}
		return new BigInteger(big_sum.toString()).trim();
	}

	public BigInteger multiply(BigInteger big){

		if(big.equal(ZERO)||equal(ZERO)){
			return ZERO;
		}

		if(!positive){
			if(!big.positive){
				return positive().multiply(big.positive());
			}else{
				return positive().multiply(big.positive()).negative();
			}
		}else{
			if(!big.positive){
				return positive().multiply(big.positive()).negative();
			}
		}

		BigInteger factor_large = (big.compare(this)==1) ? big : this;
		BigInteger factor_small = factor_large.equals(big) ? this : big;
		BigInteger product = ZERO;

		for(int factor_small_index = factor_small.digits.length-1; factor_small_index >=0; factor_small_index--){
			BigInteger factor_alpha = ZERO;
			int iteration = factor_small.digits.length-factor_small_index-1;
			for(int factor_large_index = factor_large.digits.length-1; factor_large_index >=0; factor_large_index--){
				StringBuilder num = new StringBuilder(""+(factor_large.digits[factor_large_index] *  factor_small.digits[factor_small_index]));
				for(int index = 0; index < iteration; index++){ num.append("0"); } 
				factor_alpha = factor_alpha.add(new BigInteger(num.toString()));
				iteration++;
			}
			product = product.add(factor_alpha);
		}
		return product;
	}

	public BigInteger divide(BigInteger big){
		
		if(positive){
			if(!big.positive){
				return divide(big.positive()).negative();
			}
		}else{
			if(!big.positive){
				return positive().divide(big.positive());
			}else{
				return positive().divide(big).negative();
			}
		}
		
		if(compare(big)==-1){
			return ZERO;
		}
		
		BigInteger dividend = clone(this);
		BigInteger divsor = clone(big);
		BigInteger start_dividend = dividend.truncate(0, divsor.digits.length);
		StringBuilder quotient = new StringBuilder();
		for(int start_divide = start_dividend.digits.length; start_divide <= dividend.digits.length; start_divide++){
			int factor = 0;
			divsor = clone(big);
			while(divsor.compare(start_dividend)!=1){
				divsor = divsor.add(big);
				factor++;
			}
			if(start_divide < dividend.digits.length){
				divsor = divsor.sub(big);
				start_dividend = start_dividend.sub(big.multiply(new BigInteger(""+factor)));
				start_dividend = new BigInteger(start_dividend.toString()+dividend.digits[start_divide]).trim();
			}
			quotient.append(factor);
		}
		return new BigInteger(quotient.toString()).trim();
	}

	public BigInteger factorial(){

		if(!positive){
			return positive().factorial().negative();
		}

		BigInteger factor = clone(this);
		BigInteger counter = ONE;
		while(counter.compare(this)==-1){
			factor = factor.multiply(this.sub(counter));
			counter = counter.add(ONE);
		}
		return factor;
	}

	public BigInteger truncate(int begin, int end){
		if(begin > end || begin*end<0 || end > digits.length){ return ZERO; }
		StringBuilder str = new StringBuilder();
		for(int index = begin; index < end; index++){
			str.append(digits[index]);
		}
		return new BigInteger(str.toString());
	}

	public BigInteger clone(BigInteger big){
		return new BigInteger(big.toString());
	}

	public boolean even(){
		return digits[digits.length-1]%2==0;
	}

	public BigInteger positive(){
		BigInteger big = clone(this);
		big.positive = true;
		return big;
	}

	public BigInteger negative(){
		BigInteger big = clone(this);
		big.positive = false;
		return big;
	}

	public int compare(BigInteger big){
		//boolean compare
		if(big.positive && !positive){
			return -1;
		}else if(!big.positive && positive){
			return 1;
		}else if(big.positive && positive){
			//length compare
			if(big.digits.length > digits.length){
				return -1;
			}else if(big.digits.length < digits.length){
				return 1;
			}else{
				for(int index = 0; index < digits.length; index++){
					if(digits[index] > big.digits[index]){
						return 1;
					}else if(digits[index] < big.digits[index]){
						return -1;
					}
				}
				return 0;
			}
		}else if(!big.positive && !positive){
			//length compare
			if(big.digits.length > digits.length){
				return -1;
			}else if(big.digits.length < digits.length){
				return 1;
			}else{
				for(int index = 0; index < digits.length; index++){
					if(digits[index] < big.digits[index]){
						return 1;
					}else if(digits[index] > big.digits[index]){
						return -1;
					}
				}
				return 0;
			}
		}
		return -1;
	}

	public boolean equal(BigInteger big){
		if(big.digits.length != digits.length){
			return false;
		}
		if(big.positive!=big.positive){
			return false;
		}
		for(int index = 0; index < big.digits.length; index++){
			if(big.digits[index] != digits[index]){
				return false;
			}
		}
		return true;
	}

	public BigInteger trim(){
		StringBuilder str = new StringBuilder();
		if(!positive){
			str.append("-");
		}
		boolean crop_status = true;
		for(int index = 0; index < digits.length; index++){
			if(crop_status && digits[index] == 0){
				continue;
			}
			if(digits[index] != 0){
				crop_status = false;
			}
			str.append(digits[index]);
		}
		if(str.toString().equals("")){
			str.append('0');
		}
		return new BigInteger(str.toString());
	}

	public byte[] get_digits(){
		return digits;
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		if(!positive) { str.insert(0, "-");}
		for(int index = 0; index < digits.length; index++){
			str.append(digits[index]);
		}
		return str.toString();
	}



}
