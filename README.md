# BigInteger
A BigInteger Library In Java
## Installation
  Drag and drop com.chewychiyu.biginteger into project src
## Usage
```java
  /* create new BigInteger */
  BigInteger big = new BigInteger(str);
  BigInteger big = new BigInteger(big);
  BigInteger big = new BigInteger(num);
  
  /*constants*/
  BigInteger.ZERO;
  BigInteger.ONE;
  BigInteger.TWO;
  BigInteger.TEN;
  
  /* math */
  BigInteger big_c = big_a.add(big_b);
  BigInteger big_c = big_a.sub(big_b);
  BigInteger big_c = big_a.multiply(big_b);  
  BigInteger big_c = big_a.divide(big_b);
  BigInteger big_b = big_a.factorial();
  BigInteger big_c = big_a.pow(big_b);
    
  /* change base */
  BigInteger big_b = big_a.binary();
  BigInteger big_c = big_a.base(big_b);
  
  /* misc */
  BigInteger big_b = big_a.trim();
  BigInteger big_b = big_a.truncate(begin,end);
  BigInteger big_b = big_a.clone();
  BigInteger big_b = big_a.positive();
  BigInteger big_b = big_a.negative();
  boolean b = big_a.equal(big_b);
  boolean b = big_a.even();
  int i = big_a.compare(big_b);
  byte[] arr = big_a.digits();
  String str = big_a.toString();
```