package simple_cryptosystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Crypto {

	static int prime1;
	static int prime2;
	static int product;
	static int publicKeyExponent;
	static String message = "";
	static int[] encodedMessage;
	static int[] encryptedMessage;
	static String decryptedMessage = "";
	static HashMap<Character, Integer> characterEncoding = new HashMap<Character, Integer>();
	
		// Main method
		public static void main(String[] args) {
			
			// Create buffer for user input
			BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
			
			// Good manners
			System.out.println("Welcome to Crypto.");
			System.out.println();
			
			System.out.println("This program performs RSA cryptography on text strings.");
			System.out.println();
			
			System.out.println("First you must create a public key.");
			System.out.println();
			
			// Read in two prime numbers for public key
			try {
				
				System.out.println("Define a range for the two prime numbers you would like to use.");
				System.out.println("Lower limit: ");
				
				// Read in user input
				int lower = Integer.parseInt(buffer.readLine());
				
				System.out.println("Upper limit: ");
				System.out.println();
				
				int upper = Integer.parseInt(buffer.readLine());
				
				// Compute two prime numbers between limits
				prime1 = returnPrimeBetween(lower, upper, 0);
				prime2 = returnPrimeBetween(lower, upper, 1);
				
				// Compute product of prime numbers
				product = prime1 * prime2;
				
				System.out.println("Two prime numbers were automatically generated.");
				System.out.println();
				
				System.out.println("First prime number: " + prime1);
				System.out.println("Second prime number: " + prime2);
				System.out.println();
				
				System.out.println("Product of primes: " + product);
				System.out.println();
				
				// Generate public key exponent
				generatePublicKeyExponent();
				
				System.out.println("The public key is: (" + product + ", " + publicKeyExponent + ")");
				System.out.println();
				
				// Map each character [a-z][A-Z] to an integer 0-25
				fillCharacterEncodingHashMap();
				
				System.out.println("Enter the text message you would like to send securely.");
				System.out.println("NOTE: Message can only contain [a-z][A-Z] letters.");
				System.out.println("Terminate the text message with $");
				
				boolean dollarSignStop = false;
				char c;
				
				// While $ has not been parsed from text string
				while(!dollarSignStop) {
					// Read from in buffer
					c = (char) buffer.read();
					if((int) c == 36)
						// Dollar sign parsed
						dollarSignStop = true;
					else {
					// Append character to text string
					message += c;
					}
				}
				
				// Convert message to uppercase 
				String uppercaseMessage = message.toUpperCase();
				
				// Parse uppercaseMessage to char array for processing
				char[] textString = uppercaseMessage.toCharArray();
				encodedMessage = new int[textString.length];
				
				char current;
				int numIndex;
				
				// Create encoded message
				for (int i = 0; i < textString.length; i++) {
					current = textString[i];
					numIndex = characterEncoding.get(current);
					encodedMessage[i] = numIndex;
				}
				
				// Create encrypted message
				encrypt();
				
				System.out.println();
				System.out.println("Message encrypted.");
				System.out.println();
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // End of try-catch statement
			
			// Good manners
			System.out.println();
			System.out.println("Goodbye.");
			
		} // End of main method
		
		// METHODS BELOW...
		
		// Check whether a number is prime
		public static boolean checkPrime(int number) {
		
			if (number % 2 == 0 && number != 2) {
				return false;
			}
			
			for (int i = 3; i < number/2; i+=2) {
				if (number % i == 0) {
					return false;
				}
			}
			
			return true;
		} // End of checkPrime method
		
		// Return the first prime number between start and end
		public static int returnPrimeBetween(int start, int end, int which) {
			
			int count = 0;
			
			// Return first prime number found between argument ints
			if (which == 0) {
			for (int i = start; i < end; i++) {
				if (checkPrime(i)) {
					return i;
				}
			  }
			}
			
			// Return second prime number found between argument ints
			if (which == 1) {
				for (int i = start; i < end; i++) {
					
					if (count == 1 && checkPrime(i)) {
						return i;
					}
					if (count == 0 && checkPrime(i)) {
						count = 1;
					}
				}
			}
			
			// Default prime number return 
			return 17;
		} // End of returnPrimeBetween method
		
		// Compute greatest common divisor of a and b
		public static int gcd(int a, int b) {
	        int r;
	        while(b!=0) {
	            r = (a%b);
	            a = b;
	            b = r;
	        }
	        return a;
	  } // End of gcd method
		
		// Find a number that is less than and relatively prime to relativePrimeCheck
		public static void generatePublicKeyExponent() {
			
			int relativePrimeCheck = ( (prime1-1)*(prime2-1) );
			
			for (int i = 3; i < relativePrimeCheck; i +=2) {
				if (gcd(i, relativePrimeCheck) == 1) {
					publicKeyExponent = i;
					break;
				}
			}
		} // End of generatePublicKeyExponent method
		
		// Create character hashmap that encodes [a-z] --> [0,25]
		public static void fillCharacterEncodingHashMap() {
			
			// Character [a-z][A-Z]
			char c;
			
			// A = 0, B = 1, ... , Z = 25
			int index = 0;
			
			for (int i = 65; i <=90; i++) {
				c = (char) i;
				characterEncoding.put(c, index);
				index++;
			}
		} // End of fillCharacterEncodingHashMap method
		
		// Encrypt the encodedMessage using modular arithmetic
		public static void encrypt() {
			
			int characterCode;
			int encryptedCode;
			encryptedMessage = new int[encodedMessage.length];
			
			for (int i = 0; i < encryptedMessage.length; i++) {
				characterCode = encodedMessage[i];
				encryptedCode = ((int) Math.pow(characterCode, publicKeyExponent) % product);
				encryptedMessage[i] = encryptedCode;
			}
		} // End of encrypt method
		
		// Decrypt the encryptedMessage using modular arithmetic
		public static void decrypt() {}
}
