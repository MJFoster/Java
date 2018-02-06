
public class PrimeNotFoundException extends RuntimeException {

	public PrimeNotFoundException(String msg)
	{
		super (msg + "\nNo Twin Prime Numbers Within Given Table Size Range\n");
	}
}
