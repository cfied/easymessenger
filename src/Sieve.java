public class Sieve {

	public static void main(String[] args) {
		boolean looking = true;
		int n = 20;
		int p = 2;
		int[] sieve_numbers= new int[19];

		//initialise an array 

		for (int i =0; i<sieve_numbers.length; i++) {
			sieve_numbers[i]=i+2;
		}

		//loop works only if p<n

		while(p<n) {
			looking=true;
			System.out.println(p);
			//set multiples of p =0
			for (int i =0; i<sieve_numbers.length; i++) {
				if (sieve_numbers[i]%p==0) {
					sieve_numbers[i]=0;
				}
			}
			//only look for the next number greater than p, then exit
			while (looking) {
				for (int i=0; i<sieve_numbers.length; i++) {
					if (sieve_numbers[i]>p) {
						p=sieve_numbers[i];
						looking=false; 
					}else {
						p=n+1;
						looking=false;
					}
				}
			}
			
			/*int i = p;
			while (i < sieve_numbers.length && looking) {
				if (sieve_numbers[i]>p) {
					p=sieve_numbers[i];
					looking=false; 
				}else {
					i++;
				}
			}
			if(i == sieve_numbers.length){
				p=n+1;
				looking=false;
			}*/
			
		}
	}

	
}

 